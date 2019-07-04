package com.nick.camera.view;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by nick on 18-6-19.
 */

public class Camera2View  extends TextureView implements TextureView.SurfaceTextureListener, ImageReader.OnImageAvailableListener {
    private Camera mCamera;
    private CameraDevice mCameraDevice;
    private ImageReader mImageReader;
    private CameraCaptureSession mCameraCaptureSession;
    private CaptureRequest.Builder mBuilder;

    public Camera2View(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSurfaceTextureListener(this);
        mImageReader = ImageReader.newInstance(1280, 720, ImageFormat.YV12, 2);
        mImageReader.setOnImageAvailableListener(this, new Handler());
    }

    @Override
    public void onSurfaceTextureAvailable(final SurfaceTexture surfaceTexture, int i, int i1) {
        CameraManager manager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
        //检查权限
        try {
            manager.openCamera("0", new CameraDevice.StateCallback() {

                @Override
                public void onOpened(@NonNull CameraDevice cameraDevice) {
                    try {
                        mCameraDevice = cameraDevice;
                        mBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                        getSurfaceTexture().setDefaultBufferSize(1920,1080);
                     //   getSurfaceTexture().setDefaultBufferSize(160,120);

                        // 创建作为预览的CaptureRequest.Builder
                        mBuilder.addTarget(mImageReader.getSurface());
                        mBuilder.addTarget(new Surface(getSurfaceTexture()));
                        mCameraDevice.createCaptureSession(Arrays.asList(new Surface(getSurfaceTexture()),mImageReader.getSurface()), new CameraCaptureSession.StateCallback() {

                            @Override
                            public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                                try {
                                    //创建捕获请求
                                    CaptureRequest captureRequest = mBuilder.build();
                                    mCameraCaptureSession = cameraCaptureSession;
                                    //设置反复捕获数据的请求，这样预览界面就会一直有数据显示
                                    mCameraCaptureSession.setRepeatingRequest(captureRequest, new CameraCaptureSession.CaptureCallback() {
                                        @Override
                                        public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
                                            super.onCaptureStarted(session, request, timestamp, frameNumber);
                                        }

                                    }, null);

                                } catch (CameraAccessException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

                            }
                        }, new Handler());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice cameraDevice) {

                }

                @Override
                public void onError(@NonNull CameraDevice cameraDevice, int i) {

                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    @Override
    public void onImageAvailable(ImageReader imageReader) {
        Image image = null;
        try {
            image = imageReader.acquireLatestImage();
            if (image == null) {
                return;
            }
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            image.close();
        } finally {
            if (image != null) {
                image.close();
            }

        }
    }
}