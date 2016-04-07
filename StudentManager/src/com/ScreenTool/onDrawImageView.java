package com.ScreenTool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class onDrawImageView extends ImageView {
	private Paint paint = new Paint();

	public onDrawImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	//将头像按比例缩放
	private Bitmap scaleBitmap(Bitmap bitmap) {
		int width = getWidth();
		float scale = (float)width/(float)bitmap.getWidth();
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
	}
	
	//将图像裁剪成正方形
	private Bitmap dealRawBitmap(Bitmap bitmap ) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		
		int minWidth = width > height ? height : width;
		
		int leftTopX = (width - minWidth) / 2;
		int leftTopY = (height - minWidth) / 2;
		
		Bitmap newbitmap = Bitmap.createBitmap(bitmap,leftTopX,leftTopY,minWidth,minWidth,null,false);
		return scaleBitmap(newbitmap);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();
		if(drawable != null) {
			Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
			Bitmap newbitmap = dealRawBitmap(bitmap);
			Bitmap circleBitmap = toRoundConer(newbitmap,14);
			
			final Rect rect = new Rect(0, 0, circleBitmap.getWidth(), circleBitmap.getHeight());  
            paint.reset();  
            //绘制到画布上  
            canvas.drawBitmap(circleBitmap, rect, rect, paint);  
		}
		super.onDraw(canvas);
	}
	
	private Bitmap toRoundConer(Bitmap bitmap, int pixes) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_4444);  
        Canvas canvas = new Canvas(output);  
  
        final int color = 0xff424242;  
        final Rect rect = new Rect(0, 0,bitmap.getWidth(), bitmap.getHeight());  
        paint.setAntiAlias(true);  
        canvas.drawARGB(0, 0, 0, 0);  
        paint.setColor(color);  
        int x = bitmap.getWidth();  
        canvas.drawCircle(x / 2, x / 2, x / 2, paint);  
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));  
        canvas.drawBitmap(bitmap, rect, rect, paint);  
        return output;  
	}
	
}
