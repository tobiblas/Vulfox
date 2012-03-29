package com.vulfox.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

public class GraphicsUtil {

	public static float dpToPixels(float dp, float deviceDpi) {
		float fraction = deviceDpi / 160.0f;
		return dp * fraction;
	}
	
	public static float pixelsToDp(float pixels, float deviceDpi) {
		float fraction = deviceDpi / 160.0f;
		return pixels / fraction;
	}
	
	/**
	 * Resize the bitmap. 1. Scale bitmap so that height is correct and scale
	 * width as well to preserve aspect ratio. 2. Either add or remove stripes
	 * in the middle to get the correct width.
	 */
	public static Bitmap resizeBitmap(Bitmap bitmap, int newHeight, int newWidth) {

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float scaleHeight = ((float) newHeight) / height;

		// keep the aspect ratio for now. i.e. set width to appropriate value.
		Matrix matrix = new Matrix();
		matrix.postScale(scaleHeight, scaleHeight);

		// This is a bitmap scaled so that height is correct. Now the width must
		// be adjusted.
		Bitmap heightScaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
				height, matrix, true);

		Bitmap result = null;

		if (heightScaledBitmap.getWidth() > newWidth) {

			// The bitmap is too wide. Drop some pixels in the middle.
			result = Bitmap.createBitmap(newWidth, newHeight,
					Bitmap.Config.ARGB_8888);

			Canvas canvas = new Canvas(result);
			Rect sourceRect = new Rect();
			Rect destinationRect = new Rect();

			// Draw the left part of the button.
			sourceRect
					.set(0, 0, newWidth / 2, heightScaledBitmap.getHeight());
			destinationRect
					.set(0, 0, result.getWidth() / 2, result.getHeight());
			canvas.drawBitmap(heightScaledBitmap, sourceRect, destinationRect,
					null);

			// Draw the right part of the button.
			// Add one pixel to avoid gap if width is uneven.
			int extraPixel = result.getWidth() % 2 == 0 ? 0 : 1;
			sourceRect.set(heightScaledBitmap.getWidth() - newWidth / 2
					- extraPixel, 0, heightScaledBitmap.getWidth(),
					heightScaledBitmap.getHeight());
			destinationRect.set(
					result.getWidth() - newWidth / 2 - extraPixel, 0,
					result.getWidth(), result.getHeight());
			canvas.drawBitmap(heightScaledBitmap, sourceRect, destinationRect,
					null);

		} else if (heightScaledBitmap.getWidth() < newWidth) {

			// The bitmap is too short. Add some pixels in the middle.

			int middleX = heightScaledBitmap.getWidth() / 2;

			// Middle stripe of the height scaled bitmap.
			Bitmap middleStripe = Bitmap.createBitmap(heightScaledBitmap,
					middleX, 0, 1, heightScaledBitmap.getHeight());

			result = Bitmap.createBitmap(newWidth, newHeight,
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
}
