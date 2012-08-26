package com.vulfox.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.vulfox.ImageLoader;

/**
 * A class representing a streachable button.
 * 
 * @author tobiblas
 */
public class StretchableImageButtonComponent extends ButtonComponent {

	/** The button bitmaps. */
	private Bitmap mBackground;
	private Bitmap mBackgroundPressed;

	/** The text on the button. */
	private String mText;

	/** Paints. */
	private Paint mTextPaint;
	private Paint mTextPaintShadow;
	
	/** Text rect. */
	private Rect mTextRect;
	
	/** Used if no image pressed is supplied. */
	private Paint mTintPaint = new Paint();


	/**
	 * Copies the graphics from another image button. Use this is you need the exact same button as another one
	 * and want to save memory.
	 */
	public StretchableImageButtonComponent(StretchableImageButtonComponent other) {

		this.mBackground = other.mBackground;
		this.mBackgroundPressed = other.mBackgroundPressed;

		this.mText = other.mText;
		this.mTextPaint = other.mTextPaint;
		this.mTextPaintShadow = other.mTextPaintShadow;
		this.mTextRect = other.mTextRect;

		setHeight(other.getHeight());
		setWidth(other.getWidth());
		
		ColorFilter filter = new LightingColorFilter(0x11cccccc, 1);
		mTintPaint.setColorFilter(filter);
	}
	
	/**
	 * Loads images and constructs a strechable button. Consider not calling
	 * from UI thread since loading images might take time.
	 */
	public StretchableImageButtonComponent(Context context, int background,
			int backgroundPressed, String text, int textColor,
			int textShadowColor, float textSizeDp, int widthDp, int heightDp,
			int dpi, float scale) {

		this.mBackground = ImageLoader.loadFromResource(context, background);
		this.mBackgroundPressed = ImageLoader.loadFromResource(context,
				backgroundPressed);

		this.mText = text;
		this.mTextPaint = new Paint();
		this.mTextPaint.setColor(textColor);
		this.mTextPaint.setAntiAlias(true);
		this.mTextPaint.setTextSize((int) (textSizeDp * (dpi / 160.0f)));
		this.mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
		
		this.mTextPaintShadow = new Paint(mTextPaint);
		this.mTextPaintShadow.setColor(textShadowColor);
		this.mTextPaintShadow.setStyle(Paint.Style.STROKE);
		this.mTextPaintShadow.setStrokeWidth(Math.round(3 * scale));
		
		this.mTextRect = new Rect();
		this.mTextPaint.getTextBounds(mText, 0, mText.length(), mTextRect);

		setWidthAndHeightInDp(widthDp, heightDp, dpi);

		try {
			mBackground = resizeBitmap(mBackground);
		} catch (OutOfMemoryError oomE1) {
			System.gc();
			try {
				mBackground = resizeBitmap(mBackground);
			} catch (OutOfMemoryError oomE2) {
				// No memory. Skip it!
				return;
			}
		}

		try {
			mBackgroundPressed = resizeBitmap(mBackgroundPressed);
		} catch (OutOfMemoryError oomE1) {
			System.gc();
			try {
				mBackgroundPressed = resizeBitmap(mBackgroundPressed);
			} catch (OutOfMemoryError oomE2) {
				// No memory. Skip it!
				return;
			}
		}
	}
	
	/**
	 * Loads images and constructs a strechable button. Consider not calling
	 * from UI thread since loading images might take time.
     * Will use a colorfilter on the background image when pressed.
	 */
	public StretchableImageButtonComponent(Context context, int background,
			String text, int textColor, int textShadowColor, float textSizeDp, 
			int widthDp, int heightDp, int dpi, float scale) {

		this.mBackground = ImageLoader.loadFromResource(context, background);

		this.mText = text;
		this.mTextPaint = new Paint();
		this.mTextPaint.setColor(textColor);
		this.mTextPaint.setAntiAlias(true);
		this.mTextPaint.setTextSize((int) (textSizeDp * (dpi / 160.0f)));
		this.mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
		this.mTextRect = new Rect();
		this.mTextPaint.getTextBounds(mText, 0, mText.length(), mTextRect);
		
		this.mTextPaintShadow = new Paint(mTextPaint);
		this.mTextPaintShadow.setColor(textShadowColor);
		this.mTextPaintShadow.setStyle(Paint.Style.STROKE);
		this.mTextPaintShadow.setStrokeWidth(Math.round(3 * scale));

		setWidthAndHeightInDp(widthDp, heightDp, dpi);

		try {
			mBackground = resizeBitmap(mBackground);
		} catch (OutOfMemoryError oomE1) {
			System.gc();
			try {
				mBackground = resizeBitmap(mBackground);
			} catch (OutOfMemoryError oomE2) {
				// No memory. Skip it!
				return;
			}
		}
		
		ColorFilter filter = new LightingColorFilter(0x11cccccc, 1);
		mTintPaint.setColorFilter(filter);
	}

	/**
	 * Resize the bitmap. 1. Scale bitmap so that height is correct and scale
	 * width as well to preserve aspect ratio. 2. Either add or remove stripes
	 * in the middle to get the correct width.
	 */
	private Bitmap resizeBitmap(Bitmap bitmap) {

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float scaleHeight = ((float) getHeight()) / height;

		// keep the aspect ratio for now. i.e. set width to appropriate value.
		Matrix matrix = new Matrix();
		matrix.postScale(scaleHeight, scaleHeight);

		// This is a bitmap scaled so that height is correct. Now the width must
		// be adjusted.
		Bitmap heightScaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
				height, matrix, true);

		Bitmap result = null;

		if (heightScaledBitmap.getWidth() > getWidth()) {

			// The bitmap is too wide. Drop some pixels in the middle.
			result = Bitmap.createBitmap(getWidth(), getHeight(),
					Bitmap.Config.ARGB_8888);

			Canvas canvas = new Canvas(result);
			Rect sourceRect = new Rect();
			Rect destinationRect = new Rect();

			// Draw the left part of the button.
			sourceRect
					.set(0, 0, getWidth() / 2, heightScaledBitmap.getHeight());
			destinationRect
					.set(0, 0, result.getWidth() / 2, result.getHeight());
			canvas.drawBitmap(heightScaledBitmap, sourceRect, destinationRect,
					null);

			// Draw the right part of the button.
			// Add one pixel to avoid gap if width is uneven.
			int extraPixel = result.getWidth() % 2 == 0 ? 0 : 1;
			sourceRect.set(heightScaledBitmap.getWidth() - getWidth() / 2
					- extraPixel, 0, heightScaledBitmap.getWidth(),
					heightScaledBitmap.getHeight());
			destinationRect.set(
					result.getWidth() - getWidth() / 2 - extraPixel, 0,
					result.getWidth(), result.getHeight());
			canvas.drawBitmap(heightScaledBitmap, sourceRect, destinationRect,
					null);

		} else if (heightScaledBitmap.getWidth() < getWidth()) {

			// The bitmap is too short. Add some pixels in the middle.

			int middleX = heightScaledBitmap.getWidth() / 2;

			// Middle stripe of the height scaled bitmap.
			Bitmap middleStripe = Bitmap.createBitmap(heightScaledBitmap,
					middleX, 0, 1, heightScaledBitmap.getHeight());

			result = Bitmap.createBitmap(getWidth(), getHeight(),
					Bitmap.Config.ARGB_8888);

			Canvas canvas = new Canvas(result);
			Rect sourceRect = new Rect();
			Rect destinationRect = new Rect();

			// Draw the left part of the button.
			sourceRect.set(0, 0, heightScaledBitmap.getWidth() / 2,
					heightScaledBitmap.getHeight());
			destinationRect.set(0, 0, heightScaledBitmap.getWidth() / 2,
					heightScaledBitmap.getHeight());
			canvas.drawBitmap(heightScaledBitmap, sourceRect, destinationRect,
					null);

			// Draw the right part of the button.
			sourceRect.set(
					heightScaledBitmap.getWidth()
							- heightScaledBitmap.getWidth() / 2, 0,
					heightScaledBitmap.getWidth(),
					heightScaledBitmap.getHeight());
			destinationRect.set(
					result.getWidth() - heightScaledBitmap.getWidth() / 2, 0,
					result.getWidth(), result.getHeight());
			canvas.drawBitmap(heightScaledBitmap, sourceRect, destinationRect,
					null);

			// Draw the middle part of the button.
			sourceRect.set(0, 0, 1, middleStripe.getHeight());
			destinationRect.set(heightScaledBitmap.getWidth() / 2, 0,
					result.getWidth() - heightScaledBitmap.getWidth() / 2,
					result.getHeight());
			canvas.drawBitmap(middleStripe, sourceRect, destinationRect, null);

		} else {
			// The bitmap is already perfect. Return it!
			result = heightScaledBitmap;
		}

		return result;
	}

	@Override
	public void draw(Canvas canvas) {

		if (!isVisible()) {
			return;
		}
		
		//Draw button image.
		if (mBackground != null && !mPressed) {
			canvas.drawBitmap(mBackground, getPositionX(), getPositionY(), null);
		} else if (mBackgroundPressed != null && mPressed) {
			canvas.drawBitmap(mBackgroundPressed, getPositionX(),
					getPositionY(), null);
		} else if (mBackgroundPressed == null && mPressed) {
			canvas.drawBitmap(mBackground, getPositionX(),
					getPositionY(), mTintPaint);
		}
		
		
		float glyphAdjuster = 0.0f;
		if (hasGlyph(mText)) {
			glyphAdjuster = mTextPaint.getFontMetrics().bottom / 2;
		}
		
		// Draw shadow text
		canvas.drawText(mText, getPositionX() + getWidth() / 2 - mTextRect.width()
				/ 2, getPositionY() + getHeight() / 2 + mTextRect.height() / 2
				- glyphAdjuster, mTextPaintShadow);

		// Draw text
		canvas.drawText(mText, getPositionX() + getWidth() / 2 - mTextRect.width()
				/ 2, getPositionY() + getHeight() / 2 + mTextRect.height() / 2
				- glyphAdjuster, mTextPaint);

		
	}

	private boolean hasGlyph(String text) {
		
		if (text.contains("y")) {
			return true;
		}
		if (text.contains("q")) {
			return true;
		}
		if (text.contains("p")) {
			return true;
		}
		if (text.contains("g")) {
			return true;
		}
		if (text.contains("j")) {
			return true;
		}
		
		return false;
	}

	/**
	 * @return the mBackground
	 */
	public Bitmap getBitmap() {
		return mBackground;
	}

	@Override
	public void update(float timeStep) {
	}

}
