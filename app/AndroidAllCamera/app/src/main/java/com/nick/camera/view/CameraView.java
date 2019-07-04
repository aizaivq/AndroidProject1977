package com.nick.camera.view;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.AbsoluteLayout;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.List;

/**
 * Created by nick on 18-6-19.
 */

public class CameraView extends SurfaceView implements
        SurfaceHolder.Callback, Camera.PreviewCallback {
    private static SurfaceHolder holder;
    private Camera mCamera;
    private static final  String TAG = CameraView.class.getSimpleName();
    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        mCamera = Camera.open(0);
        try {
            Camera.Parameters parameters = mCamera.getParameters();

            List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();
            for(Camera.Size item:previewSizeList)
            {
                Log.i(TAG,"item height: " + item.height + "  width: " + item.width);
            }
            parameters.setPreviewSize(1920,1080);
        //    parameters.setPreviewSize(160,120);
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mCamera.setParameters(parameters);
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(holder);
            mCamera.setPreviewCallback(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        if (mCamera != null)
            mCamera.startPreview();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Log.i(TAG,"onPreviewFrame");
    }

    public void takePickture(Camera.ShutterCallback shutterCallback, Camera.PictureCallback pictureCallback)
    {
        mCamera.takePicture(shutterCallback,null,pictureCallback);
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }
}
