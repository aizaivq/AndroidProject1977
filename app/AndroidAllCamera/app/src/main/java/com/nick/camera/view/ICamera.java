package com.nick.camera.view;

import android.hardware.Camera;

/**
 * Created by nick on 18-6-21.
 */

public interface ICamera {
    Camera getCamera();
    void setICameraView(ICameraView iCameraView);
}
