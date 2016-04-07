package com.ScreenTool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("DrawAllocation")

public class onDrawTextView extends TextView {

	public onDrawTextView(Context context) {
		super(context);
	}

	public onDrawTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.rgb(102,198,198));
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setStrokeWidth(3);
		RectF rect = new RectF(3,3,getWidth() - 3,getHeight() - 3);
		canvas.drawRoundRect(rect, 10, 10, paint);
		super.onDraw(canvas);
	}

}
