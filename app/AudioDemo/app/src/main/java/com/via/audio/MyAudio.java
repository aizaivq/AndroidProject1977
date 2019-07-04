package com.via.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by nick on 18-1-30.
 */

public class MyAudio {
    private AudioRecord mAudioRecord;
    private AudioTrack mAudioTrack;
    private boolean mIsRecording;
    private static final String TAG = MyAudio.class.getSimpleName();

    public void record(boolean start, String path, OnRecordListener onRecordListener, boolean echoCanceler) {
        mIsRecording = start;
        if (start) {
            int frequency = 11025;
            int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
            int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
            File file = new File(path);
            if (file.exists())
                file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new IllegalStateException("Failed to create " + file.toString());
            }
            try {
                OutputStream os = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(os);
                DataOutputStream dos = new DataOutputStream(bos);
                int mBufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
                mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                        frequency, channelConfiguration,
                        audioEncoding, mBufferSize);
                onRecordListener.onSessionLoad(mAudioRecord.getAudioSessionId());
                if (echoCanceler) {
                    AcousticEchoCanceler acousticEchoCanceler = AcousticEchoCanceler.create(mAudioRecord.getAudioSessionId());
                    onRecordListener.onEchoCancelerLoad(acousticEchoCanceler.getEnabled());
                    acousticEchoCanceler.setEnabled(true);

                }
                short[] buffer = new short[mBufferSize];
                mAudioRecord.startRecording();
                while (mIsRecording) {
                    int bufferReadResult = mAudioRecord.read(buffer, 0, mBufferSize);
                    Log.v(TAG, "MyAudio recording bufferReadResult: " + bufferReadResult);
                    for (int i = 0; i < bufferReadResult; i++) {
                        Log.v(TAG,"write record data: " + buffer[i]);
                        dos.writeShort(buffer[i]);
                    }

                }
                mAudioRecord.stop();
                dos.close();
            } catch (Throwable t) {
                t.printStackTrace();
            }

        }
    }

    public void play(boolean start, String path) {
        if (start) {
            File file = new File(path);
            int musicLength = (int) (file.length() / 2);
            short[] music = new short[musicLength];
            try {
                InputStream is = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(is);
                DataInputStream dis = new DataInputStream(bis);
                int i = 0;
                while (dis.available() > 0) {
                    music[i] = dis.readShort();
                    i++;
                }
                dis.close();
                mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                        11025,
                        AudioFormat.CHANNEL_CONFIGURATION_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        musicLength * 2,
                        AudioTrack.MODE_STREAM);
                mAudioTrack.play();
                mAudioTrack.write(music, 0, musicLength);
                //      mAudioTrack.stop();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        } else {
            mAudioTrack.stop();
        }

    }

    public void recordPlay() {


    }


}
