package com.example.show;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class IncomingActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE); //不显示Activity标题  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//保持全屏，不显示系统状态栏  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕不变黑
        
        setContentView(R.layout.activity_show_background);
        init();
    }
    private void init(){

//    	TelephonyManager telephonyManager =(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
//    	telephonyManager.listen(new PhoneStateListener(){
//
//    	    @Override
//    	    public void onCallStateChanged(int state, String incomingNumber){
//
//    			switch (state) {
//    			case TelephonyManager.CALL_STATE_IDLE://挂断电话
//    				finish();
////    				Log.e("通话","通话已结束");
//    				break;
//    			case TelephonyManager.CALL_STATE_RINGING://电话响起
////    				Log.e("通话",incomingNumber+"来电");
//    				break;
//    			case TelephonyManager.CALL_STATE_OFFHOOK://电话接听
////    				Log.e("通话","接听电话...");
//    				break;
//    			}
//    	    }
//
//    	}, PhoneStateListener.LISTEN_CALL_STATE);
    	
    }
}
