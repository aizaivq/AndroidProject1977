package com.nick.baseapp.base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nick.baseapp.MainActivity;
import com.nick.baseapp.R;

public abstract class BaseSplashActivity extends AppCompatActivity {
    private Handler mHandlerSkip = new Handler();
    private Runnable mRunnableSkip = new Runnable() {
        @Override
        public void run() {
            BaseSplashActivity.this.finish();
            skip();
        }
    };
    private static final int DELAY_SKIP = 2000;
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_splash);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        getPermission(),
                        REQUEST_CODE);
            } else {
                mHandlerSkip.postDelayed(mRunnableSkip, DELAY_SKIP);
            }
        } else {
            mHandlerSkip.postDelayed(mRunnableSkip, DELAY_SKIP);
        }

    }

    protected abstract String[] getPermission();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandlerSkip.removeCallbacks(mRunnableSkip);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            boolean mIsGetAllPermission = true;
            for (int i = 0; i < permissions.length; i++) {
                if (!permissions[i].equals(getPermission()[i]) || grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    mIsGetAllPermission = false;
                }
            }
            if (mIsGetAllPermission) {
                mHandlerSkip.postDelayed(mRunnableSkip, DELAY_SKIP);
            } else {
                finish();
            }
        }
    }

    protected abstract void skip();
}
