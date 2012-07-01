package com.vulfox.component;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public class TextComponent extends ScreenComponent {

	private String mText;
	
	private Paint mPaint;
	
	public TextComponent(String text, int color, int x, int y, int textSize) {
		mText = text;
		
		mPaint = new Paint();
		mPaint.setColor(color);
		
		setPositionX(x);
		setPositionY(y);
		
		mPaint.setTextSize(textSize);
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawText(mText, getPositionX(), getPositionY(), mPaint);
	}

	@Override
	public void update(float timeStep) {
	}

	@Override
	public void handleActionDown(MotionEvent motionEvent,
			boolean insideConponent) {
	}

	@Override
	public boolean handleActionUp(MotionEvent motionEvent,
			boolean insideConponent) {
		return false;
	}

	@Override
	public void handleActionMove(MotionEvent motionEvent,
			boolean insideConponent) {
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.mText = text;
	}

}
