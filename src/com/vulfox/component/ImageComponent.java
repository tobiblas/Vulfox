package com.vulfox.component;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class ImageComponent extends ScreenComponent {

	private Bitmap mBitmap;

	private Rect mRect;
	
	private Paint mImagePaint;

	public ImageComponent(Bitmap bitmap) {
		mBitmap = bitmap;
		setHeight(mBitmap.getHeight());
		setWidth(mBitmap.getWidth());
		mRect = new Rect();
		mImagePaint = new Paint();
	}

	@Override
	public void draw(Canvas canvas) {

		// TODO: move to method.
		mRect.set(getPositionX(), getPositionY(), getPositionX() + getWidth(),
				getPositionY() + getHeight());

		mImagePaint.setAntiAlias(true);
		canvas.drawBitmap(mBitmap, null, mRect, mImagePaint);
	}

	@Override
	public void handleActionDown(MotionEvent motionEvent,
			boolean insideConponent) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean handleActionUp(MotionEvent motionEvent,
			boolean insideConponent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleActionMove(MotionEvent motionEvent,
			boolean insideConponent) {
		// TODO Auto-generated method stub

	}

}
