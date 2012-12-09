package com.vulfox;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;

public class ScreenManager {

	/**
	 * Application context
	 */
	private Context mContext;

	/**
	 * List of all the screens that are present in the game Used as a stack to
	 * keep track of the top screen
	 */
	private LinkedList<Screen> mScreenList;
	
	private LinkedList<Screen> mScreensToAddList;
	
	private LinkedList<Screen> mScreensToRemoveList;

	/**
	 * Width of the draw surface
	 */
	private int mWidth;

	/**
	 * Height of the draw surface
	 */
	private int mHeight;

	/**
	 * Indicates if the manager has been initialized
	 */
	private boolean mInitialized;

	/**
	 * Creates a new uninitialized screen manager
	 * 
	 * @param context
	 *            Application context
	 */
	public ScreenManager(Context context) {
		mContext = context;
		mScreenList = new LinkedList<Screen>();
		mScreensToAddList = new LinkedList<Screen>();
		mScreensToRemoveList = new LinkedList<Screen>();
		mInitialized = false;
	}

	/**
	 * Tells if the manager has been initialized
	 * 
	 * @return True if initialized
	 */
	public boolean isInitialized() {
		return mInitialized;
	}

	/**
	 * Initializes the screen manager and any screens that have been added
	 * 
	 * @param width
	 *            Width of the draw surface
	 * @param height
	 *            Height of the draw surface
	 */
	public synchronized void initialize(int width, int height) {
		mWidth = width;
		mHeight = height;
		mInitialized = true;

		for (Screen screen : mScreenList) {
			screen.initialize(mWidth, mHeight, mContext, this);
		}
	}

	/**
	 * To be called from UI thread.
	 * @param screen
	 */
	public void addScreenUI(Screen screen) {
		synchronized(mScreensToAddList) {
			mScreensToAddList.add(screen);
		}
	}
	
	/**
	 * Adds screens to the top of the screen stack. To be called by GameThread once per frame.
	 */
	public void addScreens() {
		
		checkCorrectThread();
	
		synchronized(mScreensToAddList) {

			for (Screen screen : mScreensToAddList) {
				if (mInitialized) {
					screen.initialize(mWidth, mHeight, mContext, this);
				}
	
				mScreenList.addLast(screen);
				screen.onTop();
			}
			mScreensToAddList.clear();
		}
	}

	/**
	 * To be called from UI thread
	 * @param screen
	 * @return
	 */
	public boolean removeScreenUI(Screen screen) {
		boolean result = false;
		synchronized(mScreensToRemoveList) {
			result = mScreensToRemoveList.add(screen);
		}
		return result;
	}
	
	/**
	 * Removes screens from the top of the screen stack. To be called by GameThread once per frame.
	 */
	public void removeScreens() {

		//Limitation: only 1 screen can be removed in one frame.
		
		checkCorrectThread();
		
		if (mScreensToRemoveList.size() == 0) {
			return;
		}
		
		synchronized (mScreensToRemoveList) {

			Screen topScreenToRemove = mScreensToRemoveList.remove();
			
			mScreenList.remove(topScreenToRemove);

			if (!mScreenList.isEmpty()) {
				mScreenList.getLast().onTop();
			}
		}

	}

	public Dialog onCreateDialog(int id, Dialog dialog, Bundle args) {
		
		Screen topScreen = null;
		
		try {
			topScreen = mScreenList.getLast(); // no need to synchronize.
			return topScreen.onCreateDialog(id, dialog, args);
		} catch (NoSuchElementException e) {
			//Ignoring
		}
		
		return null;
	}

	/**
	 * Relays the motion event to the top screen
	 * 
	 * @param motionEvent
	 */
	public void handleInput(MotionEvent motionEvent) {

		checkCorrectThread();
		
		if (!mInitialized) {
			return;
		}

		if (mScreenList.size() > 0) {
			Screen topScreen = mScreenList.getLast();
			boolean eventConsumedByScreenComponent = topScreen
					.handleComponentInput(motionEvent);
			if (!eventConsumedByScreenComponent) {
				topScreen.handleInput(motionEvent);
			}
		}

	}

    /**
     * Checks so that the GameThread is the one trying to access.
     */
	private void checkCorrectThread() {
		if (Thread.currentThread().getName().equals("GameThread")) {
			return;
		} else {
			throw new RuntimeException(
					"Incorrect thread calling screenmanager. Caller: "
							+ Thread.currentThread().getName());
		}
	}

	/**
	 * Updates the top screen
	 * 
	 * @param timeStep
	 *            Time since the last frame in milliseconds
	 */
	public void update(float timeStep) {

		checkCorrectThread();

		if (!mInitialized) {
			return;
		}

		if (mScreenList.size() > 0) {

			Screen topScreen = mScreenList.getLast();
			if (topScreen.coversWholeScreen()) {
				topScreen.update(timeStep);
				topScreen.updateComponents(timeStep);
			} else {
				if (mScreenList.size() > 1) {
					Screen backScreen = mScreenList.get(mScreenList.size() - 2);
					backScreen.update(timeStep);
					backScreen.updateComponents(timeStep);
					topScreen.update(timeStep);
					topScreen.updateComponents(timeStep);
				}
			}
		}
	}

	/**
	 * Draws the top screen
	 * 
	 * @param canvas
	 *            Canvas to draw to
	 */
	public void draw(Canvas canvas) {

		checkCorrectThread();

		if (!mInitialized) {
			return;
		}

		if (mScreenList.size() > 0) {

			Screen topScreen = mScreenList.getLast();
			if (topScreen.coversWholeScreen()) {
				topScreen.draw(canvas);
				topScreen.drawComponents(canvas);
				topScreen.postDraw(canvas);
			} else {
				if (mScreenList.size() > 1) {
					Screen backScreen = mScreenList.get(mScreenList.size() - 2);
					backScreen.draw(canvas);
					backScreen.drawComponents(canvas);
					backScreen.postDraw(canvas);
					topScreen.draw(canvas);
					topScreen.drawComponents(canvas);
					topScreen.postDraw(canvas);
				}
			}
		}
	}

	/**
	 * This will be called by UI thread when user presses the back button.
	 * @return
	 */
	public boolean handleBackPressed() {

		if (!mInitialized) {
			return false;
		}

		Screen topScreen = null;
			
		try {
			topScreen = mScreenList.getLast(); // no need to synchronize.
			return topScreen.handleBackPressed();
		} catch (NoSuchElementException e) {
			//Ignoring
		}
		return false;
	}

	public void removeTopScreen() {

		if (!mInitialized) {
			return;
		}

		Screen topScreen = null;
		
		try {
			topScreen = mScreenList.getLast(); // no need to synchronize.
			removeScreenUI(topScreen);
		} catch (NoSuchElementException e) {
			//Ignoring
		}
	}
	
	public void removeTopScreens(int screensToRemove) {

		if (!mInitialized) {
			return;
		}

		Screen topScreen = null;
		
		try {
			
			for (int i = 0; i < screensToRemove; i++) {
				topScreen = mScreenList.get(mScreenList.size() - (i+1));
				removeScreenUI(topScreen);
			}
			
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
	}

	public void handleShowOptionsMenu() {
		
		if (!mInitialized) {
			return;
		}

		Screen topScreen = null;
			
		try {
			topScreen = mScreenList.getLast(); // no need to synchronize.
			topScreen.handleShowOptionsMenu();
		} catch (NoSuchElementException e) {
			//Ignoring
		}
	}

}
