package com.vulfox.util;

import com.vulfox.math.Vector2f;

public class Vector2fPool extends ObjectPool<Vector2f> {

	// Singleton implementation
	
	private static Vector2fPool mInstance = null;

	public static Vector2fPool getInstance() {
		if (mInstance == null) {
			mInstance = new Vector2fPool();
		}
		return mInstance;
	}

	private Vector2fPool() {
	}

	@Override
	protected Vector2f create() {
		return new Vector2f();
	}

	@Override
	protected void reset(Vector2f poolObject) {
		poolObject.set(0.0f, 0.0f);
	}

}
