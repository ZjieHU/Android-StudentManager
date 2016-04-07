package com.ScreenTool;

import android.os.Handler;
import android.os.Message;

public class delatTime extends Thread {
	
	private Handler handler;
	private int sign;
	
	public delatTime(Handler handler, int sign) {
		this.handler = handler;
		this.sign = sign;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(300 *(sign));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Message msg = new Message();
		msg.what = sign;
		handler.sendMessage(msg);
	}
	
}
