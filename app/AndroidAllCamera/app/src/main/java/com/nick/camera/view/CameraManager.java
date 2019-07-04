package com.nick.camera.view;

import android.hardware.Camera;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.List;

/**
 * Created by nick on 18-6-21.
 */

public class CameraManager {
    private static final String TAG = CameraManager.class.getSimpleName();
    private static int mCountFps;
    private static long mCountFpsTime;
    private static long mLastFpsTime;


    public static void setPreviewSizeSpinner(Spinner spinner, Camera camera, AdapterView.OnItemSelectedListener onItemSelectedListener) {
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> listPreviewSize = parameters.getSupportedPreviewSizes();
        String[] arrayPreviewSize = new String[listPreviewSize.size()];
        for (int i = 0; i < listPreviewSize.size(); i++) {
            arrayPreviewSize[i] = listPreviewSize.get(i).width + "x" + listPreviewSize.get(i).height;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(spinner.getContext(), android.R.layout.simple_spinner_item, arrayPreviewSize);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(onItemSelectedListener);
    }


    public static void setPreviewSize(Camera camera, int index) {
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> listPictureSize = parameters.getSupportedPictureSizes();

        for(Camera.Size item:listPictureSize)
        {
            Log.d(TAG,"picture size: " + item.width + "x" + item.height);
        }


        List<Camera.Size> listPreviewSize = parameters.getSupportedPreviewSizes();
        int w = listPreviewSize.get(index).width;
        int h = listPreviewSize.get(index).height;
        parameters.setPreviewSize(w, h);
        camera.setParameters(parameters);
        camera.stopPreview();
        camera.startPreview();

    }

    public static void setAntibanding(Camera camera, int index) {
        Camera.Parameters parameters = camera.getParameters();
        List<String> listAntibanding = parameters.getSupportedAntibanding();
        parameters.setAntibanding(listAntibanding.get(index));
        camera.setParameters(parameters);
        camera.stopPreview();
        camera.startPreview();
    }


    public static void setAntibandingSpinner(Spinner spinner, Camera camera, AdapterView.OnItemSelectedListener onItemSelectedListener) {
        Camera.Parameters parameters = camera.getParameters();
        List<String> listAntibanding = parameters.getSupportedAntibanding();
        String[] arrayAntibanding = (String[]) listAntibanding.toArray(new String[listAntibanding.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(spinner.getContext(), android.R.layout.simple_spinner_item, arrayAntibanding);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(onItemSelectedListener);

    }

    public static void setWhiteBalanceSpinner(Spinner spinner, Camera camera, AdapterView.OnItemSelectedListener onItemSelectedListener) {
        Camera.Parameters parameters = camera.getParameters();
        List<String> listWhiteBalance = parameters.getSupportedWhiteBalance();
        //  parameters.setExposureCompensation();
        parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_INCANDESCENT);
        String[] arrayWhiteBalance = (String[]) listWhiteBalance.toArray(new String[listWhiteBalance.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(spinner.getContext(), android.R.layout.simple_spinner_item, arrayWhiteBalance);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(onItemSelectedListener);
    }

    public static void setWhiteBalance(Camera camera, int index) {

        Camera.Parameters parameters = camera.getParameters();
        List<String> listWhiteBalance = parameters.getSupportedWhiteBalance();
        parameters.setWhiteBalance(listWhiteBalance.get(index));
        camera.setParameters(parameters);
        camera.stopPreview();
        camera.startPreview();

    }

    public static void setSceneModesSpinner(Spinner spinner, Camera camera, AdapterView.OnItemSelectedListener onItemSelectedListener) {
        Camera.Parameters parameters = camera.getParameters();
        List<String> listSceneModes = parameters.getSupportedSceneModes();
        String[] arraySceneModes = (String[]) listSceneModes.toArray(new String[listSceneModes.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(spinner.getContext(), android.R.layout.simple_spinner_item, arraySceneModes);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(onItemSelectedListener);
    }

    public static void setColorEffectsSpinner(Spinner spinner, Camera camera, AdapterView.OnItemSelectedListener onItemSelectedListener) {
        Camera.Parameters parameters = camera.getParameters();
        List<String> listColorEffects = parameters.getSupportedColorEffects();
        String[] arrayColorEffects = (String[]) listColorEffects.toArray(new String[listColorEffects.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(spinner.getContext(), android.R.layout.simple_spinner_item, arrayColorEffects);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(onItemSelectedListener);
    }

    public static void setColorEffects(Camera camera, int index) {
        Camera.Parameters parameters = camera.getParameters();
        List<String> listColorEffects = parameters.getSupportedColorEffects();
        parameters.setColorEffect(listColorEffects.get(index));
        camera.setParameters(parameters);
        camera.stopPreview();
        camera.startPreview();

    }


    public static void setFlashModesSpinner(Spinner spinner, Camera camera, AdapterView.OnItemSelectedListener onItemSelectedListener) {
        Camera.Parameters parameters = camera.getParameters();
        List<String> listFlashModes = parameters.getSupportedFlashModes();
        String[] arrayFlashModes = (String[]) listFlashModes.toArray(new String[listFlashModes.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(spinner.getContext(), android.R.layout.simple_spinner_item, arrayFlashModes);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(onItemSelectedListener);
    }


    public static void setZoomSpinner(Spinner spinner, Camera camera, AdapterView.OnItemSelectedListener onItemSelectedListener) {
        Camera.Parameters parameters = camera.getParameters();
        List<Integer> listZoom = parameters.getZoomRatios();
        String[] arrayZoom = new String[listZoom.size()];
        for (int i = 0; i < listZoom.size(); i++) {
            arrayZoom[i] = "" + listZoom.get(i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(spinner.getContext(), android.R.layout.simple_spinner_item, arrayZoom);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(onItemSelectedListener);
    }

    public static void setZoom(Camera camera, int index) {
        Camera.Parameters parameters = camera.getParameters();
        camera.startSmoothZoom(index);
        camera.setParameters(parameters);
    }


    public static void setFlashMode(Camera camera, int index) {
        Camera.Parameters parameters = camera.getParameters();
        List<String> listColorEffect = parameters.getSupportedColorEffects();
        parameters.setColorEffect(listColorEffect.get(index));
        camera.setParameters(parameters);
        camera.stopPreview();
        camera.startPreview();
    }


    public static void setSceneModes(Camera camera, int index) {
        Camera.Parameters parameters = camera.getParameters();
        List<String> listSceneModes = parameters.getSupportedSceneModes();
        parameters.setSceneMode(listSceneModes.get(index));
        camera.setParameters(parameters);
        camera.stopPreview();
        camera.startPreview();

    }


    public static int countFps() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (mLastFpsTime == 0) {
            mLastFpsTime = currentTime;
            return 0;
        }
        mCountFpsTime = mCountFpsTime + currentTime - mLastFpsTime;
        Log.i(TAG, "count time: " + mCountFpsTime);
        mLastFpsTime = currentTime;
        if(mCountFpsTime >= 1000)
        {
            Log.i(TAG, "fps: " + mCountFps);
            int ret = mCountFps;
            mCountFps = 0;
            mLastFpsTime = 0;
            mCountFpsTime = 0;
            return ret;
        }
        mCountFps++;

        return 0;


    }


}
