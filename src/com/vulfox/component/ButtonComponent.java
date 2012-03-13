package com.vulfox.component;

import android.view.MotionEvent;

public abstract class ButtonComponent extends ScreenComponent {

	protected boolean mPressed = false;
	
	@Override
	public void handleActionDown(MotionEvent motionEvent, boolean insideConponent) {
		if (insideConponent) {
			mPressed = true;
		} else {
			mPressed = false;
		}
	}

	@Override
	public boolean handleActionUp(MotionEvent motionEvent, boolean insideConponent) {
		boolean consumed = false;
		if (mPressed) {
			if (insideConponent) {
				buttonClicked();
				consumed = true;
			} 
			mPressed = false;
		}

		return consumed;
		
	}

	@Override
	public void handleActionMove(MotionEvent motionEvent, boolean insideConponent) {
		if (!insideConponent && mPressed) {
			mPressed = false;
		}
	}

	/**
	 * Called when a button is clicked. Should be overridden by subclasses.
	 */
	public abstract void buttonClicked();
	
}
