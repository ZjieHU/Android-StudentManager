package com.ScreenTool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;
@SuppressLint("DrawAllocation")
public class onDrawLeftMessage extends TextView {

	public onDrawLeftMessage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.rgb(102, 166, 166));
		paint.setStyle(Style.FILL_AND_STROKE);
		RectF rect = new RectF(25,15,getWidth(),getHeight());
		canvas.drawRoundRect(rect, 15, 15, paint);
		Path path = new Path();
		path.moveTo(25, 30);
		path.lineTo(25, 45);
		path.lineTo(5, 25.5f);
		canvas.drawPath(path, paint);
		super.onDraw(canvas);
	}
	
}
