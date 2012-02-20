package com.vulfox.util;

import java.util.Stack;

/**
 * Extend this class to make a pool manager
 * You will have to handle releases of the object manually
 * @param <E> Class to create a pool from
 */
public abstract class ObjectPool<E> {

	/**
	 * Holds the released objects
	 */
	private Stack<E> mPool = new Stack<E>();

	/**
	 * Override to create a new instance of the pool object class
	 * @return A new instance of E
	 */
	protected abstract E create();

	/**
	 * Override to reset an instance of a pool object class
	 * @param poolObject Pool object to reset
	 */
	protected abstract void reset(E poolObject);

	/**
	 * Get an instance of the pool object class
	 * @return An instance of the pool object class
	 */
	public E aquire() {
		if (mPool.isEmpty()) {
			return create();
		}
		return mPool.pop();
	}

	/**
	 * Release an instance from the pool object class for later reuse
	 * @param poolObject An instance from the pool object class that is no longer in use
	 */
	public void release(E poolObject) {
		reset(poolObject);
		mPool.push(poolObject);
	}
	
}
