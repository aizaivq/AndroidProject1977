package com.via.cameragl.camera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import com.hgb.iicjni.iicjni;

import java.io.IOException;
import java.util.Calendar;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by nick on 18-1-16.
 */

public class CameraView extends GLSurfaceView implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {
    private SurfaceTexture mSurfaceTexture;
    private DirectDrawer mDirectDrawer;
    private Camera mCamera;
    private static final String TAG = CameraView.class.getSimpleName();

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(RENDERMODE_CONTINUOUSLY);


    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        this.requestRender();
        Log.d(TAG, "onFrameAvailable time: " + Calendar.getInstance().getTimeInMillis());
        CameraDecorate.countFps();


    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        int id = createTextureID();
        iicjni.getInstance().open();
        mSurfaceTexture = new SurfaceTexture(id);
        mSurfaceTexture.setOnFrameAvailableListener(this);
        mDirectDrawer = new DirectDrawer(id);
        if (mCamera == null) {
            mCamera = Camera.open();
            CameraDecorate.setParameters(mCamera);
            try {
                mCamera.setPreviewTexture(mSurfaceTexture);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        mCamera.startPreview();
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        Log.i(TAG, "via camera preview onDrawFrame start time: " + Calendar.getInstance().getTimeInMillis());
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mSurfaceTexture.updateTexImage();
        float[] mtx = new float[16];
        mSurfaceTexture.getTransformMatrix(mtx);
        mDirectDrawer.draw(mtx);
        Log.i(TAG, "via camera preview onDrawFrame end time: " + Calendar.getInstance().getTimeInMillis());

    /*    gl10.glReadPixels(0, 0, WIDTH_RECT, HEIGHT_RECT, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, byteBuffer);
        byte[] data = byteBuffer.array();
        if(CameraPreview.iic != null && CameraPreview.isOptical())
        {
            CameraPreview.iic.dofftrgb32(data,CameraPreview.fn,WIDTH_RECT,HEIGHT_RECT);
        }
*/
    }

    private int createTextureID() {
        int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        return texture[0];
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        if (mCamera != null) {
            iicjni.getInstance().close();
            mCamera.release();//释放相机资源
            mCamera = null;
        }
    }
}
