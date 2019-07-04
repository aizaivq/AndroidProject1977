package com.via.opencv;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.via.opencv.natives.OpencvManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {
    private static final String PATH_TARGET = Environment.getExternalStorageDirectory().getPath() + "/test.png";
    private ImageView mImageTargetp;
    private Button mButtonTest;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case "ACTION_FOCUS_CHECK":
                    mButtonTest.setText("focus: " + intent.getFloatExtra("focus", 0));
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageTargetp = (ImageView) findViewById(R.id.mImageTarget);
        mButtonTest = (Button) findViewById(R.id.mButtonTest);
        IntentFilter intentFilter = new IntentFilter("ACTION_FOCUS_CHECK");
        registerReceiver(mBroadcastReceiver, intentFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }


    public void test(View view) {
        try {
            InputStream is = getAssets().open("test.png");
            String path = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "test.png").getAbsolutePath();
            FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Bitmap bitmap1 = BitmapFactory.decodeFile(new File(path).getAbsolutePath());
            mImageTargetp.setImageBitmap(bitmap1);
            Intent intent = new Intent();
            intent.setAction("ACTION_FOCUS_CHECK");
            intent.setComponent(new ComponentName("com.via.opencv", "com.via.opencv.OpenCVService"));
            intent.putExtra("path", path);
            startService(intent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
