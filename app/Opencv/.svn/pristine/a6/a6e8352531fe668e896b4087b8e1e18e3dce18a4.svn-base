package com.via.opencv.judge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.via.opencv.natives.OpencvManager;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by nick on 18-3-13.
 */

public class JudgeManager {
    private Handler mHandlerJudge;
    private HandlerThread mHandlerTheadJudge;
    private static final int MSG_CREATE_BITMAP = 0;
    private ImageView mImageViewPicture;
    private static final String KEY_WIDTH = "width";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_SPLIT_WIDTH = "splitWidth";
    private static final String TAG = JudgeManager.class.getSimpleName();
    private TextView mTextViewFocusCheck;

    private static final String PATH_JUDGE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "judge.jpeg";

    public JudgeManager(ImageView imageView, TextView mTextViewFocusCheck) {
        mImageViewPicture = imageView;
        this.mTextViewFocusCheck = mTextViewFocusCheck;
    }


    public void onCreate() {
        mHandlerTheadJudge = new HandlerThread(JudgeManager.class.getSimpleName());
        mHandlerTheadJudge.start();
        mHandlerJudge = new Handler(mHandlerTheadJudge.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // Log.d(TAG, "judge manager handle msg: " + msg.what);

                switch (msg.what) {
                    case MSG_CREATE_BITMAP:

                        Context context = mImageViewPicture.getContext();
                        int w = msg.getData().getInt(KEY_WIDTH);
                        int h = msg.getData().getInt(KEY_HEIGHT);
                        int splitWidth = msg.getData().getInt(KEY_SPLIT_WIDTH);


                        //       Log.d(TAG, "judge manager w: " + w);
                        //       Log.d(TAG, "judge manager h: " + h);
                        //       Log.d(TAG, "judge manager splitWidth: " + splitWidth);


                        final Bitmap bitmap = JudgeUtil.createJudgeBitmap(w, h, splitWidth);
                        try (FileOutputStream outputStream = new FileOutputStream(PATH_JUDGE)) {
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        final float focusCheck = OpencvManager.focusCheck(PATH_JUDGE);
                        new Handler(context.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                //                Log.d(TAG, "judge manager set bitmap");
                                mTextViewFocusCheck.setText("focus check: " + focusCheck);
                                mImageViewPicture.setImageBitmap(bitmap);
                            }
                        });
                        break;
                }
            }
        };
    }

    public void showJudgeBitmap(int w, int h, int splitWidth) {
        Message message = Message.obtain();
        message.what = MSG_CREATE_BITMAP;
        message.getData().putInt(KEY_HEIGHT, h);
        message.getData().putInt(KEY_WIDTH, w);
        message.getData().putInt(KEY_SPLIT_WIDTH, splitWidth);

        mHandlerJudge.sendMessage(message);


    }


    public void onDistroy() {
        mHandlerTheadJudge.quit();
    }


}
