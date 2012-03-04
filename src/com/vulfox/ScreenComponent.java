package com.vulfox;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Represents a component that can be present on a screen for example a button.
 * 
 * @author tobiblas
 */
abstract class ScreenComponent {

	/** The X position in the screen. */
	private int positionX;

	/** The Y position in the screen. */
	private int positionY;

	/** The width of the component. */
	private int width;

	/** The height of the component. */
	private int height;
	
	/**
	 * Draws this component
	 * @param canvas
	 */
	abstract void draw(Canvas canvas);

	/**
	 * Called when someone touches a component
	 * @param insideConponent
	 * @param motionEvent
	 */
	abstract void handleActionDown(MotionEvent motionEvent, boolean insideConponent);

	/**
	 * 	Called when someone releases a component
	 * @param insideConponent
	 * @param motionEvent
	 * @return true if event was consumed.
	 */
	abstract boolean handleActionUp(MotionEvent motionEvent, boolean insideConponent);
	
	/**
	 * Called when someone moves finger during touch.
	 * @param insideConponent
	 * @param motionEvent
	 * @param insideConponent 
	 */
	abstract void handleActionMove(MotionEvent motionEvent, boolean insideConponent);
	
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

}
