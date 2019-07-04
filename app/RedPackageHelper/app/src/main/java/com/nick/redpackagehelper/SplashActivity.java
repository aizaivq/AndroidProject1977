package com.nick.redpackagehelper;

import android.Manifest;
import android.os.Bundle;

import com.nick.redpackagehelper.base.BaseSplashActivity;


public class SplashActivity extends BaseSplashActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected String[] getPermission() {
        return new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
    }
}
