package com.via.opencv.judge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.via.opencv.R;

public class JudgePictureActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    private int mWidth = 1;
    private int mHeight = 1;
    private int mSplitWidth = 1;

    private JudgeManager mJudgeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judge_picture);

        NumberPicker mNumberPickerAdjustWH = (NumberPicker) findViewById(R.id.mNumberPickerAdjustWH);
        mNumberPickerAdjustWH.setMaxValue(1280);
        mNumberPickerAdjustWH.setValue(1);
        mNumberPickerAdjustWH.setOnValueChangedListener(this);
        mNumberPickerAdjustWH.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        NumberPicker mNumberPickerAdjustSplitWidth = (NumberPicker) findViewById(R.id.mNumberPickerAdjustSplitWidth);
        mNumberPickerAdjustSplitWidth.setMaxValue(1280);
        mNumberPickerAdjustSplitWidth.setValue(1);
        mNumberPickerAdjustSplitWidth.setOnValueChangedListener(this);
        mNumberPickerAdjustSplitWidth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        TextView mTextViewFocusCheck = (TextView)findViewById(R.id.mTextViewFocusCheck);
        ImageView mImageViewPicture = (ImageView) findViewById(R.id.mImageViewPicture);
        mJudgeManager = new JudgeManager(mImageViewPicture,mTextViewFocusCheck);
        mJudgeManager.onCreate();

    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        int id = numberPicker.getId();
        switch (id) {
            case R.id.mNumberPickerAdjustSplitWidth:
                mSplitWidth = i1;

                mJudgeManager.showJudgeBitmap(mWidth, mHeight, mSplitWidth);
                break;
            case R.id.mNumberPickerAdjustWH:

                mWidth = i1;
                mHeight = i1;


                mJudgeManager.showJudgeBitmap(mWidth, mHeight, mSplitWidth);

                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mJudgeManager.onDistroy();
    }
}
