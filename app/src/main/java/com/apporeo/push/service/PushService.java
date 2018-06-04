package com.apporeo.push.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.apporeo.push.PushConfig;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 推送（定时主动请求）
 * 
 * @author chen_fulei
 *
 */
public class PushService extends Service {
	private final int FLAG_MESSAGE_INTENT = 0x0001;

	private PushReceiver pushReceiver;

	@Override
	public void onCreate() {
		super.onCreate();

		pushReceiver = new PushReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.apporeo.intent.action.COMMAND");
		registerReceiver(pushReceiver , intentFilter);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			Log.e("PushService---> " , "onStartCommand");

			long timeout = PushConfig.TIMEOUT;
			
//			try {
//				if(intent != null && PushConfig.getActionChangeTime(getApplicationContext()).equals(intent.getAction())){
//					timeout = PushConfig.getAlarmTimeout(getApplicationContext(), PushConfig.TIMEOUT);
//					if(timeout == 0){
//						timeout = PushConfig.TIMEOUT;
//						PushConfig.setAlarmTimeout(getApplicationContext(), timeout);
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}

			startTimer(timeout);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Service.START_STICKY_COMPATIBILITY;
	}
	
	

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		//关闭服务进程
//		System.exit(0);

		unregisterReceiver(pushReceiver);
		destroyTimer();
		super.onDestroy();
	}

	private Timer timer;
	private TimerTask timerTask;

	private void startTimer(long delay){
		destroyTimer();

		timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				sendBroadcast(new Intent("com.apporeo.intent.action.COMMAND"));
			}
		};

		timer.schedule(timerTask , 0 ,delay);
	}

	private void destroyTimer(){
		if (timer != null){
			timer.cancel();
			timer = null;
		}

		if (timerTask != null){
			timerTask.cancel();
			timerTask = null;
		}
	}
}
