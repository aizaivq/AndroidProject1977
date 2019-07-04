package com.via.cameragl.camera;

import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nick on 18-1-16.
 */

public class CameraDecorate {
    private static boolean mStartFps = true;
    private static long mFpsStart;
    private static int mCountFps;
    private static final String TAG = CameraDecorate.class.getSimpleName();



    public static int countFps() {
        if (mStartFps) {
            mStartFps = false;
            mCountFps = 0;
            mFpsStart = new Date().getTime();
        } else if (new Date().getTime() - mFpsStart >= 1000) {
            mStartFps = true;
            Log.d(TAG, "fps: " + mCountFps);
            return mCountFps;
        }
        mCountFps++;
        return -1;
    }


    public static void setParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        List<Camera.Size> listSize = parameters.getSupportedPreviewSizes();
        List<int[]> listFps = parameters.getSupportedPreviewFpsRange();
        if (listFps != null && listFps.size() > 0) {
            //   parameters.setPreviewFpsRange(listFps.get(0)[0], listFps.get(0)[1]);
        }
        if (listSize != null && listSize.size() > 0) {
            //       parameters.setPreviewSize(listSize.get(0).width, listSize.get(0).height);
        }
        parameters.setPreviewSize(1920, 1080);

        camera.setParameters(parameters);
    }




}
