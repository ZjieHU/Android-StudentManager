package com.modle;

import android.graphics.Bitmap;

public class connectMessage {
	
	private Bitmap src;
	private String name;
	private String leastMsg;
	private String url;
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Bitmap getSrc() {
		return src;
	}
	public void setSrc(Bitmap src) {
		this.src = src;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLeastMsg() {
		return leastMsg;
	}
	public void setLeastMsg(String leastMsg) {
		this.leastMsg = leastMsg;
	}
	
}
