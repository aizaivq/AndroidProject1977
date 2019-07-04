package com.via.recordscreen;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private String mPathTarget = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "recordsample.mp4").getPath();


    private Button mBTStart;
    private Button mBTStop;
    private Button mBTPause;
    private Button mBTResume;
    private Button mBTPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBTStart = findViewById(R.id.mBTStart);
        mBTStop = findViewById(R.id.mBTStop);
        mBTPause = findViewById(R.id.mBTPause);
        mBTResume = findViewById(R.id.mBTResume);
        mBTPlay = findViewById(R.id.mBTPlay);

        mBTStart.setOnClickListener(this);
        mBTStop.setOnClickListener(this);
        mBTPause.setOnClickListener(this);
        mBTResume.setOnClickListener(this);
        mBTPlay.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mBTStart:
                File file = new File(mPathTarget);
                if (file.exists()) {
                    file.delete();
                }
                if (!Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).exists()) {
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).mkdirs();
                }
                RecordScreenService.startRecordScreen(this, mPathTarget);

                mBTStop.setVisibility(View.VISIBLE);
                mBTStart.setVisibility(View.INVISIBLE);
                mBTPause.setVisibility(View.VISIBLE);
                mBTResume.setVisibility(View.INVISIBLE);
                mBTPlay.setVisibility(View.INVISIBLE);
                break;
            case R.id.mBTStop:
                RecordScreenService.stopRecordScreen(this);
                mBTStart.setVisibility(View.VISIBLE);
                mBTPause.setVisibility(View.INVISIBLE);
                mBTPlay.setVisibility(View.VISIBLE);
                mBTStop.setVisibility(View.INVISIBLE);
                mBTResume.setVisibility(View.INVISIBLE);
                break;
            case R.id.mBTPause:
                RecordScreenService.pauseRecordScreen(this);
                mBTResume.setVisibility(View.VISIBLE);
                mBTPause.setVisibility(View.INVISIBLE);
                mBTStart.setVisibility(View.INVISIBLE);
                mBTStop.setVisibility(View.VISIBLE);
                mBTPlay.setVisibility(View.INVISIBLE);
                break;
            case R.id.mBTResume:
                RecordScreenService.resumeRecordScreen(this);
                mBTResume.setVisibility(View.INVISIBLE);
                mBTPause.setVisibility(View.VISIBLE);
                mBTStart.setVisibility(View.INVISIBLE);
                mBTStop.setVisibility(View.VISIBLE);
                mBTPlay.setVisibility(View.INVISIBLE);
                break;
            case R.id.mBTPlay:
                Utils.startSystemPlayer(this, mPathTarget);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RecordScreenService.stopRecordScreen(this);


    }
}
