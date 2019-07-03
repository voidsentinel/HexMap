/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author    VoidSentinel
 */
package org.voidsentinel.hexmap.utils;

import java.util.Random;

/**
 * a static random generator
 *
 */
public class Alea {
	private static Random	rnd			= new Random(111);
	private static long		seed			= 111L;
	private static Long		callCount	= 0L;

	/**
	 * @return
	 * @see java.util.Random#nextBoolean()
	 */
	static final public boolean nextBoolean() {
		callCount++;
		return rnd.nextBoolean();
	}

	/**
	 * @return
	 * @see java.util.Random#nextFloat()
	 */
	static final public float nextFloat() {
		callCount++;
		return rnd.nextFloat();
	}

	/**
	 * @return
	 * @see java.util.Random#nextInt()
	 */
	static final public int nextInt() {
		callCount++;
		return rnd.nextInt();
	}

	/**
	 * @return
	 * @see java.util.Random#nextInt(int)
	 */
	static final public int nextInt(int max) {
		callCount++;
		return rnd.nextInt(max);
	}

	/**
	 * @return
	 * @see java.util.Random#nextLong()
	 */
	static final public long nextLong() {
		callCount++;
		return rnd.nextLong();
	}

	/**
	 * @param arg0
	 * @see java.util.Random#setSeed(long)
	 */
	static final public void setSeed(long arg0) {
		rnd.setSeed(arg0);
		callCount = 0L;
	}

	/**
	 */
	static final public long getSeed(long arg0) {
		return seed;
	}

	/**
	 */
	static final public long getCallNumber(long arg0) {
		return callCount;
	}

	/**
	 * @param arg0
	 * @see java.util.Random#setSeed(long)
	 */
	static final public void setSeed(long arg0, long calls) {
		rnd.setSeed(arg0);
		for (long i = 0L; i < calls; i++) {
			rnd.nextInt();
		}
		callCount = calls;
	}

	/**
	 * @return
	 * @see java.util.Random#nextDouble()
	 */
	static final public double nextDouble() {
		return rnd.nextDouble();
	}

	
}
