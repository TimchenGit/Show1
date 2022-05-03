package com.example.show;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

	private EditText mEtTableNum;
	private EditText mEtPhoneNum;
	private TextView mTvMoreSet;
	private Button mBtnLogin;

	private String mTableNumString;
	private String mPhoneNumString;

	private Context mContext;

	private TelephonyManager telephonyManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE); //不显示Activity标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//保持全屏，不显示系统状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕不变黑

		setContentView(R.layout.activity_main);
		checkAndRequestPermission();
		init();
	}

	private boolean checkAndRequestPermission() {
		int readPhoneState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
		int read_call_log = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG);
		//int read_phone_numbers = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_NUMBERS);
		List listPermissionsNeeded = new ArrayList<>();
		if (readPhoneState != PackageManager.PERMISSION_GRANTED) {
			listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
		}

		if (read_call_log != PackageManager.PERMISSION_GRANTED) {
			listPermissionsNeeded.add(Manifest.permission.READ_CALL_LOG);
		}

//		if(read_phone_numbers != PackageManager.PERMISSION_GRANTED){
//			listPermissionsNeeded.add(Manifest.permission.READ_PHONE_NUMBERS);
//		}

		if (!listPermissionsNeeded.isEmpty()) {
			ActivityCompat.requestPermissions(this,
					(String[]) listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
					1);
			return false;
		}
		return true;
	}

	/**
	 * 获取电话号码
	 */
	private String getPhoneNum() {
		String nativePhoneNumber = "";
		telephonyManager = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		if ( ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED) {

			checkAndRequestPermission();

			return "";
		}

		nativePhoneNumber = telephonyManager.getLine1Number();
        
        return nativePhoneNumber;
	}
	
	private void init(){
		mContext = getApplicationContext();
		
		String phoneNum = getPhoneNum();
		
		mTableNumString = Config.getCachedTableNumber(mContext);
		
		if (null != phoneNum && !phoneNum.equals("")) {
			
			char firstChar = phoneNum.charAt(0);
			String firstString = String.valueOf(firstChar);
			
			if (firstString.equals("+") || firstString.equals("6")) {
				mPhoneNumString = phoneNum;
			}else {
				mPhoneNumString = "+63" + phoneNum;
			}
			
		}else {
			mPhoneNumString = Config.getCachedPhoneNumber(mContext);
		}
		
		mEtTableNum = (EditText) findViewById(R.id.table_number);
		mEtPhoneNum = (EditText) findViewById(R.id.phone_number);
		mTvMoreSet = (TextView) findViewById(R.id.more_set);
		mBtnLogin = (Button) findViewById(R.id.login);
		
		if (null != mTableNumString && mTableNumString.equals("")) {
			mEtTableNum.setText(R.string.welcome_string);
		}else if (null != mTableNumString && !mTableNumString.equals("")) {
			mEtTableNum.setText(mTableNumString);
		}
		
		if (null != mPhoneNumString && !mPhoneNumString.equals("")) {
			mEtPhoneNum.setText(mPhoneNumString);
		}
		
		mTvMoreSet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mBtnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (null != mTableNumString) {
					Config.cacheTableNumber(mContext, mEtTableNum.getText().toString());
				}
				
				if (null != mPhoneNumString) {
					Config.cachePhoneNumber(mContext, mEtPhoneNum.getText().toString());
				}
				
				Intent intent = new Intent(MainActivity.this, ShowActivity.class);
				startActivity(intent);
				
			}
		});
		
		
	}
    
}
