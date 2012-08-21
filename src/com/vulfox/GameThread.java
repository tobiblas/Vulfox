package com.vulfox;

import java.util.LinkedList;

import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

	/**
	 * Default frame delay time (60 fps)
	 */
	private static final long DEFAULT_FRAME_TIME = (long) Math
			.round(1000.0 / 60.0);

	/**
	 * Indicates if the thread loop should use fixed time updates
	 */
	private boolean mFixedTimeStep;

	/**
	 * Total frame time in a fixed time step loop
	 */
	private long mFixedFrameTime = DEFAULT_FRAME_TIME;

	/**
	 * Indicates if the activity is paused or not
	 */
	private boolean mPaused;

	/**
	 * Indicates if the activity still is active
	 */
	private boolean mDone;

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
	
	/** List holding all motion events not yet handled. */
	private LinkedList<MotionEvent> mMotionEvents;

	/**
	 * Creates the game thread that will be handling the screens
	 * 
	 * @param surfaceHolder
	 *            The surface we will be drawing to
	 * @param context
	 *            Application context for loading assets etc
	 * @param screenManager
	 *            The screen manager that will be holding the screens
	 */
	public GameThread(SurfaceHolder surfaceHolder,
			ScreenManager screenManager) {
		setName("GameThread");
		mSurfaceHolder = surfaceHolder;
		mSurfaceHolder.setFormat(PixelFormat.RGBA_8888);
		mScreenManager = screenManager;
		mFixedTimeStep = true;
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

				float timeStep = (float) ((System.nanoTime() - startTime) / 1000000000d);
				startTime = System.nanoTime();

				synchronized (mMotionEvents) {
					for (MotionEvent motionEvent : mMotionEvents) {
						mScreenManager.handleInput(motionEvent);
					}
					mMotionEvents.clear();
				}

				mScreenManager.update(timeStep);

				canvas = mSurfaceHolder.lockCanvas();
				if (canvas != null) {
					mScreenManager.draw(canvas);
					mSurfaceHolder.unlockCanvasAndPost(canvas);
				}
				
				//If another thread has posted a new screen add it so that it can be drawn.
				mScreenManager.addScreens();
				
				//If another thread has posted a remove of a screen remove it.
				mScreenManager.removeScreens();
			}
			
			if (mFixedTimeStep) {
				
				long frameTime = ((System.nanoTime() - startTime) / 1000000L);
				long sleepTime = mFixedFrameTime - frameTime;
				if (sleepTime > 0) {
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
					}
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
	 * 
	 * @param motionEvent
	 *            A touch motion event
	 */
	public void onTouch(MotionEvent motionEvent) {
		synchronized (mMotionEvents) {
			mMotionEvents.addLast(MotionEvent.obtain(motionEvent));
		}
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
	 * 
	 * @param width
	 *            Width of the surface
	 * @param height
	 *            Height of the surface
	 */
	public synchronized void onSurfaceChanged(int width, int height) {

		mWidth = width;
		mHeight = height;
		mHasSurface = true;

		if (!mScreenManager.isInitialized()) {
			mScreenManager.initialize(mWidth, mHeight);
		}

		notify();
	}

	public boolean isFixedTimeStep() {
		return mFixedTimeStep;
	}

	public void setFixedTimeStep(boolean fixedTimeStep) {
		mFixedTimeStep = fixedTimeStep;
	}

	public float getFixedFrameTime() {
		return mFixedFrameTime;
	}

	public void setFixedFrameTime(long fixedFrameTime) {
		mFixedFrameTime = fixedFrameTime;
	}

	public void setFixedFPS(int fps) {
		mFixedFrameTime = (long) Math.round(1000.0 / fps);
	}

	public boolean onBackPressed() {
		return mScreenManager.handleBackPressed();
	}

}
