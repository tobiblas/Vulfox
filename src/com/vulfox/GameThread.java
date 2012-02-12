package com.vulfox;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

	private boolean mPaused;

	private boolean mDone;

	private Context mContext;
	
	private SurfaceHolder mSurfaceHolder;

	private int mWidth;
	private int mHeight;

	private boolean mHasSurface;
	
	private ScreenManager mScreenManager;
	
	private LinkedList<MotionEvent> mMotionEvents;

	public GameThread(SurfaceHolder surfaceHolder, Context context, ScreenManager screenManager) {
		mContext = context;
		mSurfaceHolder = surfaceHolder;
		mScreenManager = screenManager;
		mMotionEvents = new LinkedList<MotionEvent>();
	}

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

	public synchronized void onResume() {
		mPaused = false;
		notify();
	}

	public synchronized void onPause() {
		mPaused = true;
	}
	
	public synchronized void onTouch(MotionEvent motionEvent) {
		mMotionEvents.addLast(motionEvent);
	}

	public synchronized void onSurfaceCreated() {
	}

	public synchronized void onSurfaceDestroyed() {
		mHasSurface = false;
	}

	public synchronized void onSurfaceChanged(int width, int height) {
		mWidth = width;
		mHeight = height;
		mHasSurface = true;
		mScreenManager.initialize(mWidth, mHeight);
		notify();
	}

}
