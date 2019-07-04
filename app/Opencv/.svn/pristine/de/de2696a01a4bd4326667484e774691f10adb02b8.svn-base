package com.via.opencv;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class BitmapActivity extends AppCompatActivity {
    public static Bitmap mBitmap;
    public static byte[] mOriginDatas;
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);
        ImageView mImageView = (ImageView) findViewById(R.id.mImageView);
        mImageView.setImageBitmap(mBitmap);
        for(int i = 0;i < WIDTH * HEIGHT;i++)
        {
            Log.i("opencv","data: " + mOriginDatas[i]);
        }


    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, BitmapActivity.class));
    }
}
