package com.apporeo.push.callback;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;


import com.apporeo.R;
import com.apporeo.push.BKMessage;
import com.apporeo.push.BKMessageList;
import com.apporeo.push.PushConfig;

import java.lang.ref.WeakReference;

/**
 * 通知
 * @author chen_fulei
 *
 */
public class BKLocalNotification {
	private static BKLocalNotification bknotification = null;
	private NotificationManager mNotificationManager;
	
	public static synchronized BKLocalNotification getInstance(){
		if(bknotification == null){
			bknotification = new BKLocalNotification();
		}
		return bknotification;
	}
	
	public void show(Context mContext , BKMessageList bkMessage){
		if(mNotificationManager == null){
			mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		Bitmap largeIcon = null;
		
		int largeIconID = getResourceID(mContext, "push_notification_default_large_icon");
		int smallIconID = getResourceID(mContext, "push_notification_default_small_icon");
		try {
			if(largeIconID <=0){
				largeIconID = R.mipmap.ic_launcher;
			}
			if(smallIconID <=0){
				smallIconID = R.mipmap.ic_launcher;
			}
			
			largeIcon= BitmapFactory.decodeResource(mContext.getResources(), largeIconID);
			WeakReference<Bitmap> weakBitmap = new WeakReference<Bitmap>(largeIcon);
			if(bkMessage != null && bkMessage.getData() != null){
				for(BKMessage data : bkMessage.getData()){
					//生成通知之前广播消息出来
					Intent msgIntent = new Intent(PushConfig.getActionMessageHandler(mContext));
					msgIntent.putExtra("messagehandler_data", data);
					mContext.sendBroadcast(msgIntent);
					
					
					int flag = (int) System.currentTimeMillis();
					data.setNoficationid(flag);;
					Intent it = new Intent(PushConfig.getActionNotificClick(mContext));
					it.putExtra("notification_data", data);
					
					PendingIntent pIntent = PendingIntent.getBroadcast(mContext, flag, it, PendingIntent.FLAG_UPDATE_CURRENT);
					
					Notification notification = new NotificationCompat.Builder(mContext)
							.setSmallIcon(smallIconID)
							.setLargeIcon(weakBitmap.get())
							.setContentTitle(data.getTitle())
							.setContentText(data.getText())
							.setTicker(data.getAlert())
							.setContentIntent(pIntent)
							.build();
					notification.flags |= Notification.FLAG_AUTO_CANCEL;
					notification.defaults = Notification.DEFAULT_ALL;

					mNotificationManager.notify(data.getNoficationid(), notification);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(largeIcon !=null){
				largeIcon.recycle();
			}
		}
	}
	
	private int getResourceID(Context mContext, String imgName){
		try {
			ApplicationInfo appInfo = mContext.getApplicationInfo();
			
			return mContext.getResources().getIdentifier(imgName, "drawable", appInfo.packageName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}
