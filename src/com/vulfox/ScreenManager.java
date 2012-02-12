package com.vulfox;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

public class ScreenManager {

	private Context mContext;
	private LinkedList<Screen> mScreenList;

	private int mWidth;
	private int mHeight;

	private boolean mInitialized;

	public ScreenManager(Context context) {
		mContext = context;
		mScreenList = new LinkedList<Screen>();
		mInitialized = false;
	}

	public synchronized void initialize(int width, int height) {
		mWidth = width;
		mHeight = height;
		mInitialized = true;

		for (Screen screen : mScreenList) {
			screen.initialize(mWidth, mHeight, mContext);
		}
	}

	public synchronized void addScreen(Screen screen) {
		if (mInitialized) {
			screen.initialize(mWidth, mHeight, mContext);
		}

		mScreenList.addLast(screen);
	}

	public void handleInput(MotionEvent motionEvent) {
		if(!mInitialized) {
			return;
		}
		
		if (mScreenList.size() > 0) {
			Screen topScreen = mScreenList.getLast();
			topScreen.handleInput(motionEvent);
		}
	}

	public void update(float timeStep) {
		if(!mInitialized) {
			return;
		}
		
		if (mScreenList.size() > 0) {
			Screen topScreen = mScreenList.getLast();
			topScreen.update(timeStep);
		}
	}

	public void draw(Canvas canvas) {
		if(!mInitialized) {
			return;
		}
		
		if (mScreenList.size() > 0) {
			Screen topScreen = mScreenList.getLast();
			topScreen.draw(canvas);
		}
	}
}
