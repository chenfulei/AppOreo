package com.apporeo.push.callback;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.apporeo.push.BKMessage;
import com.apporeo.push.PushConfig;


/**
 * 消息处理
 * @author chen_fulei
 *
 */
public abstract class PushMessageReceiver extends BroadcastReceiver{
	private NotificationManager mNotificationManager;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(mNotificationManager == null){
			mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		
		if(PushConfig.getActionNotificClick(context).equals(intent.getAction())){
			try {
				BKMessage data = (BKMessage) intent.getSerializableExtra("notification_data");
				if(data == null){
					launchApp(context);
					mNotificationManager.cancelAll();
				}else{
					onNotificationClicked(context, data);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(PushConfig.getActionMessageHandler(context).equals(intent.getAction())){
			
			try {
				BKMessage data = (BKMessage) intent.getSerializableExtra("messagehandler_data");
				if(data != null){
					handleMessage(context, data);
				}
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * 数据处理
	 * @param paramContext
	 * @param paramMessage
	 */
	public abstract void handleMessage(Context paramContext, BKMessage paramMessage);
	
	/**
	 * 通知点击处理
	 * @param paramContext
	 * @param paramMessage
	 */
	public abstract void onNotificationClicked(Context paramContext, BKMessage paramMessage);

	
	public void launchApp(Context paramContext) {
		Intent localIntent = paramContext.getPackageManager().getLaunchIntentForPackage(paramContext.getPackageName());
		if (localIntent == null) {
			Log.e("", "handleMessage(): cannot find app: " + paramContext.getPackageName());
			return;
		}
		localIntent.setPackage(null);
		localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		paramContext.startActivity(localIntent);
	} 
	
}
