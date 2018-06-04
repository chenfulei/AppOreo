package com.apporeo.push;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.UUID;

/**
 * 设备信息配置
 * 
 * @author chen_fulei
 *
 */
public class DeviceConfig {

	/**
	 * 检测服务是否正在运行
	 * @param paramContext
	 * @param service_Name
	 * @return
	 */
	public static boolean isServiceRunning(Context paramContext, String service_Name) {
		ActivityManager manager = (ActivityManager) paramContext.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (service_Name.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取 message secret
	 * 
	 * @param paramContext
	 * @return
	 */
	public static String getMessageSecret(Context paramContext) {
		return getMetaData(paramContext, PushConfig.MESSAGE_SECRET);
	}

	/**
	 * 获取appkey
	 * 
	 * @param paramContext
	 * @return
	 */
	public static String getAppkey(Context paramContext) {

		return getMetaData(paramContext, PushConfig.APP_KEY);
	}

	/**
	 * 获取AndroidManifest.xml 中meta-data
	 * 
	 * @param paramContext
	 * @param paramString
	 * @return
	 */
	public static String getMetaData(Context paramContext, String paramString) {
		try {
			PackageManager localPackageManager = paramContext.getPackageManager();
			ApplicationInfo localApplicationInfo = localPackageManager.getApplicationInfo(paramContext.getPackageName(),
					PackageManager.GET_META_DATA);

			if (localApplicationInfo != null) {
				String str = localApplicationInfo.metaData.getString(paramString);
				if (str != null)
					return str.trim();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.e("", String.format("Could not read meta-data %s from AndroidManifest.xml.", new Object[] { paramString }));
		return null;
	}

	/**
	 * 获取当前设备的唯一序号
	 * 
	 * @param mContext
	 * @return
	 */
	public static String getDeviceId(Context mContext) {
		String deviceID = "";
		try {
			TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
			deviceID = tm.getDeviceId();
		} catch (Exception e) {
		}

		if (TextUtils.isEmpty(deviceID)) {
			try {
				deviceID = "" + android.provider.Settings.Secure.getString(mContext.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			} catch (Exception e) {
			}
		}
		return deviceID;
	}

	/**
	 * 机器的mac地址
	 * 
	 * @param mContext
	 * @return
	 */
	public static String getMediaMac(Context mContext) {
		try {
			WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
			if (wifiManager.isWifiEnabled()) {
				WifiInfo wifi = wifiManager.getConnectionInfo();
				return wifi.getMacAddress();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 获取UUID
	 * 
	 * @param mContext
	 * @return
	 */
	public static String getUUID(Context mContext) {
		TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		String androidId = "" + android.provider.Settings.Secure.getString(mContext.getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);
		String tmDevice = "" + tm.getDeviceId();
		String tmMac = "" + getMediaMac(mContext);

		UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmMac.hashCode());
		return deviceUuid.toString();
	}

}
