package com.via.opencv;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CameraActivity extends AppCompatActivity {
    public static boolean mIsTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        CameraActivity.mIsTest = true;
    }

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.via.opencv", "com.via.opencv.CameraActivity");
        context.startActivity(intent);
    }

    public static void test(View view)
    {
        mIsTest = !mIsTest;
    }
}
