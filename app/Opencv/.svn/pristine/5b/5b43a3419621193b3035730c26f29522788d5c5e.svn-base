package com.via.opencv.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.via.opencv.BitmapActivity;
import com.via.opencv.CameraActivity;
import com.via.opencv.natives.OpencvManager;
import com.via.opencv.util.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by nick on 17-10-26.
 */

public class CameraView extends SurfaceView implements
        SurfaceHolder.Callback {
    private static SurfaceHolder holder;
    private Camera mCamera;

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        if (mCamera == null) {
            mCamera = Camera.open(0);//开启相机，可以放参数 0 或 1，分别代表前置、后置摄像头，默认为 0
            try {
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setPreviewSize(BitmapActivity.WIDTH,BitmapActivity.HEIGHT);
                parameters.setRotation(90);
                mCamera.setDisplayOrientation(90);
                mCamera.setParameters(parameters);
                mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] bytes, Camera camera) {
                  //      Log.i("opencv", "onPreviewFrame" + camera.getParameters().getPreviewSize().height);
                  //      Log.i("opencv", "onPreviewFrame" + camera.getParameters().getPreviewSize().width);

                        if (CameraActivity.mIsTest) {

                            Log.i("opencv", "size:   " + bytes.length);
//                            for (byte item : bytes) {
//                                Log.i("opencv", "data: " + item);
//
//                            }
                            CameraActivity.mIsTest = !CameraActivity.mIsTest;
                            YuvImage yuvImage = new YuvImage(bytes, ImageFormat.NV21, 640,480,null);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            yuvImage.compressToJpeg(new Rect(0,0,BitmapActivity.WIDTH,BitmapActivity.HEIGHT),100,stream);
                            BitmapActivity.mBitmap = BitmapFactory.decodeByteArray(stream.toByteArray(),0,stream.size());
                            BitmapActivity.mOriginDatas = bytes;
                            BitmapActivity.start(getContext());
                     //       FileUtil.write(BitmapFactory.decodeByteArray(stream.toByteArray(),0,stream.size()),"/sdcard/opencv.jpeg");
                         //   Log.i("opencv", "opencv test result: " +OpencvManager.getFocusCheck(bytes));
                        }
                    }
                });
                mCamera.setPreviewDisplay(holder);//整个程序的核心，相机预览的内容放在 holder
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        mCamera.startPreview();//该方法只有相机开启后才能调用
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();//释放相机资源
            mCamera = null;
        }
    }
}