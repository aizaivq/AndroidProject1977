package com.via.audio;

/**
 * Created by nick on 18-2-5.
 */

public interface OnRecordListener {
    void onSessionLoad(int session);
    void onEchoCancelerLoad(boolean enable);

}
