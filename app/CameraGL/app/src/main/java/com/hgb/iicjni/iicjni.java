package com.hgb.iicjni;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;


/**
 * Created by nick on 17-10-26.
 */

public class iicjni {
    static {
        System.loadLibrary("iicjni");
    }

    public static final String TAG = iicjni.class.getSimpleName();
    private Handler mHandler;
    private HandlerThread mHandlerThread;


    private static final int WHAT_INCREASE_ZOOM = 0;
    private static final int WHAT_DECREASE_ZOOM = 1;

    private static final int DELAY_START_FOCUS = 100;


    private static final int WHAT_START_FOCUS = 2;


    private static iicjni mIicjni = new iicjni();
    private static final int[] ZOOM = {700, 690, 680, 660, 640, 620, 600, 560, 520, 480, 440, 400, 360, 320, 300};
    private int[] mArrayFocus = new int[]{125, 123, 121, 116, 111, 105, 100, 90, 78, 67, 56, 44, 32, 18, 12};


    private int mIndex;
    private int mFd;

    private iicjni() {

    }

    public static iicjni getInstance() {
        return mIicjni;
    }

    public void open() {
        mFd = mIicjni.open(0, 20);
        mHandlerThread = new HandlerThread(iicjni.class.getSimpleName());
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case WHAT_DECREASE_ZOOM:
                        if (mIndex < ZOOM.length - 1) {
                            mIndex++;
                        }
                        setzoom(mFd, ZOOM[mIndex]);
                        sendEmptyMessageDelayed(WHAT_START_FOCUS, DELAY_START_FOCUS);
                        break;
                    case WHAT_INCREASE_ZOOM:
                        if (mIndex > 0) {
                            mIndex--;
                        }
                        setzoom(mFd, ZOOM[mIndex]);
                        sendEmptyMessageDelayed(WHAT_START_FOCUS, DELAY_START_FOCUS);
                        break;
                    case WHAT_START_FOCUS:
                        startf(mFd, mArrayFocus[mIndex]);
                        break;
                }
            }
        };
        //mHandler.sendEmptyMessage(WHAT_DECREASE_ZOOM);

    }

    public void close() {
        mIicjni.close(mFd);
        mHandlerThread.quit();
    }

    public void increaseZoom() {
        if (mIndex < ZOOM.length - 1) {
            mIndex++;
        }
        setzoom(mFd, ZOOM[mIndex]);
       // mHandler.sendEmptyMessage(WHAT_INCREASE_ZOOM);
    }

    public void decreaseZoom() {
        if (mIndex > 0) {
            mIndex--;
        }
        setzoom(mFd, ZOOM[mIndex]);
      //  mHandler.sendEmptyMessage(WHAT_DECREASE_ZOOM);

    }


    private native int test(int i);

    private native int open(int id, int addr);

    private native int close(int fileHander);

    private native int setzoom(int fileHander, int zoom);

    private native int setfocus(int fileHander, int f);

    private native int getfocus(int fileHander, int f);

    private native int setdat(int d1, int d2, int d3, int d4, int d5, int d6);


    private native int startf(int fileHander, int focusrange);

    private native int startf(int fileHander);

    private native int settable(int[] data, int len);

    private native int dofft(byte[] data, int fileHander, int w, int h);

}
