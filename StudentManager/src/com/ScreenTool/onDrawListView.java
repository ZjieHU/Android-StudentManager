package com.ScreenTool;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class onDrawListView extends ListView {

	public boolean isOnMeasure ;
	
	public onDrawListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		isOnMeasure = true;
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		isOnMeasure = false;
		super.onLayout(changed, l, t, r, b);
	}
}
