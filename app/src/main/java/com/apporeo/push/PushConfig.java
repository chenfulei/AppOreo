package com.apporeo.push;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 基本配置
 * @author chen_fulei
 *
 */
public class PushConfig {
	public static long TIMEOUT = 5 *1000; // 3分钟一次
	public static String BASE_URL = "http://pms.baby-kingdom.com/";
	
	public static String APP_KEY = "HKBK_APPKEY";
	public static String MESSAGE_SECRET = "HKBK_MESSAGE_SECRET";
	
	public static String SHARE_APP_KEY = "app_key";
	public static String SHARE_MESSAGE_SECRET_KEY = "message_secret";
	public static String SHARE_APP_DEVICES = "app_devices";
	public static String SHARE_APP_TOKEN = "app_token";
	public static String SHARE_ALARM_TIMEOUT = "alarm_timeout";
	
	public static String APP_DEVICES_ID = "";
	public static String APP_DEVICES_TOKEN = "";
	
	/**
	 * 请求新数据的处理
	 * @param mContext
	 * @return
	 */
	public static String getReceiverActionCommand(Context mContext){
		return mContext.getPackageName()+".intent.action.COMMAND";
	}
	/**
	 * 通知点击的广播
	 * @param mContext
	 * @return
	 */
	public static String getActionNotificClick(Context mContext){
		return mContext.getPackageName()+".intent.action.notification.CLICK";
	}
	
	/**
	 * 改变闹钟的时间
	 * @param mContext
	 * @return
	 */
	public static String getActionChangeTime(Context mContext){
		return mContext.getPackageName()+".intent.action.change.TIMEOUT";
	}
	/**
	 * 改变数据
	 * @param mContext
	 * @return
	 */
	public static String getActionChangeData(Context mContext){
		return mContext.getPackageName()+".intent.action.change.MSG_DATA";
	}
	
	/**
	 * 接收消息
	 * @param mContext
	 * @return
	 */
	public static String getActionMessageHandler(Context mContext){
		return mContext.getPackageName()+".intent.action.message.HANDLER";
	}
	
	public static SharedPreferences getSharePreference(Context paramContext){
		return paramContext.getSharedPreferences("bkpush_AppStore", 0);
	}
	
	public static String getPreferenceToString(Context paramContext, String key , String defValue){
		SharedPreferences sp = getSharePreference(paramContext);
		
		return sp.getString(key, defValue);
	}
	
	public static void setPreferenceToString(Context paramContext , String key ,String value){
		SharedPreferences sp = getSharePreference(paramContext);
		sp.edit().putString(key, value).commit();
	}
	
	public static long getPreferenceToLong(Context paramContext, String key , long defValue){
		SharedPreferences sp = getSharePreference(paramContext);
		
		return sp.getLong(key, defValue);
	}
	
	public static void setPreferenceToLong(Context paramContext , String key ,long value){
		SharedPreferences sp = getSharePreference(paramContext);
		sp.edit().putLong(key, value).commit();
	}
	
	/**
	 * 配置里的app key
	 * @param paramContext
	 * @return
	 */
	public static String getAppKey(Context paramContext){
		return getPreferenceToString(paramContext, SHARE_APP_KEY, "");
	}
	public static void setAppKey(Context paramContext , String value){
		setPreferenceToString(paramContext, SHARE_APP_KEY, value);
	}
	
	/**
	 * 配置里的secret key
	 * @param paramContext
	 * @return
	 */
	public static String getMessageSecret(Context paramContext){
		return getPreferenceToString(paramContext, SHARE_MESSAGE_SECRET_KEY, "");
	}
	public static void setMessageSecret(Context paramContext , String value){
		setPreferenceToString(paramContext, SHARE_MESSAGE_SECRET_KEY, value);
	}
	
	/**
	 * 服务器返回设备id
	 * @param paramContext
	 * @return
	 */
	public static String getAppDevices(Context paramContext){
		return getPreferenceToString(paramContext, SHARE_APP_DEVICES, "");
	}
	public static void setAppDevices(Context paramContext ,String value){
		setPreferenceToString(paramContext, SHARE_APP_DEVICES, value);
	}
	
	/**
	 * 服务器返回token
	 * @param paramContext
	 * @return
	 */
	public static String getAppToken(Context paramContext){
		return getPreferenceToString(paramContext, SHARE_APP_TOKEN, "");
	}
	public static void setAppToken(Context paramContext ,String value){
		setPreferenceToString(paramContext, SHARE_APP_TOKEN, value);
	}
	
	/**
	 * 轮询时间
	 * @param paramContext
	 * @param value
	 */
	public static void setAlarmTimeout(Context paramContext ,long value){
		setPreferenceToLong(paramContext, SHARE_ALARM_TIMEOUT, value);
	}
	public static long getAlarmTimeout(Context paramContext ,long defValue){
		return getPreferenceToLong(paramContext, SHARE_ALARM_TIMEOUT, defValue);
	}
}
