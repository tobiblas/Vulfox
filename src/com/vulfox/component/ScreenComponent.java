package com.vulfox.component;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Represents a component that can be present on a screen for example a button.
 * 
 * @author tobiblas
 */
public abstract class ScreenComponent {

	/** The X position in the screen. */
	private int positionX;

	/** The Y position in the screen. */
	private int positionY;

	/** The width of the component. */
	private int width;

	/** The height of the component. */
	private int height;
	
	/** Boolean for visibility. */
	private boolean mVisible = true;
	
	/**
	 * Draws this component
	 * @param canvas
	 */
	public abstract void draw(Canvas canvas);

	/**
	 * Called when someone touches a component
	 * @param insideConponent
	 * @param motionEvent
	 */
	public abstract void handleActionDown(MotionEvent motionEvent, boolean insideConponent);

	/**
	 * 	Called when someone releases a component
	 * @param insideConponent
	 * @param motionEvent
	 * @return true if event was consumed.
	 */
	public abstract boolean handleActionUp(MotionEvent motionEvent, boolean insideConponent);
	
	/**
	 * Called when someone moves finger during touch.
	 * @param insideConponent
	 * @param motionEvent
	 * @param insideConponent 
	 */
	public abstract void handleActionMove(MotionEvent motionEvent, boolean insideConponent);
	
	/**
	 * @return the positionX
	 */
	public int getPositionX() {
		return positionX;
	}

	/**
	 * @param positionX
	 *            the positionX to set
	 */
	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}
	
	/**
	 * @param positionX
	 *            the positionX to set
	 */
	public void setPositionXInDp(int positionXinDp, int deviceDpi) {
		float fraction = deviceDpi / 160.0f;
		this.positionX = (int)(positionXinDp * (fraction));
	}

	/**
	 * @return the positionY
	 */
	public int getPositionY() {
		return positionY;
	}

	/**
	 * @param positionY
	 *            the positionY to set
	 */
	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}
	
	/**
	 * @param positionY
	 *            the positionY to set
	 */
	public void setPositionYInDp(int positionYinDp, int deviceDpi) {
		float fraction = deviceDpi / 160.0f;
		this.positionY = (int)(positionYinDp * (fraction));
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
	/**
	 * @param widthDp Density-independent pixel
	 * @param heightDp Density-independent pixel
	 * @param deviceDpi dots per inch for the device. 
	 */
	public void setWidthAndHeightInDp(int widthDp, int heightDp, int deviceDpi) {
		float fraction = deviceDpi / 160.0f;
		this.height = (int)(heightDp * (fraction));
		this.width = (int)(widthDp * (fraction));
	}

	/**
	 * @return the mVisible
	 */
	public boolean isVisible() {
		return mVisible;
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		this.mVisible = visible;
	}

}
