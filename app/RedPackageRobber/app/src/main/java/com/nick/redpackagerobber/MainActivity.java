package com.nick.redpackagerobber;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{
    private NotificationManager mNotificationManager;
    private Switch mSW;
    private static final String TAG = MainActivity.class.getSimpleName();

    public static boolean mSWRob = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        findViewById(R.id.mBTNotifications).setOnClickListener(this);
        mSW = findViewById(R.id.mSW);
        mSW.setOnCheckedChangeListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSW.setChecked(mSWRob);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mBTNotifications:
                RobService.start(this);

                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);

/*
                StatusBarNotification[] statusBarNotifications = mNotificationManager.getActiveNotifications();
                for (int i = 0; i < statusBarNotifications.length; i++) {
                    Log.d(TAG, "statusBarNotifications: " + statusBarNotifications[i].toString());
                }*/
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mSWRob = isChecked;
    }

    public boolean isNotificationListenerEnabled(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(this);
        if (packageNames.contains(context.getPackageName())) {
            return true;
        }
        return false;
    }

    public void openNotificationListenSettings() {
        try {
            Intent intent;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            } else {
                intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
