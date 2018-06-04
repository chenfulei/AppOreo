package com.apporeo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.apporeo.service.TestJobIntentService;

/**
 * 功能描述:
 * Created by chen_fulei on 2018/2/26.
 */

public class NotificationActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        findViewById(R.id.btn_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = "style.bigPicture传递的是个bitmap对象 所以也不应该传过大的图 否则会oom addline可以添加多行 但是多余5行的时候每行高度会有截断" +
                        "同BigTextStyle 低版本上系统只能显示普通样式点击前后的ContentTitle、ContentText可以不一致，bigText内容可以自动换行 好像最多5行的样子";
                NotificationUtils notificationUtils = new NotificationUtils(NotificationActivity.this);
                notificationUtils.sendNotification("测试标题", content);
            }
        });

        findViewById(R.id.btn_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestJobIntentService.enqueueWork(NotificationActivity.this ,new Intent("test"));
                finish();
            }
        });

    }
}
