package com.example.show.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.show.model.MessageEvent;

import org.greenrobot.eventbus.EventBus;

public class MyPhoneBroadcastReceiver extends BroadcastReceiver{

	
	@Override
	public void onReceive(final Context context, Intent intent) {

	    Bundle extras = intent.getExtras();

//	    abortBroadcast();
	    
	    if (extras != null) {
	        String state = extras.getString(TelephonyManager.EXTRA_STATE);
	       String incomingNumber = extras.getString("incoming_number");

	        Handler callActionHandler = new Handler();

	        Runnable runRingingActivity = new Runnable() {

	            @Override
	            public void run() {
	                Intent intentPhoneCall = new Intent("android.intent.action.ANSWER");
	                //intentPhoneCall.putExtra("INCOMING_NUM", incomingNumber);

	                intentPhoneCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                context.startActivity(intentPhoneCall);
	            }
	        };

			//Log.i("Test", state+"Blah");
	        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
	        	if (incomingNumber != null) {
					EventBus.getDefault().post(new MessageEvent(1, incomingNumber));
				}
	            callActionHandler.postDelayed(runRingingActivity, 800);

	        }

	        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
				EventBus.getDefault().post(new MessageEvent(0,incomingNumber));
	            callActionHandler.removeCallbacks(runRingingActivity);
	            // setResultCode(Activity.RESULT_CANCELED);
	        }

	    }
	}
}
