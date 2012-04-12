package com.vulfox.util;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;

/**
 * The purpose with this class is to save memory and time (performance).
 * 
 * @author tobiblas
 *
 */
public final class BitmapManager {

	private static Map<Integer, Bitmap> bitmapMap;
	
	private BitmapManager() {
	}
	
	public static void addBitmap(Integer key, Bitmap bitmap) {
		if (bitmapMap == null) {
			bitmapMap = new HashMap<Integer, Bitmap>();
		}
		
		Bitmap previous = bitmapMap.get(key);
		if (previous != null && !previous.isRecycled()) {
			return;
		}
		bitmapMap.put(key, bitmap);
	}
	
	/**
	 * Gets a saved bitmap. If not found or if it has been recycled null is returned.
	 * @param key
	 * @return
	 */
	public static Bitmap getBitmap(Integer key) {
		if (bitmapMap == null) {
			bitmapMap = new HashMap<Integer, Bitmap>();
		}
		Bitmap bitmap = bitmapMap.get(key);
		
//		if (bitmap != null && bitmap.isRecycled()) {
//			bitmap = null;
//		}
		return bitmap;
	}
	
	
}
