package com.vulfox;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;

import com.vulfox.component.ScreenComponent;


public abstract class Screen {

	/**
	 * Width of the draw surface
	 */
	private int mWidth;

	/**
	 * Height of the draw surface
	 */
	private int mHeight;

	/**
	 * Application context
	 */
	protected Context mContext;

	/**
	 * Application screen manager
	 */
	protected ScreenManager mScreenManager;

	/**
	 * A list of screen components. Can be empty if no components is used.
	 */
	private List<ScreenComponent> mScreenComponents = new ArrayList<ScreenComponent>();
	
	/**
	 * Boolean telling if this screen covers the screen behind it. If set to
	 * false screenManager will first draw the screen below this one.
	 */
	private boolean mCoversWholeScreen = true;

	/**
	 * Called when the surface manager has been initialized
	 * 
	 * @param width
	 *            Width of the draw surface
	 * @param height
	 *            Height of the draw surface
	 * @param context
	 *            Application context
	 */
	protected final void initialize(int width, int height, Context context,
			ScreenManager screenManager) {
		mWidth = width;
		mHeight = height;
		mContext = context;
		mScreenManager = screenManager;

		initialize();
	}

	/**
	 * Is called when the screen is initialized
	 */
	protected void initialize() {

	}
	
	/**
	 * Is called when the screen comes on top of all screens.
	 */
	protected void onTop() {

	}

	/**
	 * Adds a screen component to the screen.
	 * 
	 * @param button
	 */
	public void addScreenComponent(ScreenComponent component) {
		synchronized (mScreenComponents) {
			if (mScreenComponents == null) {
				mScreenComponents = new ArrayList<ScreenComponent>();
			}
			mScreenComponents.add(component);
		}
	}

	/**
	 * Is called once for every motion event at the beginning of a frame
	 * 
	 * @param motionEvent
	 */
	public void handleInput(MotionEvent motionEvent) {

	}

	/**
	 * Is called once ever frame right after handleInput
	 * 
	 * @param timeStep
	 *            Time since last frame in milliseconds
	 */
	public void update(float timeStep) {

	}

	/**
	 * Is called once at the end of every frame
	 * 
	 * @param canvas
	 *            Canvas to draw to
	 */
	public void draw(Canvas canvas) {

	}
	
	/**
	 * Is called by the game activity if a dialog is to be shown.
	 * 
	 * @param id
	 *            The id for the dialog to show.
	 */
	protected Dialog onCreateDialog(int id, Dialog dialog, Bundle args) {
		return null;
	}

	/**
	 * If a screen component was touched execute the components handle method.
	 * 
	 * @param motionEvent
	 * @return true if the event was consumed.
	 */
	public boolean handleComponentInput(MotionEvent motionEvent) {
		boolean consumed = false;
		synchronized (mScreenComponents) {
			if (mScreenComponents != null) {
				for (int i = 0; i < mScreenComponents.size(); i++) {
					ScreenComponent screenComponent = mScreenComponents.get(i);
					boolean insideConponent = false;
					
					//Check if touch was inside screenComponent bounds.
					if (motionEvent.getX() > screenComponent.getPositionX() && 
							motionEvent.getX() < screenComponent.getPositionX() + screenComponent.getWidth() &&
							motionEvent.getY() > screenComponent.getPositionY() &&
							motionEvent.getY() < screenComponent.getPositionY() + screenComponent.getHeight()) {
							insideConponent = true;
					} 
					
					//If we moved finger we need to update components states for example
					//buttons need to get their untouched images if we touched a button 
					//and then moved outside of it.
					if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
						screenComponent.handleActionMove(motionEvent, insideConponent);
					}
					
					//If we released finger we need to update components states for example
					//buttons need to get their untouched images.
					if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
						boolean tempConsumed = false;
						tempConsumed = screenComponent.handleActionUp(motionEvent, insideConponent);
						if (tempConsumed) {
							consumed = true;
						}
					}
					
					//If we are inside component register the touch
					if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
						screenComponent.handleActionDown(motionEvent, insideConponent);
					}
				}
			}
		}
		return consumed;
	}

	public void drawComponents(Canvas canvas) {
		synchronized (mScreenComponents) {
			if (mScreenComponents != null) {
				for (int i = 0; i < mScreenComponents.size(); i++) {
					ScreenComponent screenComponent = mScreenComponents.get(i);
					screenComponent.draw(canvas);
				}
			}
		}
	}
	
	/**
	 * Is called once every frame
	 * 
	 * @param timeStep
	 *            Time since last frame in milliseconds
	 */
	public void updateComponents(float timeStep) {
		synchronized (mScreenComponents) {
			if (mScreenComponents != null) {
				for (int i = 0; i < mScreenComponents.size(); i++) {
					ScreenComponent screenComponent = mScreenComponents.get(i);
					screenComponent.update(timeStep);
				}
			}
		}
	}

	/**
	 * Should be overridden by subclass if needed
	 * @return
	 */
	protected boolean handleBackPressed() {
		return false;
	}

	/**
	 * @return the mWidth
	 */
	public int getWidth() {
		return mWidth;
	}

	/**
	 * @return the mHeight
	 */
	public int getHeight() {
		return mHeight;
	}

	/**
	 * @return the coversWholeScreen
	 */
	public boolean coversWholeScreen() {
		return mCoversWholeScreen;
	}

	/**
	 * @param coversWholeScreen the coversWholeScreen to set
	 */
	public void setCoversWholeScreen(boolean coversWholeScreen) {
		this.mCoversWholeScreen = coversWholeScreen;
	}

	/**
	 * Will be called when user presses menu button.
	 * Should be overridden by subclass if needed
	 */
	public void handleShowOptionsMenu() {
	}

}
