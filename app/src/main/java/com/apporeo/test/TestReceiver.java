package com.apporeo.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 功能描述:
 * Created by chen_fulei on 2018/1/9.
 */

public class TestReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("--> " , "TestReceiver");

        OkHttpUtils
                .get()
                .url("http://bapi.baby-kingdom.com/index.php?mod=misc&op=smiley&ver=2.0.0&app=android")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("-->" , "onResponse--> "+response);
                    }
                });
    }
}
