package com.apporeo.service;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;
import android.util.Log;
import android.widget.Toast;

/**
 * 功能描述:
 * Created by chen_fulei on 2018/3/1.
 */

public class TestJobIntentService extends JobIntentService {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("---> " , "onCreate");
    }

    /**
     * Unique job ID for this service.
     */
    static final int JOB_ID = 1000;

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, TestJobIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4*1000);
                }catch (Exception e){
                    e.printStackTrace();
                }

                Log.e("-->"  ,"onHandleWork --> thread sleep");
            }
        }).start();
    }
}
