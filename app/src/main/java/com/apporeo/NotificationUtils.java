package com.apporeo;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;

import com.apporeo.test.TestReceiver;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.math.BigInteger;

/**
 * 功能描述:
 * Created by chen_fulei on 2018/1/11.
 */

public class NotificationUtils extends ContextWrapper {

    private NotificationManager manager;
    public static final String id = "channel_1";
    public static final String name = "channel_name_1";

    public NotificationUtils(Context context) {
        super(context);
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
//        AudioAttributes AUDIO_ATTRIBUTES_DEFAULT =new AudioAttributes.Builder()
//                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                .build();
//        channel.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" +R.raw.aa) , AUDIO_ATTRIBUTES_DEFAULT);
        getManager().createNotificationChannel(channel);
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(final String title, final String content) {

        Glide.with(this).load("http://g.hiphotos.baidu.com/image/pic/item/2cf5e0fe9925bc312224243152df8db1cb137019.jpg").asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

//                RemoteViews remoteView = new RemoteViews(getPackageName() , R.layout.inc_notification_view);
//                remoteView.setTextViewText(R.id.txt_content , content);
//                remoteView.setImageViewBitmap(R.id.iv_icon , resource);

                //点击快速回复中发送按钮的时候，会发送一个广播给GetMessageReceiver
                Intent intent = new Intent("com.testReceiver");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent, PendingIntent.FLAG_ONE_SHOT);

                RemoteInput remoteInput = new RemoteInput.Builder("key_text_reply")
                        .setLabel("快速回复")
                        .build();

                Notification.Action replyAction = new Notification.Action.Builder(
                        android.R.drawable.stat_notify_more,"回复", pendingIntent)
                        .build();

                Notification notification = new Notification.Builder(getApplicationContext(), id)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setSmallIcon(android.R.drawable.stat_notify_more)
                        .setShowWhen(true)
                        .setColor(new BigInteger("000000", 16).intValue())
                        .setLargeIcon(resource)
                        .setGroup(NOTIFICATION_GROUP)
                        .addAction(replyAction)
                        .setStyle(new Notification.BigPictureStyle()
                                .bigPicture(resource))
                        .build();

                getManager().notify(getNewNotificationId(), notification);
//                //如果有必要，增加/更新/移除通知的归类
                updateNotificationSummary();
            }
        });

        return null;
    }

    public NotificationCompat.Builder getNotification_25(final String title, final String content) {

        Glide.with(this).load("http://g.hiphotos.baidu.com/image/pic/item/2cf5e0fe9925bc312224243152df8db1cb137019.jpg").asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                Notification notification = new NotificationCompat.Builder(getApplicationContext())
                        .setContentTitle(title)
                        .setContentText(content)
                        .setSmallIcon(android.R.drawable.stat_notify_more)
                        .setShowWhen(true)
                        .setColor(getResources().getColor(R.color.colorAccent))
                        .setLargeIcon(resource)
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(resource))
                        .setGroup(NOTIFICATION_GROUP)
                        .build();

                getManager().notify(getNewNotificationId(), notification);
                //如果有必要，增加/更新/移除通知的归类
                updateNotificationSummary();
            }
        });

        return null;
    }

    public void sendNotification(String title, String content) {
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel();
//            Notification notification = getChannelNotification(title, content).build();
//            getManager().notify(1,notification);

            getChannelNotification(title, content);
        } else {
//            Notification notification = getNotification_25(title, content).build();
//            getManager().notify(1,notification);

            getNotification_25(title, content);
        }

        //this is test
        //
    }

    /**
     * 归类的key
     */
    private static final String NOTIFICATION_GROUP =
            "com.example.android.activenotifications.notification_type";

    /**
     * 通知组别的id
     */
    private static final int NOTIFICATION_GROUP_SUMMARY_ID = 1;

    /**
     * 这是每一个通知的唯一id，如果不唯一，前一个通知会被后一个通知覆盖
     */
    private static int sNotificationId = NOTIFICATION_GROUP_SUMMARY_ID + 1;

    /**
     * 如果有必要，增加/更新/移除通知的归类
     */
    protected void updateNotificationSummary() {
        int numberOfNotifications = getNumberOfNotifications();
        if (numberOfNotifications > 1) { //如果数量>=2,说明有了同样组key的通知，需要归类起来
            //将通知添加/更新归类到同一组下面
            String notificationContent = "两条数据";
            final Notification notification;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notification = new Notification.Builder(getApplicationContext(), id)
                        .setSmallIcon(android.R.drawable.stat_notify_more)
                        //添加富样式到通知的显示样式中，如果当前系统版本不支持，那么将不起作用，依旧用原来的通知样式
                        .setStyle(new Notification.BigTextStyle()
                                .setSummaryText(notificationContent))
                        .setGroup(NOTIFICATION_GROUP) //设置类组key，说明此条通知归属于哪一个归类
                        .setGroupSummary(true).build(); //这句话必须和上面那句一起调用，否则不起作用
            } else {
                notification = new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(android.R.drawable.stat_notify_more)
                        //添加富样式到通知的显示样式中，如果当前系统版本不支持，那么将不起作用，依旧用原来的通知样式
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .setSummaryText(notificationContent))
                        .setGroup(NOTIFICATION_GROUP) //设置类组key，说明此条通知归属于哪一个归类
                        .setGroupSummary(true).build(); //这句话必须和上面那句一起调用，否则不起作用
            }
            //发送通知到状态栏
            //测试发现，发送归类状态栏也是算一条通知的。所以返回值得时候，需要-1
            getManager().notify(NOTIFICATION_GROUP_SUMMARY_ID, notification);
        } else {
            //移除归类
            getManager().cancel(NOTIFICATION_GROUP_SUMMARY_ID);
        }
    }


    /**
     * 获取当前状态栏具有统一id的通知的数量
     *
     * @return 数量
     */
    @TargetApi(Build.VERSION_CODES.M)
    private int getNumberOfNotifications() {
        //查询当前展示的所有通知的状态列表
        final StatusBarNotification[] activeNotifications = getManager().getActiveNotifications();

        //获取当前通知栏里头，NOTIFICATION_GROUP_SUMMARY_ID归类id的组别
        //因为发送分组的通知也算一条通知，所以需要-1
        for (StatusBarNotification notification : activeNotifications) {
            if (notification.getId() == NOTIFICATION_GROUP_SUMMARY_ID) {
                //-1是因为
                return activeNotifications.length - 1;
            }
        }
        return activeNotifications.length;
    }

    /**
     * 返回一个通知的唯一id
     */
    public int getNewNotificationId() {
        int notificationId = sNotificationId++;
        if (notificationId == NOTIFICATION_GROUP_SUMMARY_ID) {
            notificationId = sNotificationId++;
        }
        return notificationId;
    }
}