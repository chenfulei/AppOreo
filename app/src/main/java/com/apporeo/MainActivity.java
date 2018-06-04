package com.apporeo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.apporeo.service.TestJobIntentService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("-->", "test");

        findViewById(R.id.btn_notification).setOnClickListener(this);
        findViewById(R.id.btn_webview).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_notification:
                startActivity(new Intent(this, NotificationActivity.class));
                break;

            case R.id.btn_webview:
                startActivity(new Intent(this, WebviewActivity.class));
                break;
        }
    }
}
