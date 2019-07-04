package com.nick.baseapp;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.nick.baseapp.base.camera.BaseCameraActivity;
import com.nick.baseapp.base.BaseSplashActivity;

public class SplashActivity extends BaseSplashActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected String[] getPermission() {
        return new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
    }

    @Override
    protected void skip() {
        //startActivity(new Intent(SplashActivity.this, MainActivity.class));
        startActivity(new Intent(SplashActivity.this, BaseCameraActivity.class));
        
    }
}
