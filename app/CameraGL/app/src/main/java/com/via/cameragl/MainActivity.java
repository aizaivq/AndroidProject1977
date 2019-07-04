package com.via.cameragl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.hgb.iicjni.iicjni;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_F1:    //+
                iicjni.getInstance().increaseZoom();
                break;
            case KeyEvent.KEYCODE_F2:   //-
                iicjni.getInstance().decreaseZoom();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}
