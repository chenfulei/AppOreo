package com.apporeo.push;

import android.text.TextUtils;

import org.json.JSONObject;

public class TokenBean {

	private String message;
	private String status;
	private String next_time;
	
	private String token;
	private String expires;
	private String deviceid;

	public TokenBean(){
	}

	public TokenBean(String json){
		if(!TextUtils.isEmpty(json)){
			fromJson(json);
		}
	}
	
	private void fromJson(String json) {
		try {
			JSONObject jobj = new JSONObject(json);
			
			setStatus(jobj.getString("status"));
			setMessage(jobj.getString("message"));
			if(jobj.has("next_time")){
				setNext_time(jobj.getString("next_time"));
			}
			
			if(jobj.has("data")){
				JSONObject jo = jobj.getJSONObject("data");
				if(jo.has("token")){
					setToken(jo.getString("token"));
				}
				
				if(jo.has("expires")){
					setExpires(jo.getString("expires"));
				}
				
				if(jo.has("deviceid")){
					setDeviceid(jo.getString("deviceid"));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getExpires() {
		return expires;
	}

	public void setExpires(String expires) {
		this.expires = expires;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getNext_time() {
		return next_time;
	}

	public void setNext_time(String next_time) {
		this.next_time = next_time;
	}
}
