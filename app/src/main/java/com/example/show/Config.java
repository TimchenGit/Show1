package com.example.show;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class Config {

//	public final static boolean DEBUG = true;
	public final static boolean DEBUG = false;
	
	public static final String APP_ID = "com.example.show";
	public static final String CHARSET = "utf-8";
	
    /* 下载包安装路径 */  
    public static final String savePath = "/sdcard/tugether/";  
    public static final String saveFileName = savePath + "update_apk";  
	
	public static final String TABLE_NUMBER = "table_number";
	public static final String PHONE_NUMBER = "phone_number";
	public static final String WELCOME_STRING = "welcome_string";
	public static final String INCOMING_STRING = "incoming_string";
	
	public static String getCachedTableNumber(Context context){
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(TABLE_NUMBER, "");
	}
	
	public static void cacheTableNumber(Context context,String tableNum){
		Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
		e.putString(TABLE_NUMBER, tableNum);
		e.commit();
	}
	
	public static String getCachedPhoneNumber(Context context){
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(PHONE_NUMBER, "");
	}
	
	public static void cachePhoneNumber(Context context,String phoneNum){
		Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
		e.putString(PHONE_NUMBER, phoneNum);
		e.commit();
	}
	
	public static String getCachedWelcome(Context context){
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(WELCOME_STRING, "");
	}
	
	public static void cacheWelcome(Context context,String welcome){
		Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
		e.putString(WELCOME_STRING, welcome);
		e.commit();
	}
	
	public static String getCachedIncoming(Context context){
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(INCOMING_STRING, "");
	}
	
	public static void cacheIncoming(Context context,String incoming){
		Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
		e.putString(INCOMING_STRING, incoming);
		e.commit();
	}
}