package com.vulfox.component;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class ImageComponent extends ScreenComponent {

	private Bitmap mBitmap;

	private Rect mRect;

	private Paint mImagePaint;

	private float aspectRatio;

	public ImageComponent(Bitmap bitmap) {
		mBitmap = bitmap;
		setHeight(mBitmap.getHeight());
		setWidth(mBitmap.getWidth());
		mRect = new Rect();
		mImagePaint = new Paint();
		aspectRatio = mBitmap.getWidth() / (float) mBitmap.getHeight();
	}

	@Override
	public void draw(Canvas canvas) {

		// TODO: move to method.
		mRect.set(getPositionX(), getPositionY(), getPositionX() + getWidth(),
				getPositionY() + getHeight());

		mImagePaint.setAntiAlias(true);
		canvas.drawBitmap(mBitmap, null, mRect, mImagePaint);
	}

	/**
	 * Resize the bitmap. This avoids ugly scaling effects when trying to draw a
	 * large image in a small rect.
	 */
	public void resizeBitmap() {

		int width = mBitmap.getWidth();
		int height = mBitmap.getHeight();
		float scaleWidth = ((float) getWidth()) / width;
		float scaleHeight = ((float) getHeight()) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix,
				true);
	}

	@Override
	public void handleActionDown(MotionEvent motionEvent,
			boolean insideConponent) {
	}

	@Override
	public boolean handleActionUp(MotionEvent motionEvent,
			boolean insideConponent) {
		return false;
	}

	@Override
	public void handleActionMove(MotionEvent motionEvent,
			boolean insideConponent) {
	}

	/**
	 * @param widthDp
	 *            Density-independent pixel
	 * @param deviceDpi
	 *            dots per inch for the device.
	 */
	public void setWidthInDpAutoSetHeight(int widthDp, int deviceDpi) {
		float fraction = deviceDpi / 160.0f;
		setWidth((int) (widthDp * fraction));
		setHeight((int) (getWidth() / aspectRatio));
	}

	/**
	 * @param heightDp
	 *            Density-independent pixel
	 * @param deviceDpi
	 *            dots per inch for the device.
	 */
	public void setHeightInDpAutoSetWidth(int heightDp, int deviceDpi) {
		float fraction = deviceDpi / 160.0f;
		setHeight((int) (heightDp * fraction));
		setWidth((int) (getHeight() * aspectRatio));
	}

}
