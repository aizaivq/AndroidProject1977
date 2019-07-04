package android.rockchip.update.service;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.IntentFilter;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import java.util.Formatter;
import java.util.Locale;
import android.rockchip.update.*;
import java.lang.StringBuilder;
import android.os.HandlerThread;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.content.BroadcastReceiver;
import android.os.Message;
import android.util.Log;
import android.content.Intent;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.os.Looper;
import android.app.Dialog;
import android.os.Environment;

public class UpdateAndRebootActivity extends BaseActivity {
    static final String TAG = "UpdateAndRebootActivity";
    private Context mContext;
    private static final boolean DEBUG = true;

    private static void LOG(String msg) {
        if ( DEBUG ) {
            Log.d(TAG, msg);
        }
    }
    
    private String mImageFilePath;
    private WorkHandler mWorkHandler;
    private UiHandler mUiHandler;
    private RKUpdateService.LocalBinder mBinder;
    /*-------------------------------------------------------*/
    
    private ServiceConnection mConnection = new ServiceConnection() { 
        public void onServiceConnected(ComponentName className, IBinder service) { 
        	mBinder = (RKUpdateService.LocalBinder)service;
            mWorkHandler.sendEmptyMessageDelayed(WorkHandler.COMMAND_START_UPDATING , 3000);
        } 

        public void onServiceDisconnected(ComponentName className) { 
        	mBinder = null;
        } 
}; 


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mContext = this;
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.notify_dialog);
		setFinishOnTouchOutside(false);
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
                android.R.drawable.ic_dialog_alert);
        setTitle(getString(R.string.updating_title));
        
        TextView txt = (TextView)this.findViewById(R.id.notify);    
        Button btn_ok = (Button)this.findViewById(R.id.button_ok);
		Button btn_cancel = (Button)this.findViewById(R.id.button_cancel);
        
        Bundle extr = getIntent().getExtras();
        mImageFilePath = extr.getString(RKUpdateService.EXTRA_IMAGE_PATH); 
        
        String msg = getString(R.string.updating_prompt);
        if (mImageFilePath.contains(RKUpdateService.SDCARD_ROOT)) {
            msg += getString(R.string.updating_prompt_sdcard);
        }
        
        txt.setText(msg);
        btn_ok.setVisibility(View.GONE);
        btn_cancel.setVisibility(View.GONE);
        
        LOG("onCreate() : start 'work thread'.");
        HandlerThread workThread = new HandlerThread("UpdateAndRebootActivity : work thread");   
        workThread.start();
       
        mWorkHandler = new WorkHandler(workThread.getLooper() );
        mUiHandler = new UiHandler();      
        mContext.bindService(new Intent(mContext, RKUpdateService.class), mConnection, Context.BIND_AUTO_CREATE); 
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        LOG("onPause() : Entered.");
    }

    private class UiHandler extends Handler {
    	private static final int COMMAND_START_CHECK_FAILD = 1;
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case COMMAND_START_CHECK_FAILD:
				dialog();
				break;
			}
		}
    	
    }
    
    private class WorkHandler extends Handler {
        private static final int COMMAND_START_UPDATING = 1;
        
        public WorkHandler(Looper looper) {
            super(looper);
        }
        
        public void handleMessage(Message msg) {       

            switch (msg.what) {
                case COMMAND_START_UPDATING:
                    LOG("WorkHandler::handleMessage() : To perform 'COMMAND_START_UPDATING'.");

                    if(mBinder != null){
                    	if(mImageFilePath.endsWith("img")){
                    		//rkimge update mode
                    		mBinder.updateFirmware(mImageFilePath, RKUpdateService.RKUPDATE_MODE);
                    	}else{
                    		//ota update mode
                    		if(!(mBinder.doesOtaPackageMatchProduct(mImageFilePath))){
                    			mUiHandler.sendEmptyMessage(UiHandler.COMMAND_START_CHECK_FAILD);
                    		}else{
                    			mBinder.updateFirmware(mImageFilePath, RKUpdateService.OTAUPDATE_MODE);
                    		}
                    	}
                    }else {
                    	Log.d(TAG, "service have not connected!");
                    }
                    break;

                /*---------------*/
                default:
                    break;
            }
        }
    }
    
    protected void dialog() {
    	  AlertDialog.Builder builder = new Builder(mContext);
    	  builder.setMessage("not a valid update package !");
    	  builder.setTitle("error");

    	  builder.setPositiveButton("OK", new OnClickListener() {

    	   public void onClick(DialogInterface dialog, int which) {
    	    dialog.dismiss();

    	    finish();
    	   }
    	  });

    	  AlertDialog dialog = builder.create();
    	  dialog.setCanceledOnTouchOutside(false);
    	  dialog.setCancelable(false);
    	  dialog.show();
    }

}
