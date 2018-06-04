package com.apporeo.push;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BKMessageList {
	private String status;
	private String message;
	private String next_time;
	private ArrayList<BKMessage> data = new ArrayList<BKMessage>();

	public BKMessageList() {
	}
	
	public BKMessageList(String json){
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
				JSONArray ja = jobj.getJSONArray("data");
				for(int i =0 ; i < ja.length() ; i++){
					data.add(new BKMessage(ja.getJSONObject(i)));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNext_time() {
		return next_time;
	}

	public void setNext_time(String next_time) {
		this.next_time = next_time;
	}

	public ArrayList<BKMessage> getData() {
		return data;
	}

	public void setData(ArrayList<BKMessage> data) {
		this.data = data;
	}

}
