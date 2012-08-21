package com.vulfox.math;

public class Vector2f {

	/**
	 * X coordinate.
	 */
	private float mX;
	
	/**
	 * Y coordinate.
	 */
	private float mY;

	/**
	 * Constructs a zero vector
	 */
	public Vector2f() {
		mX = 0.0f;
		mY = 0.0f;
	}

	/**
	 * Constructs a vector from the specified x and y coordinates
	 * @param x
	 * @param y
	 */
	public Vector2f(float x, float y) {
		mX = x;
		mY = y;
	}

	/**
	 * Constructs a vector from the specified Vector2f
	 * @param other
	 */
	public Vector2f(Vector2f other) {
		mX = other.mX;
		mY = other.mY;
	}

	/**
	 * Creates a new Vector2f from this one
	 */
	public Vector2f clone() {
		return new Vector2f(mX, mY);
	}

	/**
	 * Copies the x and y coordinates from this vector to the specified Vector2f
	 * @param result Vector to copy to
	 */
	public void clone(Vector2f result) {
		result.mX = mX;
		result.mY = mY;
	}

	/**
	 * Returns the x coordinate of this vector
	 * @return x coordinate
	 */
	public float getX() {
		return mX;
	}

	/**
	 * Returns the y coordinate of this vector
	 * @return y coordinate
	 */
	public float getY() {
		return mY;
	}

	/**
	 * Sets the x coordinate of this vector
	 * @param x 
	 */
	public void setX(float x) {
		mX = x;
	}

	/**
	 * Sets the y coordinate of this vector
	 * @param y
	 */
	public void setY(float y) {
		mY = y;
	}

	/**
	 * Sets the x and y coordinates of this vector
	 * @param x
	 * @param y
	 */
	public void set(float x, float y) {
		mX = x;
		mY = y;
	}

	/**
	 * Copies the x and y coordinates from the specified Vector2f to this vector
	 * @param other
	 */
	public void set(Vector2f other) {
		mX = other.mX;
		mY = other.mY;
	}

	/**
	 * Sets the length of this vector
	 * @param length
	 */
	public void setLength(float length) {
		normalizeT();
		mulT(length);
	}

	/**
	 * Gets the length of this vector
	 * @return
	 */
	public float getLength() {
		return (float) Math.sqrt(mX * mX + mY * mY);
	}

	/**
	 * Gets the length squared of this vector
	 * @return
	 */
	public float getLengthSquared() {
		return mX * mX + mY * mY;
	}

	/**
	 * Constructs a new Vector2f from this vector and normalizes it
	 * @return A normalized new instance of this vector
	 */
	public Vector2f normalize() {
		Vector2f result = new Vector2f(this);
		result.normalizeT();
		return result;
	}

	/**
	 * Normalizes this vector in the specified Vector2f
	 * @param result Vector2f to store the result in
	 */
	public void normalize(Vector2f result) {
		result.mX = mX;
		result.mY = mY;
		result.normalizeT();
	}

	/**
	 * Normalizes this vector
	 */
	public void normalizeT() {
		float length = getLength();
		divT(length);
	}

	/**
	 * Calculates the dot product between this and another Vector2f
	 * @param other 
	 * @return The dot product
	 */
	public float dot(Vector2f other) {
		return mX * other.mX + mY * other.mY;
	}

	/**
	 * Constructs a new inverted Vector2f from this one 
	 * @return
	 */
	public Vector2f inv() {
		return new Vector2f(-mX, -mY);
	}

	/**
	 * Inverts this vector and stores it in the specified Vector2f
	 * @param result
	 */
	public void inv(Vector2f result) {
		result.mX = -mX;
		result.mY = -mY;
	}

	/**
	 * Inverts this vector
	 */
	public void invT() {
		mX = -mX;
		mY = -mY;
	}

	/**
	 * Constructs a new Vector2f from this vector and multiplies it with the specified scalar 
	 * @param scalar
	 * @return
	 */
	public Vector2f mul(float scalar) {
		return new Vector2f(mX * scalar, mY * scalar);
	}

	/**
	 * Multiplies this vector with the specified scalar and stores it in the specified Vector2f
	 * @param scalar
	 * @param result
	 */
	public void mul(float scalar, Vector2f result) {
		result.mX = mX * scalar;
		result.mY = mY * scalar;
	}

	/**
	 * Multiplies this vector with the specified scalar
	 * @param scalar
	 */
	public void mulT(float scalar) {
		mX *= scalar;
		mY *= scalar;
	}

	/**
	 * Constructs a new Vector2f from this vector and divides it by the specified scalar 
	 * @param scalar
	 * @return
	 */
	public Vector2f div(float scalar) {
		if (scalar == 0.0) {
			return new Vector2f(Float.MAX_VALUE, Float.MAX_VALUE);
		}

		float inv = 1.0f / scalar;
		return new Vector2f(mX * inv, mY * inv);
	}

	/**
	 * Divides this vector by the specified scalar and stores it in the specified Vector2f
	 * @param scalar
	 * @param result
	 */
	public void div(float scalar, Vector2f result) {
		if (scalar == 0.0) {
			result.mX = Float.MAX_VALUE;
			result.mY = Float.MAX_VALUE;
		} else {
			float inv = 1.0f / scalar;
			result.mX = mX * inv;
			result.mY = mY * inv;
		}
	}

	/**
	 * Divides this vector by the specified scalar
	 * @param scalar
	 */
	public void divT(float scalar) {
		if (scalar == 0.0)
			return;

		mX /= scalar;
		mY /= scalar;
	}

	/**
	 * Constructs a new Vector2f from this vector and adds the specified Vector2f.
	 * @param other Vector2f to add to this vector.
	 * @return Resulting Vector2f from the addition.
	 */
	public Vector2f add(Vector2f other) {
		return new Vector2f(mX + other.mX, mY + other.mY);
	}

	/**
	 * Adds the specified Vector2f to this vector and stores the result in the specified Vector2f
	 * @param other Vector2f to add to this vector.
	 * @param result
	 */
	public void add(Vector2f other, Vector2f result) {
		result.mX = mX + other.mX;
		result.mY = mY + other.mY;
	}

	/**
	 * Adds the specified Vector2f to this vector
	 * @param other
	 */
	public void addT(Vector2f other) {
		mX += other.mX;
		mY += other.mY;
	}

	/**
	 * Constructs a new Vector2f from this vector and subtracts the specified Vector2f. 
	 * @param other Vector2f to subtract from this vector.
	 * @return Resulting Vector2f from the subtraction.
	 */
	public Vector2f sub(Vector2f other) {
		return new Vector2f(mX - other.mX, mY - other.mY);
	}

	/**
	 * Subtracts the specified Vector2f from this vector and stores the result in the specified Vector2f
	 * @param other
	 * @param result
	 */
	public void sub(Vector2f other, Vector2f result) {
		result.mX = mX - other.mX;
		result.mY = mY - other.mY;
	}

	/**
	 * Subtracts the specified Vector2f from this vector
	 * @param other
	 */
	public void subT(Vector2f other) {
		mX -= other.mX;
		mY -= other.mY;
	}

	/**
	 * Constructs a Vector2f that is perpendicular to this one.
	 * The new Vector2f has the same length as this one.
	 * @return
	 */
	public Vector2f perp() {
		return new Vector2f(mY, -mX);
	}
	
	/**
	 * Sets the specified Vector2f to a Vector2f perpendicular to this one
	 * @param result
	 */
	public void perp(Vector2f result) {
		result.mX = mY;
		result.mY = -mX;
	}

	/**
	 * Makes this vector perpendicular to its old state
	 */
	public void perpT() {
		float temp = mX;
		mX = mY;
		mY = -temp;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Vector2f [mX=" + mX + ", mY=" + mY + "]";
	}
}
