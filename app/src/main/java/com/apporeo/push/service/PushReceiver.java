package com.apporeo.push.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.apporeo.push.BKMessageList;
import com.apporeo.push.DeviceConfig;
import com.apporeo.push.PushConfig;
import com.apporeo.push.TokenBean;
import com.apporeo.push.callback.BKLocalNotification;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 定时去请求数据
 * @author chen_fulei
 *
 */
public class PushReceiver extends BroadcastReceiver{

	private Context mContext;
	private RequestCall asynLoad;
	private RequestCall mAsyncLoad;

	private final int LOAD_ID_ONE = 0x001;
	private final int LOAD_ID_TWO = 0x002;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.mContext = context;
		Log.e("PushReceiver---> " , "onReceive");
		disLoad();
		if(PushConfig.getReceiverActionCommand(context).equals(intent.getAction()) && check()){
			loadMsg();
		}
	}
	
	/**
	 * 加载数据
	 */
	private void loadMsg(){
		String url = PushConfig.BASE_URL+"check";
		Map<String, String> params = new HashMap<String, String>();
		params.put("deviceid", PushConfig.APP_DEVICES_ID);
		params.put("token", PushConfig.APP_DEVICES_TOKEN);
		asynLoad = OkHttpUtils
				.get()
				.url(url)
				.params(params)
				.id(LOAD_ID_ONE)
				.build();
		asynLoad.execute(loadListener);
	}
	
	/**
	 * 取消请求数据
	 */
	private void disLoad(){
		try {
			if(asynLoad != null){
				asynLoad.cancel();
				asynLoad = null;
			}
			
			if(mAsyncLoad != null){
				mAsyncLoad.cancel();
				mAsyncLoad = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean check(){
		if(TextUtils.isEmpty(PushConfig.APP_DEVICES_ID) || TextUtils.isEmpty(PushConfig.APP_DEVICES_TOKEN)){
			String d = PushConfig.getAppDevices(mContext);
			String t = PushConfig.getAppToken(mContext);
			if(TextUtils.isEmpty(d)|| TextUtils.isEmpty(t)){
				loadToken(DeviceConfig.getAppkey(mContext), DeviceConfig.getMessageSecret(mContext));
				
				return false;
			}

			PushConfig.APP_DEVICES_ID = d;
			PushConfig.APP_DEVICES_TOKEN = t;
		}
		
		return true;
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
		Map<String, String> params = new HashMap<String, String>();
		params.put("appkey", appkey);
		params.put("appsecret", msgSecret);
		params.put("imei", imei);
		params.put("mac", mac);
		mAsyncLoad = OkHttpUtils
				.get()
				.url(url)
				.params(params)
				.id(LOAD_ID_TWO)
				.build();
		mAsyncLoad.execute(loadListener);
	}


	private StringCallback loadListener = new StringCallback() {

		/**
		 * 判断是否时间改变
		 * @param nextTime
		 * @return
		 */
		public boolean isChangeTime(String nextTime){
			try {
				long old = PushConfig.getAlarmTimeout(mContext, PushConfig.TIMEOUT);
				long next = Long.parseLong(nextTime) * 1000;
				if(next != 0 && old != next){
					PushConfig.setAlarmTimeout(mContext, next);
					return true;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		public void onError(Call call, Exception e, int id) {

		}

		@Override
		public void onResponse(String response, int id) {
			if (id == LOAD_ID_ONE){
				try {
					if(!TextUtils.isEmpty(response)){
						Log.e("PushReceiver-->" ,"response -> "+ response);
						BKMessageList bkmsg = new BKMessageList(response);
//						try {
//							if(isChangeTime(bkmsg.getNext_time())){
//								Intent intent = new Intent(PushConfig.getActionChangeTime(mContext));
//								intent.setPackage(mContext.getPackageName());
//								mContext.startService(intent);
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						}

						if(bkmsg != null && "1".equals(bkmsg.getStatus())){
							BKLocalNotification.getInstance().show(mContext, bkmsg);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}else if (id == LOAD_ID_TWO){
				try {
					if(!TextUtils.isEmpty(response)){
						Log.e("PushReceiver2-->" ,"response2 -> "+ response);
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
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};
}
