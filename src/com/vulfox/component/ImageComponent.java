package com.vulfox.component;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

public class ImageComponent extends ButtonComponent {

	private Bitmap mBitmap;

	private Rect mRect;

	private Paint mImagePaint;

	private float aspectRatio;
	
	private boolean mTintWhenClicked;
	
	/** Used if clickable. */
	private Paint mTintPaint;

	public ImageComponent(Bitmap bitmap, boolean tintWhenClicked) {
		mBitmap = bitmap;
		setHeight(mBitmap.getHeight());
		setWidth(mBitmap.getWidth());
		mTintWhenClicked = tintWhenClicked;
		mRect = new Rect();
		mImagePaint = new Paint();
		aspectRatio = mBitmap.getWidth() / (float) mBitmap.getHeight();
		if (tintWhenClicked) {
			mTintPaint = new Paint();
			ColorFilter filter = new LightingColorFilter(0x11cccccc, 1);
			mTintPaint.setColorFilter(filter);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		
		if (!isVisible()) {
			return;
		}
		
		// TODO: move to method.
		mRect.set(getPositionX(), getPositionY(), getPositionX() + getWidth(),
				getPositionY() + getHeight());

		mImagePaint.setAntiAlias(true);

		//Draw button image.
		if (mTintWhenClicked) {
			if (!mPressed) {
				canvas.drawBitmap(mBitmap, null, mRect, mImagePaint);
			} else {
				canvas.drawBitmap(mBitmap, getPositionX(),
						getPositionY(), mTintPaint);
			}
		} else {
			canvas.drawBitmap(mBitmap, null, mRect, mImagePaint);
		}
		
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

		Bitmap temp = Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix,
				true);
		
		//some phones cheats if target width and height is same as original. In that case do a copy or we will crash because of the recycle.
		if (temp == mBitmap) {
			temp = mBitmap.copy(mBitmap.getConfig(), mBitmap.isMutable() ? true : false);
		}
		
		mBitmap.recycle();
		mBitmap = temp;
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

	/**
	 * @return the mBitmap
	 */
	public Bitmap getBitmap() {
		return mBitmap;
	}

}
