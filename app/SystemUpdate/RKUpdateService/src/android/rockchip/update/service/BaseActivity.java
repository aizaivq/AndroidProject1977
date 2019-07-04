package android.rockchip.update.service;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

public class BaseActivity extends Activity {

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		   if(keyCode == event.KEYCODE_BACK)
		  {
			  return true;
		  }
        return super.onKeyDown(keyCode, event);
	}

}
