package com.example.show;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.show.model.MessageEvent;
import com.example.show.views.MarqueeTextRun;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class ShowActivity extends Activity {

	private MarqueeTextRun mWelcomeText;
	private TextView mTimeText;
	private TextView mPhoneTextView;
	private ImageView mShowViewLogo;
	
	private MarqueeTextRun mWelcomeIncText;
	private TextView mPhoneIncText;
	
	private String mMyPhoneNum = "";
	private String mMyTableNum = "";
	private Context mContext;
	private String mWelcomeString = "";
	private String mWelcomePrefix = "";
	private String mWelcomeAppend = "";
	
	private LayoutInflater inflater;
	private View topWindowView;// 第一个topwindow
	private View inComingView;//来电的view
	
    private Timer timer = new Timer();
    private TimerTask task = new TimerTask() {
		
		@Override
		public void run() {
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}
	};
	
    Handler handler = new Handler(){
    	
    	@Override
    	public void handleMessage(Message msg){
    		switch (msg.what) {
			case 1:
				showTime();
				break;

			default:
				break;
			}
    		super.handleMessage(msg);
    	}
    	
    };
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE); //不显示Activity标题  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//保持全屏，不显示系统状态栏 
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕不变黑
        
        setMarquee();
        
        setContentView(R.layout.activity_show_background);

		checkDrawOverlayPermission();

        init();
        showInfo();
    }

	public boolean checkDrawOverlayPermission() {
		/** check if we already  have permission to draw over other apps */
		if (Build.VERSION.SDK_INT >= 23) {
			if (!Settings.canDrawOverlays(this)) {
				/** if not construct intent to request permission */
				Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
						Uri.parse("package:" + this.getPackageName()));
				/** request permission via start activity for result */
				this.startActivityForResult(intent, 2);
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}


	/**
	 * 
	 * @Method: setMarquee
	 * @Description: 各平台对TextView设置 Marquee的源码定制不一样，所以用反射来设置跑马灯是完全的跑马灯，而不是含有省略号的跑马灯。
	 * @Author:David
	 * @Reference:http://www.ithao123.cn/content-7281178.html
	 */
    private void setMarquee(){
		ViewConfiguration configuration =ViewConfiguration.get(this);
		  Class claz =configuration.getClass();
		  try {
		  Field field=claz.getDeclaredField("mFadingMarqueeEnabled");
		  field.setAccessible(true);
		  field.set(configuration, true); 

		} catch (NoSuchFieldException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		} 
		catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
    	if (keyCode == KeyEvent.KEYCODE_HOME) {
//			clearTopWindow(topWindowView);
//			finish();
//			return false;
    		return true;
		}else {
			return super.onKeyDown(keyCode, event);
		}
    	
    }
    
    @Override
    public void onBackPressed() {
		clearTopWindow(topWindowView);
		finish();
    	super.onBackPressed();
    }
    
    private void init(){
    	mContext = getApplicationContext();
    	
    	Resources resources = getResources();
    	
    	mWelcomeString = resources.getString(R.string.welcome_string);
    	mWelcomePrefix = resources.getString(R.string.welcome_string_prefix);
    	mWelcomeAppend = resources.getString(R.string.welcome_string_append);
    	
        inflater = getLayoutInflater().from(ShowActivity.this);
		topWindowView = inflater.inflate(R.layout.top_window_show_view, null);
        
        mWelcomeText = (MarqueeTextRun) topWindowView.findViewById(R.id.welcome_string);
        mTimeText = (TextView) topWindowView.findViewById(R.id.time);
        mPhoneTextView = (TextView) topWindowView.findViewById(R.id.telephone);
        mShowViewLogo = (ImageView) topWindowView.findViewById(R.id.show_view_logo);
        
        //3.硬返回键按下时，也应该退出。
        mShowViewLogo.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				clearTopWindow(topWindowView);
				finish();
				return false;
			}
		});
        
        showTopWindowView(topWindowView);
        
        
        
        inComingView = inflater.inflate(R.layout.activity_incoming, null);
        mWelcomeIncText = (MarqueeTextRun) inComingView.findViewById(R.id.welcome_string_incoming);
        mPhoneIncText = (TextView) inComingView.findViewById(R.id.telephone_incoming);
        
    	
    	
        mMyPhoneNum = Config.getCachedPhoneNumber(mContext);
        mMyTableNum = Config.getCachedTableNumber(mContext);

//    	TelephonyManager telephonyManager =(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
//    	telephonyManager.listen(new PhoneStateListener(){
//
//    		private String incomeNumber = "unknown";
//
//    	    @Override
//    	    public void onCallStateChanged(int state, String incomingNumber){
//    	        Log.e("PhoneCallState", "Incoming number "+incomingNumber); //incomingNumber就是来电号码
//    	        incomeNumber = incomingNumber;
//
//    			switch (state) {
//    			case TelephonyManager.CALL_STATE_IDLE://挂断电话
//    				showTelephone(mMyPhoneNum, true);
////    				Log.e("通话","通话已结束");
//    				break;
//    			case TelephonyManager.CALL_STATE_RINGING://电话响起
//    				showTelephone(incomeNumber, false);
////    				Log.e("通话",incomingNumber+"来电");
//    				break;
//    			case TelephonyManager.CALL_STATE_OFFHOOK://电话接听
//    				showTelephone(incomeNumber, false);
////    				Log.e("通话","接听电话...");
//    				break;
//    			}
//    	    }
//
//    	}, PhoneStateListener.LISTEN_CALL_STATE);
    	
    	timer.schedule(task, 0, 60000);
    	
    }
    
    private void showTime(){
    	SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd  HH:mm");
    	mTimeText.setText(sdf.format(new Date()));
    }
    
    //要注意，imcoming字符串为null的情况。返回的值是什么，不可预测。
    private void showTelephone(String imcomingNumber, Boolean isMyNumber){
    	
    	if (isMyNumber)	{
    		
    		mWelcomeIncText.stopScroll();
    		clearTopWindow(inComingView);
    		
    		mWelcomeText.setText(mMyTableNum);
    		
    		new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					mWelcomeText.startScroll();
				}
			}, 1000);
    		
//    		mWelcomeText.startScroll();
    		
    		//2.要能设置本机电话。
			mPhoneTextView.setText(mMyPhoneNum);
		}else {
			
			mWelcomeText.stopScroll();
			showIncWindowView(inComingView);
			
			mWelcomeIncText.setText(R.string.incoming_string);
			
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
//					mWelcomeIncText.setText(R.string.incoming_string);
					mWelcomeIncText.startScroll();
				}
			}, 1500);
			
//			mWelcomeIncText.setText(R.string.incoming_string);
//			mWelcomeIncText.startScroll();
			//设置为输入的号码。
	    	String maskNumber = mMyPhoneNum;
	    	
	    	int length = imcomingNumber.length();
	    	
	    	if (length > 8) {
				maskNumber = imcomingNumber.substring(0,length - 8)
						+ "****"
						+ imcomingNumber.substring(length - 4, length);
			}
	    	
//	    	mPhoneTextView.setText(maskNumber);
	    	mPhoneIncText.setText(maskNumber);
		}
    }

	@Override
	protected void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		//Log.i("Test", "STOPED");
		//EventBus.getDefault().unregister(this);
	}
    
    private void showInfo(){
    	
    	showTelephone(mMyPhoneNum, true);
    }
    
	/**
	 * 
	 * @Method: showTopWindow
	 * @Description: 显示最顶层view
	 */
	public void showTopWindowView(View view) {
		
		//window管理器
		WindowManager windowManager = (WindowManager) getApplicationContext()
				.getSystemService(WINDOW_SERVICE);


		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
			params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
					| WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY
					| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
					| WindowManager.LayoutParams.FLAG_FULLSCREEN;
		} else {
			params = new WindowManager.LayoutParams(
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
							| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
							| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
							| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
					PixelFormat.TRANSLUCENT);
		}




//		params.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
		//设置全屏显示  可以根据自己需要设置大小
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.height = WindowManager.LayoutParams.MATCH_PARENT;
		
		params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN;
		
//		params.gravity=Gravity.LEFT|Gravity.TOP;   //调整悬浮窗口至左上角
		//设置显示初始位置 屏幕左上角为原点
		params.x = 0;
		params.y = 0;
		
		//view 全屏显示，status bar 弹出来时，view的layout不重排。
		view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		
		view.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				
		    	if (keyCode == KeyEvent.KEYCODE_HOME) {
					clearTopWindow(topWindowView);
					finish();
					return false;
				}
				return false;
			}
		});
		
		// topWindow显示到最顶部
		windowManager.addView(view, params);
	}
    
	/**
	 * 
	 * @Method: clearTopWindow
	 * @Description: 移除最顶层view
	 */
	public void clearTopWindow(View view) {
		if (view != null && view.isShown()) {
			WindowManager windowManager = (WindowManager) getApplicationContext()
					.getSystemService(WINDOW_SERVICE);
			windowManager.removeView(view);
		}
	}
	
	public void showIncWindowView(View view) {
		
		int fullscreenFlag, fullScreenMask, screenOnFlag, screenOnMask, screenPortraitFlag, screenPortraitMask;
		
		fullscreenFlag = fullScreenMask = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		screenOnFlag = screenOnMask = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
//		screenPortraitFlag = screenPortraitMask = WindowManager.LayoutParams.Flag
		
		
		//window管理器
		WindowManager windowManager = (WindowManager) getApplicationContext()
				.getSystemService(WINDOW_SERVICE);
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();


		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
			params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
					| WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY
					| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
					| WindowManager.LayoutParams.FLAG_FULLSCREEN;
		} else {
			params = new WindowManager.LayoutParams(
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
							| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
							| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
							| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
					PixelFormat.TRANSLUCENT);
		}


//		params.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
		//设置全屏显示  可以根据自己需要设置大小
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.height = WindowManager.LayoutParams.MATCH_PARENT;
		
//		attrs.flags = (attrs.flags&~mask) | (flags&mask);
		
//		params.flags = (params.flags & ~fullScreenMask) | (fullscreenFlag & fullScreenMask);
//		params.flags = (params.flags & ~screenOnMask) | (screenOnFlag & screenOnMask);
		
		params.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		
//		params.gravity=Gravity.LEFT|Gravity.TOP;   //调整悬浮窗口至左上角
		//设置显示初始位置 屏幕左上角为原点
		params.x = 0;
		params.y = 0;
		
		//view 全屏显示，status bar 弹出来时，view的layout不重排。
		view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		
		// topWindow显示到最顶部
		windowManager.addView(view, params);
	}
	
    @Override
    protected void onDestroy() {
    	
    	//退出时清空 timer 和 timerTask
    	if (null != timer) {
        	timer.cancel();
        	
        	if (null != timer) {
        		timer = null;
			}
		}
    	
    	if (null != task) {
    		task.cancel();
    		
    		if (null != task) {
				task = null;
			}
			
		}
    	
    	super.onDestroy();
    }


	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onMessageEvent(MessageEvent event) {
		switch (event.state) {
			case 0://挂断电话
				showTelephone(mMyPhoneNum, true);
				Log.i("Test", "CALL_STATE_IDLE");
				break;
			case 1://电话响起
				showTelephone(event.incomingNumber, false);
				Log.i("Test", "CALL_STATE_RINGING");
				break;
		}

	}
}
