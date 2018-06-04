package com.apporeo.push;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class BKMessage implements Serializable{
	private static final long serialVersionUID = 1053412967314724078L;
	
	private String alert;
	private String title;
	private String text;
	private String time;
	private String jsoncustom;
	private int noficationid;

	private ArrayList<BKMessage> data = new ArrayList<BKMessage>();

	public BKMessage() {
	}

	public BKMessage(JSONObject json) {
		if (json != null) {
			fromJson(json);
		}
	}

	private void fromJson(JSONObject json) {
		try {
			
			if(json.has("alert")){
				setAlert(json.getString("alert"));
			}
			if(json.has("title")){
				setTitle(json.getString("title"));
			}
			if(json.has("text")){
				setText(json.getString("text"));
			}
			if(json.has("time")){
				setTime(json.getString("time"));
			}
			if(json.has("jsoncustom")){
				setJsoncustom(json.getString("jsoncustom"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getJsoncustom() {
		return jsoncustom;
	}

	public void setJsoncustom(String jsoncustom) {
		this.jsoncustom = jsoncustom;
	}

	public int getNoficationid() {
		return noficationid;
	}

	public void setNoficationid(int noficationid) {
		this.noficationid = noficationid;
	}

	public ArrayList<BKMessage> getData() {
		return data;
	}

	public void setData(ArrayList<BKMessage> data) {
		this.data = data;
	}
}
