
/**
 * 解析 push 用法
 * @author chen_fulei
 */
package com.apporeo.push;

/*配置方面:AndroidManifest.xml

 
   <service android:name="com.bk.sdk.common.push.PushService"
   		 android:exported="false"
         android:process=":push" >
         <intent-filter >
              <action android:name="#(package).intent.action.change.TIMEOUT"/>
         </intent-filter>
    service />   
    
    <receiver android:name="com.bk.sdk.common.push.PushReceiver"
         android:exported="false"
         android:process=":push">
         <intent-filter >
              <action android:name="#(package).intent.action.COMMAND"/>
         </intent-filter>
    </receiver>
        
    <receiver android:name="com.bk.sdk.common.push.callback.DataChangeReceiver"
            android:process=":push"
            android:exported="false">
            <intent-filter>
                <action android:name="#(package).intent.action.change.MSG_DATA" />
            </intent-filter>
     </receiver>
     
     <!-- PushMessageHandler要继承这个广播类，在自己项目中（接收显示通知前信息处理和点击通知后信息处理） -->
     <receiver android:name="#(package).PushMessageHandler" 
            android:exported="false">
            <intent-filter >
                <action android:name="#(package).intent.action.notification.CLICK"/>
                <action android:name="#(package).intent.action.message.HANDLER"/>
            </intent-filter>
        </receiver>
        
    <meta-data
        android:name="HKBK_APPKEY"
        android:value="com.bk.push" >
    </meta-data>
        
    <meta-data
        android:name="HKBK_MESSAGE_SECRET"
        android:value="1afcb7a53a2940d3c9e6f7b0a1293c94" >
    </meta-data>
    
    //方法使用
     
     //开启推送
     PushAgent.getInstance(Context).enable();
	//关闭推送
     PushAgent.getInstance(Context).disable();
     //判断是否开启推送
     PushAgent.getInstance(Context).isEnable();
     //获取推送设备id
     PushAgent.getInstance(Context).getRegistrationId();
     
 * 
 */


