package com.apporeo.push.callback;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.apporeo.push.PushConfig;

public class DataChangeReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		if(PushConfig.getActionChangeData(context).equals(intent.getAction())){
			try {
				String deviceid = intent.getStringExtra("app_devices_id");
				if(!TextUtils.isEmpty(deviceid)){
					PushConfig.setAppDevices(context, deviceid);
				}
				
				String devicetoken = intent.getStringExtra("app_devices_token");
				if(!TextUtils.isEmpty(devicetoken)){
					PushConfig.setAppToken(context, devicetoken);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
