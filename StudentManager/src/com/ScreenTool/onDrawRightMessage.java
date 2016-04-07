package com.ScreenTool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("DrawAllocation")
public class onDrawRightMessage extends TextView {

	public onDrawRightMessage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.rgb(102, 182, 166));
		paint.setStyle(Style.FILL_AND_STROKE);
		RectF rect = new RectF(0,15,getWidth() - 25,getHeight());
		canvas.drawRoundRect(rect, 15, 15, paint);
		Path path = new Path();
		path.moveTo(getWidth() - 25, 30);
		path.lineTo(getWidth() - 25, 45);
		path.lineTo(getWidth() - 5, 25.5f);
		canvas.drawPath(path, paint);
		super.onDraw(canvas);
	}
}
