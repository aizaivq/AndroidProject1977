package com.via.recordscreen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.MediaRecorder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by nick on 18-10-31.
 */

public class RecordScreenManager {
    private int mVideoBitrate = 8000 * 1000;
    private int mFrameRate = 30;
    private String mVideoMime = "video/avc";
    private int mWidth = 1280;
    private int mHeight = 720;
    private int mAudioSource = MediaRecorder.AudioSource.MIC;
    private String mAudioMime = "audio/mp4a-latm";
    private int mAudioChannel = 1;
    private int mAudioSampleRate = 48000;
    private int mAudioBitRate = 60000;
    private DisplayManager mDisplayManager;
    private MediaMuxer mMuxer = null;
    private MediaFormat aformat = null;
    private MediaFormat vformat = null;
    private int mSatusRecord = STATUS_RECORD_STOP;
    private static final int STATUS_RECORD_STOP = 0;
    private static final int STATUS_RECORD_START = 1;
    private static final int STATUS_RECORD_PAUSE = 2;
    private int mIdrInterval = 10;
    private static final String TAG = RecordScreenManager.class.getSimpleName();
    private final Object mMuxerLock = this;
    private int mOutFormat = MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4;
    private String mPath;


    private SourceThread mAudio = null;
    private EncodingThread mVidEnc = null;
    private EncodingThread mAudEnc = null;


    private class SourceFormat {
        public byte[] source;
        public int availablesize;
        public long timestamp;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }

    private class SourceThread extends Thread {
        protected EncodingThread mSink = null;
        protected SourceFormat mSourceBuf = new SourceFormat();
        private boolean mPause = false;

        private AudioRecord mRecordSource;
        private long timeUs = 0;

        protected void initSource() {
            int audioChannelMask;
            if (mAudioChannel == 2) {
                audioChannelMask = AudioFormat.CHANNEL_CONFIGURATION_STEREO;
            } else {
                audioChannelMask = AudioFormat.CHANNEL_CONFIGURATION_MONO;
            }
            int bufferSize = AudioRecord.getMinBufferSize(mAudioSampleRate,
                    audioChannelMask,
                    AudioFormat.ENCODING_PCM_16BIT);
            mRecordSource = new AudioRecord(mAudioSource,
                    mAudioSampleRate,
                    audioChannelMask,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize);
            mRecordSource.startRecording();
            mSourceBuf.source = new byte[bufferSize];
        }

        protected boolean pullSource(SourceFormat data) {
            int len = mRecordSource.read(data.source, 0, data.source.length);
            if (len > 0) {
                data.availablesize = len;
                data.timestamp = timeUs;

                //16 bit data, 2 byte for one sample
                timeUs += ((long) len * 1000000 / 2 / mAudioChannel) / mAudioSampleRate;
                return true;
            } else {
                return false;
            }
        }

        protected void stopSource() {
            mRecordSource.stop();
            mRecordSource.release();
            mRecordSource = null;
        }


        protected void writeSink(SourceFormat data) {
            if (mSink != null) {
                mSink.feedInputData(data.source, data.availablesize, data.timestamp);
            }
        }

        public void setSink(EncodingThread sink) {
            mSink = sink;
        }

        public void pauseMe() {
            synchronized (this) {
                mPause = true;
            }
        }

        public void resumeMe() {
            synchronized (this) {
                if (mPause) {
                    mPause = false;
                    this.notify();
                }
            }
        }

        @Override
        public void run() {
            this.initSource();
            while (mSatusRecord != STATUS_RECORD_STOP) {
                Log.v(TAG, "SourceThread run while");
                synchronized (this) {
                    if (mPause) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            Log.d(TAG, "audio source pause error:" + e);
                        }
                    }
                }
                boolean fetched = this.pullSource(mSourceBuf);
                if (fetched) {
                    this.writeSink(mSourceBuf);
                }
            }
            this.stopSource();
        }
    }


    private class EncodingThread extends Thread {
        private VirtualDisplay mVirtualDisplay = null;
        private final MediaFormat mFormat;
        private final int mMuxIndex;
        private MediaCodec mCodec = null;
        private MediaCodec.BufferInfo mBufInfo = null;
        private ByteBuffer[] mInputBuffers = null;
        private ByteBuffer[] mOutputBuffers = null;
        private Surface mCodecSurface;
        private final Object mCodecLock = new Object();
        private boolean mPause = false;
        private final long mStartTime = System.currentTimeMillis();
        private long mPauseTime = 0;
        private long mLastTime = 0;

        private final Paint mPaint;
        private final Rect mRect;

        private boolean mCodecSpecific = false;

        EncodingThread(MediaFormat format) {
            mFormat = format;
            mMuxIndex = mMuxer.addTrack(format);

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(0xffffffff);
            mRect = new Rect(0, 0, mWidth, mHeight);
        }

        public void pauseMe() {
            synchronized (this) {
                mPause = true;
            }
        }

        public void resumeMe() {
            synchronized (this) {
                if (mPause) {
                    mPause = false;
                    this.notify();
                }
            }
        }

        public void feedInputData(byte[] input, int size, long timeUs) {
            int len, left = size;
            int in_offset = 0;
            Log.d(TAG, "feedInputData size:" + size);
            while (mSatusRecord != STATUS_RECORD_STOP && (left > 0)) {
                synchronized (this) {
                    if (!mPause) {
                        synchronized (mCodecLock) {
                            if ((mCodec != null) && (mInputBuffers != null)) {
                                Log.v(TAG, "feedInputData mInputBuffers != null mCodec != null ");
                                int inputBufferIndex = mCodec.dequeueInputBuffer(1000);
                                if (inputBufferIndex >= 0) {
                                    ByteBuffer buffer = mInputBuffers[inputBufferIndex];
                                    buffer.clear();
                                    if (left < buffer.capacity()) {
                                        len = left;
                                    } else {
                                        len = buffer.capacity();
                                        Log.d(TAG, "data len:" + input.length + ", capacity:" + buffer.capacity()
                                                + ",write len" + len + ", left size:" + left);
                                    }
                                    buffer.put(input, in_offset, len);
                                    mCodec.queueInputBuffer(inputBufferIndex, 0, len, timeUs, 0);
                                    in_offset += len;
                                    left = size - in_offset;
                                }
                            }
                        }
                    }
                }
            }
        }

        private boolean isSurfaceEncoding(MediaFormat format) {
            boolean surface = false;
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("video")) {
                int color = format.getInteger(MediaFormat.KEY_COLOR_FORMAT);
                if (color == MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface) {
                    surface = true;
                }
            }
            return surface;
        }


        private void startEncoder() {
            try {
                mCodec = MediaCodec.createEncoderByType(mFormat.getString(MediaFormat.KEY_MIME));
            } catch (IOException e) {
                Log.d(TAG, "can not create encoder, error:" + e);
            }
            mBufInfo = new MediaCodec.BufferInfo();
            mCodec.configure(mFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);

            if (isSurfaceEncoding(mFormat)) {
                Log.d(TAG, "video encoder");
                mCodecSurface = mCodec.createInputSurface();
                mCodec.start();
                mVirtualDisplay = mDisplayManager.createVirtualDisplay(
                        "Recorder", mWidth, mHeight, DisplayMetrics.DENSITY_TV,
                        mCodecSurface, DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC);
            } else {
                Log.d(TAG, "audio encoder");
                mCodec.start();
            }
            mInputBuffers = mCodec.getInputBuffers();
            mOutputBuffers = mCodec.getOutputBuffers();
        }

        private void dequeueOutputBuffer() {
            if (mCodec != null) {
                boolean needFeed = true;
                int outputBufferIndex = mCodec.dequeueOutputBuffer(mBufInfo, 5000);
                if (outputBufferIndex >= 0) {
                    Log.v(TAG, "outputBufferIndex >= 0");
                    if ((mBufInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG)
                            == MediaCodec.BUFFER_FLAG_CODEC_CONFIG) {
                        if (mCodecSpecific) {
                            needFeed = false;
                        } else {
                            mCodecSpecific = true;
                        }
                    }
                    if (needFeed) {
                        ByteBuffer buf = mOutputBuffers[outputBufferIndex];

                        synchronized (mMuxerLock) {
                            if (mMuxer != null) {
                                //update time
                                mBufInfo.presentationTimeUs = (System.currentTimeMillis() - mStartTime - mPauseTime) * 1000;

                                Log.d(TAG, "presentationTimeUs:" + mBufInfo.presentationTimeUs + ",mPauseTime:" + mPauseTime);

                                //NOTICE: maybe left some data before pause, ignore
                                if (mLastTime < mBufInfo.presentationTimeUs) {
                                    Log.v(TAG, "Muxer writeSampleData");
                                    mMuxer.writeSampleData(mMuxIndex, buf, mBufInfo);
                                }
                                mLastTime = mBufInfo.presentationTimeUs;
                            }
                        }
                    }
                    mCodec.releaseOutputBuffer(outputBufferIndex, false);
                } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                    mOutputBuffers = mCodec.getOutputBuffers();
                } else if (outputBufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    Log.d(TAG, "Codec dequeue buffer timed out.");
                }
            }
        }

        private void stopEncoder() {
            if (mVirtualDisplay != null) {
                mVirtualDisplay.release();
            }
            synchronized (mCodecLock) {
                if (mCodec != null) {
                    if (isSurfaceEncoding(mFormat)) {
                        mCodec.signalEndOfInputStream();
                    }
                    mCodec.stop();
                    mCodec.release();
                    mCodec = null;
                }
            }
        }

        @Override
        public void run() {
            long pauseStartTime = 0;
            startEncoder();
            while (mSatusRecord != STATUS_RECORD_STOP) {
                Log.v(TAG, "EncodingThread run while type: " + mFormat.toString());
                synchronized (this) {
                    if (mPause) {
                        try {
                            pauseStartTime = System.currentTimeMillis();
                            this.wait();
                            mPauseTime += System.currentTimeMillis() - pauseStartTime;
                        } catch (InterruptedException e) {
                            Log.d(TAG, "codec pause error:" + e);
                        }
                    }
                }
                dequeueOutputBuffer();
            }
            stopEncoder();
        }
    }


    public RecordScreenManager(Context context) {
        mDisplayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
    }


    public void start() {
        mSatusRecord = STATUS_RECORD_START;

        try {
            mMuxer = new MediaMuxer(mPath, mOutFormat);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "MediaMuxer error");
            return;
        }


        //video
        //createVideoFormat=1280x720&1920x1080
        //MediaFormat.KEY_BIT_RATE=8000 * 1000&12000 * 1000
        vformat = MediaFormat.createVideoFormat(mVideoMime, mWidth, mHeight);
        vformat.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        vformat.setInteger(MediaFormat.KEY_BIT_RATE, mVideoBitrate);
        vformat.setInteger(MediaFormat.KEY_FRAME_RATE, mFrameRate);
        vformat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, mIdrInterval);


        mAudio = new SourceThread();


        //audio
        aformat = MediaFormat.createAudioFormat(mAudioMime, mAudioSampleRate, mAudioChannel);
        aformat.setInteger(MediaFormat.KEY_BIT_RATE, mAudioBitRate);


        mVidEnc = new EncodingThread(vformat);
        mAudEnc = new EncodingThread(aformat);
        mAudio.setSink(mAudEnc);

        mMuxer.start();


        //thread start
        mVidEnc.start();
        mAudEnc.start();
        mAudio.start();


    }

    public void stop() {
        mSatusRecord = STATUS_RECORD_STOP;

        Log.d(TAG, "stop");
        if (mVidEnc != null) {
            mVidEnc.resumeMe();
            mVidEnc = null;
        }
        if (mAudEnc != null) {
            mAudEnc.resumeMe();
            mAudEnc = null;
        }
        if (mAudio != null) {
            mAudio.resumeMe();
            mAudio = null;
        }
        new Thread() {
            @Override
            public void run() {
                synchronized (mMuxerLock) {
                    if (mMuxer != null) {
                        mMuxer.stop();
                        mMuxer = null;
                    }
                    Log.d(TAG, "thread muxer stop");
                }
            }
        }.start();

    }

    public void pause() {
        mSatusRecord = STATUS_RECORD_PAUSE;

        if (mAudio != null) {
            mAudio.pauseMe();
        }
        if (mAudEnc != null) {
            mAudEnc.pauseMe();
        }
        if (mVidEnc != null) {
            mVidEnc.pauseMe();
        }
    }


    public void resume() {
        mSatusRecord = STATUS_RECORD_START;

        if (mAudio != null) {
            mAudio.resumeMe();
        }
        if (mAudEnc != null) {
            mAudEnc.resumeMe();
        }
        if (mVidEnc != null) {
            mVidEnc.resumeMe();
        }
    }


}
