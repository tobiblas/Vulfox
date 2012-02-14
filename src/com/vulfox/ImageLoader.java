package com.vulfox;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Helper class for loading images 
 */
public class ImageLoader {
	/**
	 * Loads a bitmap from a resource ID
	 * @param context Application context
	 * @param resourceID Resource ID
	 * @return The loaded bitmap
	 */
	public static Bitmap loadFromResource(Context context, int resourceID) {
		Bitmap bitmap = null;
		if (context != null) {
			InputStream is = context.getResources().openRawResource(resourceID);
			try {
				bitmap = BitmapFactory.decodeStream(is);
			} finally {
				try {
					is.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		return bitmap;
	}
}
