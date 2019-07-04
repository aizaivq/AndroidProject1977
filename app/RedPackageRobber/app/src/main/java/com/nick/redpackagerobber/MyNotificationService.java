package com.nick.redpackagerobber;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by nick on 19-1-14.
 */

public class MyNotificationService extends NotificationListenerService {
    private static final String TAG = "RPR_RobberService";
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Log.i(TAG,"onNotificationPosted");
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Log.i(TAG,"onNotificationRemoved");
    }
}
