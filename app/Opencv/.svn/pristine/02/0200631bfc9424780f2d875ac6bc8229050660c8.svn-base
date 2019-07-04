package com.via.opencv;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.via.opencv.natives.OpencvManager;

import java.io.File;

/**
 * Created by nick on 17-11-30.
 */

public class OpenCVService extends Service {
    private static final String ACTION_FOCUS_CHECK = "ACTION_FOCUS_CHECK";
    private static final String ACTION_FOCUS_CHECK_DATA = "ACTION_FOCUS_CHECK_DATA";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //am startservice -n com.via.opencv/.OpenCVService -a ACTION_FOCUS_CHECK --es "/storage/sdcard0/0.png"
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        Log.i("service", "service action: " + action);
        switch (action) {
            case ACTION_FOCUS_CHECK:
                String path = intent.getStringExtra("path");
                float focus = OpencvManager.focusCheck(path);
                Intent resultIntent = new Intent(ACTION_FOCUS_CHECK);
                resultIntent.putExtra("focus", focus);
                resultIntent.putExtra("path", path);
                Log.i("service", "service focusCheck: " + focus);
                sendBroadcast(resultIntent);
                break;
        }
        return START_NOT_STICKY;
    }
}
