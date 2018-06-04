package com.apporeo;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import com.apporeo.test.TestReceiver;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * 功能描述:
 * Created by chen_fulei on 2018/1/9.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();
        OkHttpUtils.initClient(okHttpClient);

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.testReceiver");

        registerReceiver(new TestReceiver() , filter);

    }
}
