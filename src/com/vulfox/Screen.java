package com.vulfox;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

public abstract class Screen {

	protected int mWidth;
	protected int mHeight; 
	protected Context mContext;
	
	/**
	 * Called when the surface manager has been initialized
	 * @param width
	 * @param height
	 * @param context
	 */
	protected final void initialize(int width, int height, Context context) {
		mWidth = width;
		mHeight = height;
		mContext = context;
		
		initialize();
	}
	
	/**
	 * Override to load content and set up your game data
	 */
	protected void initialize() {
		
	}
	
	public void update(float timeStep) {
		
	}
	
	public void draw(Canvas canvas) {
		
	}
	
	
	public void handleInput(MotionEvent motionEvent) {
		
	}
	
	
}
