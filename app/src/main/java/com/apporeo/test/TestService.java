package com.apporeo.test;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 功能描述:
 * Created by chen_fulei on 2018/1/9.
 */

public class TestService extends Service {

    private TestReceiver testReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.oreo.test.receiver");
        testReceiver = new TestReceiver();
        registerReceiver(testReceiver , intentFilter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startTimer(5000);

        return Service.START_STICKY_COMPATIBILITY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        destroyTimer();

        unregisterReceiver(testReceiver);
    }

    private Timer timer;
    private TimerTask timerTask;

    private void startTimer(long delay){
        destroyTimer();

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                sendBroadcast(new Intent("android.oreo.test.receiver"));
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
