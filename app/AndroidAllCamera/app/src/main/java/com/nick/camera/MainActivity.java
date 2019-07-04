package com.nick.camera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nick.camera.view.CameraManager;
import com.nick.camera.view.CameraView;
import com.nick.camera.view.ICamera;
import com.nick.camera.view.ICameraView;

public class MainActivity extends AppCompatActivity {
    private Spinner mSpinnerPreviewSize;
    private Spinner mSpinnerAntibanding;
    private Spinner mSpinnerWhiteBalance;
    private Spinner mSpinnerSceneMode;
    private Spinner mSpinnerZoom;
    private Spinner mSpinnerColorEffect;
    private Spinner mSpinnerFlashMode;

    private TextView mTextViewFps;


    private ICamera mICamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSpinnerPreviewSize = (Spinner) findViewById(R.id.mSpinnerPreviewSize);
        mSpinnerAntibanding = (Spinner) findViewById(R.id.mSpinnerAntibanding);
        mSpinnerWhiteBalance = (Spinner) findViewById(R.id.mSpinnerWhiteBalance);
        mSpinnerSceneMode = (Spinner) findViewById(R.id.mSpinnerSceneMode);
        mSpinnerZoom = (Spinner) findViewById(R.id.mSpinnerZoom);
        mSpinnerColorEffect = (Spinner) findViewById(R.id.mSpinnerColorEffect);
        mSpinnerFlashMode = (Spinner) findViewById(R.id.mSpinnerFlashMode);
        mTextViewFps = (TextView) findViewById(R.id.mTextViewFps);
        mICamera = (ICamera) findViewById(R.id.mCameraView);
        mICamera.setICameraView(new ICameraView() {
            @Override
            public void onCreate() {
                CameraManager.setPreviewSizeSpinner(mSpinnerPreviewSize, mICamera.getCamera(), new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        CameraManager.setPreviewSize(mICamera.getCamera(), i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                CameraManager.setAntibandingSpinner(mSpinnerAntibanding, mICamera.getCamera(), new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        CameraManager.setAntibanding(mICamera.getCamera(), i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                CameraManager.setWhiteBalanceSpinner(mSpinnerWhiteBalance, mICamera.getCamera(), new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        CameraManager.setWhiteBalance(mICamera.getCamera(),i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                CameraManager.setSceneModesSpinner(mSpinnerSceneMode, mICamera.getCamera(), new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        CameraManager.setSceneModes(mICamera.getCamera(),i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                CameraManager.setFlashModesSpinner(mSpinnerFlashMode, mICamera.getCamera(), new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        CameraManager.setFlashMode(mICamera.getCamera(),i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                CameraManager.setColorEffectsSpinner(mSpinnerColorEffect, mICamera.getCamera(), new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        CameraManager.setColorEffects(mICamera.getCamera(),i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                CameraManager.setZoomSpinner(mSpinnerZoom, mICamera.getCamera(), new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        CameraManager.setZoom(mICamera.getCamera(),i);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


            }
            @Override
            public void onPreview() {
                int fps = CameraManager.countFps();
                if(fps > 0) {
                    mTextViewFps.setText("" + fps);
                }
            }
        });

    }
}
