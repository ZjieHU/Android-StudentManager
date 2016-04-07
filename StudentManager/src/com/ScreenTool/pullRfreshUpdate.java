package com.ScreenTool;

import android.os.Handler;
import android.os.Message;

public class pullRfreshUpdate extends Thread {
	public static final int SUCCESS_UPDATE = 1;
	public static final int ERROR_UPDATE = 0;
	
	private Handler handler;
	
	public pullRfreshUpdate(Handler handler) {
		this.handler = handler;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			Message msg = new Message();
			msg.what = ERROR_UPDATE;
			handler.sendMessage(msg);
			e.printStackTrace();
		}
		Message msg = new Message();
		msg.what = SUCCESS_UPDATE;
		handler.sendMessage(msg);
	}
	
}
