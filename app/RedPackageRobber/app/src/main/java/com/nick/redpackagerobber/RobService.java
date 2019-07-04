package com.nick.redpackagerobber;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import java.util.List;

/**
 * Created by nick on 19-1-14.
 */

public class RobService extends AccessibilityService {
    private static final String TAG = RobService.class.getSimpleName();
    private boolean mIsRob = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.i(TAG, "onAccessibilityEvent event: " + event.getEventType());

        List<CharSequence> texts = event.getText();
        if (!texts.isEmpty()) {
            for (CharSequence text : texts) {
                String content = text.toString();
                Log.i(TAG, "onAccessibilityEvent content: " + content);
            }

        }

        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                texts = event.getText();
                if (!texts.isEmpty()) {
                    for (CharSequence text : texts) {
                        String content = text.toString();
                        Log.i(TAG, "notification status changed content: " + content);
                        if (content.contains("[微信红包]")) {
                          /*  if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification) {
                                Notification notification = (Notification) event.getParcelableData();
                                PendingIntent pendingIntent = notification.contentIntent;
                                try {
                                    pendingIntent.send();
                                } catch (PendingIntent.CanceledException e) {
                                    e.printStackTrace();
                                }
                            }*/
                        }
                    }
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                if(MainActivity.mSWRob) {
                    AccessibilityNodeInfo rootNode = getRootInActiveWindow();
                    if (rootNode != null) {
                        Log.i(TAG, "des " + rootNode.getContentDescription());
                        //    pList(rootNode);
                        if (mIsRob) {
                            Log.i(TAG, "rob");
                            pList(rootNode);
                        }
                        AccessibilityNodeInfo node = findNodeInfosByText(rootNode, "微信红包");
                        if (node != null) {
                            Log.i(TAG, "-->微信红包:" + node);
                            mIsRob = true;
                            performClick(node);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mIsRob = false;
                                    MainActivity.mSWRob = false;
                                }
                            }, 1000);

                        }
                    } else {
                        List<AccessibilityWindowInfo> nullNode = getWindows();
                        Log.i(TAG, "AccessibilityWindowInfo size (): " + nullNode.size());

                        for (int i = 0; i < nullNode.size(); i++) {
                            Log.i(TAG, "nullNode describeContents(): " + nullNode.get(i).describeContents());
                            Log.i(TAG, "AccessibilityWindowInfo child count: " + nullNode.get(i).getChildCount());

                        }

                    }
                }

                break;
        }
    }

    private int count = 0;

    private void pList(AccessibilityNodeInfo rootNode) {
        if(mIsRob) {
            count++;
            Log.i(TAG, "count: " + count);
            if (rootNode != null) {
                for (int i = 0; i < rootNode.getChildCount(); i++) {
                    if (rootNode.getChild(i) != null) {
                        Log.i(TAG, "pList des " + rootNode.getContentDescription());
                        pList(rootNode.getChild(i));
                        if (rootNode.getChild(i).isClickable()) {
                            Log.i(TAG, "pList click" + i);
                            if (i != 0) {
                                Log.i(TAG, "pList i != 0");
                                rootNode.getChild(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            }

                        }
                    }


                }
            }
        }
    }
/*
    public static void performClickWindow(AccessibilityWindowInfo nodeInfo) {
        if(nodeInfo == null) {
            return;
        }
        if(nodeInfo.isClickable()) {
            Log.i(TAG,"isClickable" );
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            performClick(nodeInfo.getParent());
        }
    }*/

    public static void performClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        if (nodeInfo.isClickable()) {
            Log.i(TAG, "isClickable");
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            performClick(nodeInfo.getParent());
        }
    }

    public static AccessibilityNodeInfo findNodeInfosByText(AccessibilityNodeInfo nodeInfo, String text) {
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(text);
            if (list == null || list.isEmpty()) {
                return null;
            }
            return list.get(0);
        }
        return null;
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.i(TAG, "onServiceConnected");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    public static void start(Context context) {
        context.bindService(new Intent(context, RobService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i(TAG, "start onServiceConnected");

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
        //context.startService(new Intent(context, RobService.class));
    }

    @Override
    public void onInterrupt() {

    }
}
