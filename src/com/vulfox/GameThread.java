package com.vulfox;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

	/**
	 * Indicates if the activity is paused or not
	 */
	private boolean mPaused;

	/**
	 * Indicates if the activity still is active
	 */
	private boolean mDone;

	/**
	 * Needed to load assets etc
	 */
	private Context mContext;
	
	/**
	 * Surface to draw to
	 */
	private SurfaceHolder mSurfaceHolder;

	/**
	 * Indicates if the surface is ready for drawing
	 */
	private boolean mHasSurface;
	
	/**
	 * Draw surface width
	 */
	private int mWidth;
	
	/**
	 * Draw surface height
	 */
	private int mHeight;

	/**
	 * Manager for screen handling
	 */
	private ScreenManager mScreenManager;
	
	/**
	 * Holds any motion events that occured between frames
	 */
	private LinkedList<MotionEvent> mMotionEvents;

	/**
	 * Creates the game thread that will be handling the screens
	 * @param surfaceHolder The surface we will be drawing to
	 * @param context Application context for loading assets etc
	 * @param screenManager The screen manager that will be holding the screens
	 */
	public GameThread(SurfaceHolder surfaceHolder, Context context, ScreenManager screenManager) {
		mContext = context;
		mSurfaceHolder = surfaceHolder;
		mScreenManager = screenManager;
		mMotionEvents = new LinkedList<MotionEvent>();
	}

	/**
	 * The main game loop is handled here
	 */
	@Override
	public void run() {

		Canvas canvas = null;
		long startTime = System.nanoTime();
		
		while (!mDone) {

			synchronized (this) {

				while (mPaused || !mHasSurface) {

					try {
						wait();
					} catch (InterruptedException e) {
					}
					
					startTime = System.nanoTime();
				}

				float timeStep = (float)((System.nanoTime() - startTime) / 1000000L);
				startTime = System.nanoTime();
				
				for (MotionEvent motionEvent : mMotionEvents) {
					mScreenManager.handleInput(motionEvent);
				}				
				mMotionEvents.clear();
				
				mScreenManager.update(timeStep);
				
				canvas = mSurfaceHolder.lockCanvas();
				if(canvas != null)
				{
					mScreenManager.draw(canvas);
					mSurfaceHolder.unlockCanvasAndPost(canvas);
				}								
			}	
		}
	}

	/**
	 * Called when the activity execution is paused
	 */
	public synchronized void onPause() {
		mPaused = true;
	}
	
	/**
	 * Called when the activity is resuming execution
	 */
	public synchronized void onResume() {
		mPaused = false;
		notify();
	}
	
	/**
	 * Add a motion event that will be handled the next frame
	 * @param motionEvent A touch motion event
	 */
	public synchronized void onTouch(MotionEvent motionEvent) {
		mMotionEvents.addLast(motionEvent);
	}

	/**
	 * Called when the surface has been created
	 */
	public synchronized void onSurfaceCreated() {
	}

	/**
	 * Called when the surface has been destroyed
	 */
	public synchronized void onSurfaceDestroyed() {
		mHasSurface = false;
	}

	/**
	 * Called when the surface has changed dimensions
	 * @param width Width of the surface
	 * @param height Height of the surface
	 */
	public synchronized void onSurfaceChanged(int width, int height) {
		mWidth = width;
		mHeight = height;
		mHasSurface = true;
		mScreenManager.initialize(mWidth, mHeight);
		notify();
	}

}
