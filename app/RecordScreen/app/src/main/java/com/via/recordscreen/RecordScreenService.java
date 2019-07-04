package com.via.recordscreen;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by nick on 18-10-31.
 */

public class RecordScreenService extends Service {

    private RecordScreenManager mRecordScreenManager;
    private static final String ACTION_RECORD_SCREEN_START = "ACTION_RECORD_SCREEN_START";
    private static final String ACTION_RECORD_SCREEN_STOP = "ACTION_RECORD_SCREEN_STOP";

    private static final String ACTION_RECORD_SCREEN_PAUSE = "ACTION_RECORD_SCREEN_PAUSE";
    private static final String ACTION_RECORD_SCREEN_RESUME = "ACTION_RECORD_SCREEN_RESUME";


    private static final String KEY_PATH = "KEY_PATH";

    @Override
    public void onCreate() {
        super.onCreate();
        mRecordScreenManager = new RecordScreenManager(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getAction()) {
            case ACTION_RECORD_SCREEN_START:
                mRecordScreenManager.stop();
                String path = intent.getStringExtra(KEY_PATH);
                mRecordScreenManager.setmPath(path);
                mRecordScreenManager.start();
                break;
            case ACTION_RECORD_SCREEN_STOP:
                mRecordScreenManager.stop();
                break;
            case ACTION_RECORD_SCREEN_PAUSE:
                mRecordScreenManager.pause();
                break;
            case ACTION_RECORD_SCREEN_RESUME:
                mRecordScreenManager.resume();
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void startRecordScreen(Context context, String path) {
        Intent intent = new Intent(ACTION_RECORD_SCREEN_START);
        intent.putExtra(KEY_PATH, path);
        intent.setComponent(new ComponentName("com.via.recordscreen", "com.via.recordscreen.RecordScreenService"));
        context.startService(intent);
    }

    public static void stopRecordScreen(Context context) {
        Intent intent = new Intent(ACTION_RECORD_SCREEN_STOP);
        intent.setComponent(new ComponentName("com.via.recordscreen", "com.via.recordscreen.RecordScreenService"));
        context.startService(intent);
    }

    public static void pauseRecordScreen(Context context) {
        Intent intent = new Intent(ACTION_RECORD_SCREEN_PAUSE);
        intent.setComponent(new ComponentName("com.via.recordscreen", "com.via.recordscreen.RecordScreenService"));
        context.startService(intent);


    }

    public static void resumeRecordScreen(Context context) {
        Intent intent = new Intent(ACTION_RECORD_SCREEN_RESUME);
        intent.setComponent(new ComponentName("com.via.recordscreen", "com.via.recordscreen.RecordScreenService"));
        context.startService(intent);

    }


}
