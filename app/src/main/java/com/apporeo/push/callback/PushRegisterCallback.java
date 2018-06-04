package com.apporeo.push.callback;

/**
 * 注册获取设备id
 * @author chen_fulei
 *
 */
public interface PushRegisterCallback {
	
	void onRegistered(String deviceId);
}
