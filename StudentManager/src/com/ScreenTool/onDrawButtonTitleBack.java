package com.ScreenTool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.Button;

@SuppressLint("DrawAllocation")
public class onDrawButtonTitleBack extends Button {

	public onDrawButtonTitleBack(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setStrokeWidth(5);
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.STROKE);
		RectF rect = new RectF(5,5,getWidth() - 5,getHeight() - 5);
		canvas.drawRoundRect(rect,10,10, paint);
		super.onDraw(canvas);
	}

}
