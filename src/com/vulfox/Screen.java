package com.vulfox;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

public abstract class Screen {

	/**
	 * Width of the draw surface
	 */
	protected int mWidth;
	
	/**
	 * Height of the draw surface
	 */
	protected int mHeight; 
	
	/**
	 * Application context
	 */
	protected Context mContext;
	
	/**
	 * Called when the surface manager has been initialized
	 * @param width Width of the draw surface
	 * @param height Height of the draw surface
	 * @param context Application context
	 */
	protected final void initialize(int width, int height, Context context) {
		mWidth = width;
		mHeight = height;
		mContext = context;
		
		initialize();
	}
	
	/**
	 * Is called when the screen is initialized
	 */
	protected void initialize() {
		
	}
	
	/**
	 * Is called once for every motion event at the beginning of a frame
	 * @param motionEvent
	 */
	public void handleInput(MotionEvent motionEvent) {
		
	}
	
	/**
	 * Is called once ever frame right after handleInput
	 * @param timeStep Time since last frame in milliseconds
	 */
	public void update(float timeStep) {
		
	}
	
	/**
	 * Is called once at the end of every frame
	 * @param canvas Canvas to draw to
	 */
	public void draw(Canvas canvas) {
		
	}	
}
