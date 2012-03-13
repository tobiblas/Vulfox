package com.vulfox.component;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

/**
 * A class representing a button. If used the user should extend it and override
 * the buttonClicked method.
 * 
 * @author tobiblas
 */
public abstract class TextButtonComponent extends ButtonComponent {

	/**
	 * State variable.
	 */
	private boolean pressed = false;

	/** The text on the button. */
	private String text;

	/** The background for the button. */
	private Bitmap background;

	/**
	 * The background for the button when it is touched.
	 */
	private Bitmap backgroundTouched;

	/** Paints. */
	private Paint textPaint;
	private Paint backgroundColor;
	private Paint backgroundTouchedColor;
	
	private int textHeightInPixels;

	private RectF backgroundRect;

	public TextButtonComponent(String text, int textColor, float textSize,
			int backgroundColor, int backgroundTouchedColor, int width,
			int height, int x, int y) {
		
		this.text = text;
		this.textPaint = new Paint();
		this.textPaint.setColor(textColor);
		this.textPaint.setAntiAlias(true);
		
		textPaint.setTextSize(textSize);
		this.textPaint.setTextAlign(Paint.Align.CENTER);
		Rect rect = new Rect();
		textPaint.getTextBounds(text, 0, text.length(), rect);
		textHeightInPixels = Math.abs(rect.top);

		backgroundRect = new RectF(x,y,x+width, y+height);
		
		this.backgroundColor = new Paint();
		this.backgroundColor.setColor(backgroundColor);
		this.backgroundColor.setAntiAlias(true);
		this.backgroundTouchedColor = new Paint();
		this.backgroundTouchedColor.setColor(backgroundTouchedColor);
		this.backgroundTouchedColor.setAntiAlias(true);
		setWidth(width);
		setHeight(height);
		setPositionX(x);
		setPositionY(y);
	}

	public TextButtonComponent(String text, int textColor, float textSize,
			Bitmap background, Bitmap backgroundTouched, int width, int height,
			int x, int y) {
		//TODO: IF WE WANT TO USE IMAGES AS BUTTON BACKGROUND IMPLEMENT THIS.
	}

	@Override
	public void draw(Canvas canvas) {
		
		if (background == null) {
			// We don't have a background image. Draw rounded rects.
			if (pressed) {
				
				canvas.drawRoundRect(backgroundRect, (float)getHeight() /4,(float)getHeight() /4, backgroundTouchedColor);
				canvas.drawText(text, getPositionX() + getWidth() / 2,
						getPositionY() + (getHeight() - textHeightInPixels)/2 + textHeightInPixels,
						textPaint);
			} else {
				canvas.drawRoundRect(backgroundRect, (float)getHeight() /4,(float)getHeight() /4, backgroundColor);
				canvas.drawText(text, getPositionX() + getWidth() / 2,
						getPositionY() + (getHeight() - textHeightInPixels)/2 + textHeightInPixels,
						textPaint);
			}
		} else {
			// TODO: draw background image.
		}
	}

}
