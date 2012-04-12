package com.vulfox.component;

import android.view.MotionEvent;

import com.vulfox.listener.EventListener;

public abstract class ButtonComponent extends ScreenComponent {

	protected boolean mPressed = false;
	
	private EventListener listener;
	
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
				consumed = buttonClicked();
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
	
	public void setEventListener(EventListener listener) {
		this.listener = listener;
	}

	/**
	 * Called when a button is clicked.
	 */
	public final boolean buttonClicked() {
		boolean consumed = false;
		if (listener != null) {
			consumed = listener.handleButtonClicked();
		}
		return consumed;
	}
	
}
