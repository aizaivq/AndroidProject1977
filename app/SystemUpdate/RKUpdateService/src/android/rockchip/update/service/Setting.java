package android.rockchip.update.service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

public class Setting extends Activity {
	private static final String TAG = "RKUpdateService.Setting";
	private Context mContext;
	private ImageButton mBtn_CheckNow;
	private SharedPreferences mAutoCheckSet;
	private TextView mTxtProduct;
	private TextView mTxtVersion;
	private final String ACTION_OTA_UPDATE_CLIENT = "ACTION_OTA_UPDATE_CLIENT";
	private final String ACTION_OTA_UPDATE_SERVER = "ACTION_OTA_UPDATE_SERVER";
	private final String PARAM_IS_UPDATE = "PARAM_IS_UPDATE";
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			final String ACTION = intent.getAction();
			final boolean IS_UPDATE = intent.getBooleanExtra(PARAM_IS_UPDATE, false);
			if (ACTION_OTA_UPDATE_SERVER.equals(ACTION)) {
				if (IS_UPDATE) {
					Intent serviceIntent;
					serviceIntent = new Intent("android.rockchip.update.service");
					serviceIntent.putExtra("command", RKUpdateService.COMMAND_CHECK_REMOTE_UPDATING_BY_HAND);
					context.startService(serviceIntent);
				} else {
					Setting.this.finish();
				}
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting);
		mContext = this;
		mBtn_CheckNow = (ImageButton) this.findViewById(R.id.btn_check_now);
		mTxtProduct = (TextView) this.findViewById(R.id.txt_product);
		mTxtVersion = (TextView) this.findViewById(R.id.txt_version);
		mTxtProduct.setText(RKUpdateService.getOtaProductName());
		mTxtVersion.setText(RKUpdateService.getSystemVersion());
		mAutoCheckSet = getSharedPreferences("auto_check", MODE_PRIVATE);
		setContentView(R.layout.activity_null);

//		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction(ACTION_OTA_UPDATE_SERVER);
//		registerReceiver(receiver, intentFilter);
//
//		Intent intent = new Intent();
//		intent.setAction(ACTION_OTA_UPDATE_CLIENT);
//		sendBroadcast(intent);
		Intent serviceIntent;
		serviceIntent = new Intent("android.rockchip.update.service");
		serviceIntent.putExtra("command", RKUpdateService.COMMAND_CHECK_REMOTE_UPDATING_BY_HAND);
		startService(serviceIntent);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//		unregisterReceiver(receiver);
		super.onDestroy();
	}

}
