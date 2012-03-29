package com.vulfox.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.vulfox.ImageLoader;

/**
 * A class representing an image.
 * 
 * @author tobiblas
 */
public class StretchableImageComponent extends ScreenComponent {

	/** The image bitmap. */
	private Bitmap mBackground;

	/**
	 * Loads images and constructs a strechable image. Consider not calling
	 * from UI thread since loading images might take time.
	 */
	public StretchableImageComponent(Context context, int background, int widthDp, int heightDp, int dpi) {

		this.mBackground = ImageLoader.loadFromResource(context, background);

		setWidthAndHeightInDp(widthDp, heightDp, dpi);

		try {
			mBackground = resizeBitmap(mBackground);
		} catch (OutOfMemoryError oomE1) {
			System.gc();
			try {
				mBackground = resizeBitmap(mBackground);
			} catch (OutOfMemoryError oomE2) {
				// No memory. Skip it!
				return;
			}
		}
		
	}

	/**
	 * Resize the bitmap. 1. Scale bitmap so that height is correct and scale
	 * width as well to preserve aspect ratio. 2. Either add or remove stripes
	 * in the middle to get the correct width.
	 */
	private Bitmap resizeBitmap(Bitmap bitmap) {

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float scaleHeight = ((float) getHeight()) / height;

		// keep the aspect ratio for now. i.e. set width to appropriate value.
		Matrix matrix = new Matrix();
		matrix.postScale(scaleHeight, scaleHeight);

		// This is a bitmap scaled so that height is correct. Now the width must
		// be adjusted.
		Bitmap heightScaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
				height, matrix, true);

		Bitmap result = null;

		if (heightScaledBitmap.getWidth() > getWidth()) {

			// The bitmap is too wide. Drop some pixels in the middle.
			result = Bitmap.createBitmap(getWidth(), getHeight(),
					Bitmap.Config.ARGB_8888);

			Canvas canvas = new Canvas(result);
			Rect sourceRect = new Rect();
			Rect destinationRect = new Rect();

			// Draw the left part of the button.
			sourceRect
					.set(0, 0, getWidth() / 2, heightScaledBitmap.getHeight());
			destinationRect
					.set(0, 0, result.getWidth() / 2, result.getHeight());
			canvas.drawBitmap(heightScaledBitmap, sourceRect, destinationRect,
					null);

			// Draw the right part of the button.
			// Add one pixel to avoid gap if width is uneven.
			int extraPixel = result.getWidth() % 2 == 0 ? 0 : 1;
			sourceRect.set(heightScaledBitmap.getWidth() - getWidth() / 2
					- extraPixel, 0, heightScaledBitmap.getWidth(),
					heightScaledBitmap.getHeight());
			destinationRect.set(
					result.getWidth() - getWidth() / 2 - extraPixel, 0,
					result.getWidth(), result.getHeight());
			canvas.drawBitmap(heightScaledBitmap, sourceRect, destinationRect,
					null);

		} else if (heightScaledBitmap.getWidth() < getWidth()) {

			// The bitmap is too short. Add some pixels in the middle.

			int middleX = heightScaledBitmap.getWidth() / 2;

			// Middle stripe of the height scaled bitmap.
			Bitmap middleStripe = Bitmap.createBitmap(heightScaledBitmap,
					middleX, 0, 1, heightScaledBitmap.getHeight());

			result = Bitmap.createBitmap(getWidth(), getHeight(),
					Bitmap.Config.ARGB_8888);

			Canvas canvas = new Canvas(result);
			Rect sourceRect = new Rect();
			Rect destinationRect = new Rect();

			// Draw the left part of the button.
			sourceRect.set(0, 0, heightScaledBitmap.getWidth() / 2,
					heightScaledBitmap.getHeight());
			destinationRect.set(0, 0, heightScaledBitmap.getWidth() / 2,
					heightScaledBitmap.getHeight());
			canvas.drawBitmap(heightScaledBitmap, sourceRect, destinationRect,
					null);

			// Draw the right part of the button.
			sourceRect.set(
					heightScaledBitmap.getWidth()
							- heightScaledBitmap.getWidth() / 2, 0,
					heightScaledBitmap.getWidth(),
					heightScaledBitmap.getHeight());
			destinationRect.set(
					result.getWidth() - heightScaledBitmap.getWidth() / 2, 0,
					result.getWidth(), result.getHeight());
			canvas.drawBitmap(heightScaledBitmap, sourceRect, destinationRect,
					null);

			// Draw the middle part of the button.
			sourceRect.set(0, 0, 1, middleStripe.getHeight());
			destinationRect.set(heightScaledBitmap.getWidth() / 2, 0,
					result.getWidth() - heightScaledBitmap.getWidth() / 2,
					result.getHeight());
			canvas.drawBitmap(middleStripe, sourceRect, destinationRect, null);

		} else {
			// The bitmap is already perfect. Return it!
			result = heightScaledBitmap;
		}

		return result;
	}

	@Override
	public void draw(Canvas canvas) {

		//Draw image.
		if (mBackground != null) {
			canvas.drawBitmap(mBackground, getPositionX(), getPositionY(), null);
		}
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

}
