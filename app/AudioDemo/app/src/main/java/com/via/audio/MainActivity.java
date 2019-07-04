package com.via.audio;

import android.media.audiofx.AcousticEchoCanceler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    //   private static final String PATH_RECORD = "/storage/sdcard0/record.pcm";
    private static final String PATH_RECORD = "/sdcard/record.pcm";
    private StringBuilder mMsg = new StringBuilder();
    private MyAudio mAudio = new MyAudio();
    private TextView mTextViewMsg;
    private Switch mSwitchEchoCanceler;

    private int mSessionId;
    private boolean mEchoCanceler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((ToggleButton) findViewById(R.id.mToggleButtonRecord)).setOnCheckedChangeListener(this);
        ((ToggleButton) findViewById(R.id.mToggleButtonPlay)).setOnCheckedChangeListener(this);
        mTextViewMsg = ((TextView) findViewById(R.id.mTextViewMsg));
        mTextViewMsg.setText("AcousticEchoCanceler: " + AcousticEchoCanceler.isAvailable());
        mSwitchEchoCanceler = (Switch) findViewById(R.id.mSwitchEchoCanceler);
    }

    private void setMsg() {
        mMsg.setLength(0);
        mMsg.append("AcousticEchoCanceler: " + AcousticEchoCanceler.isAvailable()).append('\n');
        mMsg.append("session id: " + mSessionId).append('\n');
        mMsg.append("echocanceler: " + mEchoCanceler);

        mTextViewMsg.setText(mMsg);

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();
        switch (id) {
            case R.id.mToggleButtonPlay:
                play(b);
                break;
            case R.id.mToggleButtonRecord:
                record(b);

                break;
        }
    }

    private void record(final boolean start) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAudio.record(start, PATH_RECORD, new OnRecordListener() {
                    @Override
                    public void onSessionLoad(final int session) {
                        mSessionId = session;
                    }

                    @Override
                    public void onEchoCancelerLoad(final boolean enable) {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mEchoCanceler = enable;
                                setMsg();
                            }
                        });
                    }
                }, mSwitchEchoCanceler.isChecked());

            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);

                    mAudio.play(start, PATH_RECORD);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void play(final boolean start) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAudio.play(start, PATH_RECORD);

            }
        }).start();
    }
}
