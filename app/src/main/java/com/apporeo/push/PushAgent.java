package com.apporeo.push;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.apporeo.push.callback.PushRegisterCallback;
import com.apporeo.push.service.PushService;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 推送入口
 * 
 * @author chen_fulei
 *
 */
public class PushAgent {
	private static PushAgent pushAgent;
	private Context mContext;
	
	private PushRegisterCallback registerCallback;
	private RequestCall mAsyncLoad;
	private PushAgent(Context parentcxt){
		this.mContext = parentcxt;
	}
	
	public static synchronized PushAgent getInstance(Context paramContext) {
		if(pushAgent == null){
			pushAgent = new PushAgent(paramContext);
		}
		
		return pushAgent;
	}

	/**
	 * 开启
	 */
	public PushAgent enable() {
		disAsyncLoad();
		loadToken(DeviceConfig.getAppkey(mContext), DeviceConfig.getMessageSecret(mContext));
		
		//启动服务
		try {
			Intent intent = new Intent(PushConfig.getActionChangeTime(mContext));
			intent.setPackage(mContext.getPackageName());
			mContext.startService(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 关闭
	 */
	public PushAgent disable() {
		try {
			Intent intent = new Intent(PushConfig.getActionChangeTime(mContext));
			intent.setPackage(mContext.getPackageName());
			mContext.stopService(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public boolean isEnable(){
		if(DeviceConfig.isServiceRunning(mContext, PushService.class.getName())){
			return true;
		}
		return false;
	}
	
	/**
	 * 加载token
	 * @param appkey
	 * @param msgSecret
	 */
	private void loadToken(String appkey , String msgSecret){
		String mac = DeviceConfig.getMediaMac(mContext);
		String imei = DeviceConfig.getDeviceId(mContext);
		
		if(TextUtils.isEmpty(appkey) || TextUtils.isEmpty(msgSecret)) return ;
		
		String url = PushConfig.BASE_URL +"token";
		Map<String, String> params = new HashMap<>();
		params.put("appkey", appkey);
		params.put("appsecret", msgSecret);
		params.put("imei", imei);
		params.put("mac", mac);

		mAsyncLoad = OkHttpUtils
				.get()
				.url(url)
				.params(params)
				.build();
		mAsyncLoad.execute(loadListener);
	}
	
	/**
	 * 获取设备id
	 * @return
	 */
	public String getRegistrationId(){
		return PushConfig.getAppDevices(mContext);
	}
	/**
	 * 注销线程请求
	 */
	private void disAsyncLoad(){
		if(mAsyncLoad != null){
			mAsyncLoad.cancel();
			mAsyncLoad = null;
		}
	}

	private Callback<String> loadListener = new Callback<String>() {
		@Override
		public String parseNetworkResponse(Response response, int id) throws Exception {
			return null;
		}

		@Override
		public void onError(Call call, Exception e, int id) {

		}

		@Override
		public void onResponse(String response, int id) {
			try {
				if(!TextUtils.isEmpty(response)){
					TokenBean tb = new TokenBean(response);
					if(tb != null && "1".equals(tb.getStatus())){
						PushConfig.APP_DEVICES_ID = tb.getDeviceid();
						PushConfig.APP_DEVICES_TOKEN = tb.getToken();

						PushConfig.setAppDevices(mContext, tb.getDeviceid());
						PushConfig.setAppToken(mContext, tb.getToken());

						Intent intent = new Intent(PushConfig.getActionChangeData(mContext));
						intent.putExtra("app_devices_id", tb.getDeviceid());
						intent.putExtra("app_devices_token", tb.getToken());
						mContext.sendBroadcast(intent);
					}

					Log.e("", PushConfig.getAppDevices(mContext)+" <--->" + PushConfig.getAppToken(mContext));

					if(registerCallback != null){
						registerCallback.onRegistered(getRegistrationId());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

}
