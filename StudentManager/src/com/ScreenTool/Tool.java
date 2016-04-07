package com.ScreenTool;

import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;

public class Tool {
	
	Activity activity;
	
	public Tool(Activity activity) {
		this.activity = activity;
	}
	
	public int getTitleHigh() {
		int status = 50;
		Display d = activity.getWindowManager().getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		Rect rect = new Rect();
		d.getMetrics(dm);
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		status = rect.top;
		int contentTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
		return contentTop - status;
	}
}
