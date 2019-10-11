/**
 * 
 */
package org.voidsentinel.hexmap.utils;

import org.voidsentinel.hexmap.view.HexMetrics;

import com.jme3.math.Vector3f;

/**
 * @author guipatry
 *
 */
public class Perturbator {

	static final FastNoise			fn							= new FastNoise(Alea.nextInt());

	
	/**
	 * Return the perturbation of a given vertex.
	 * 
	 * @param v1 the vertex whose pertubation is requested
	 * @return the perturbation to be added
	 */
	public static Vector3f getPerturbation(Vector3f v1) {
		return getPerturbation(v1, 1f);
	}

	public static Vector3f getPerturbation(Vector3f v1, float coefficient) {
		final float VARIATION = HexMetrics.INNERRADIUS * 0.75f;
		float o1 = Math.abs(fn.GetPerlin(v1.x * 11, v1.z * 17)) * VARIATION - 0.5f * VARIATION;
		float o2 = Math.abs(fn.GetPerlin(v1.z * 11, v1.x * 17)) * VARIATION - 0.5f * VARIATION;
		return new Vector3f(o1, 0f, o2).multLocal(coefficient);
	}

	/**
	 * Apply the perturbatio to the given vertice. Equilavalent to
	 * v1.addLocal(getPerturbation(v1))
	 * 
	 * @param v1 the vertice to perturbate
	 */
	public static void perturbate(Vector3f v1) {
		v1.addLocal(getPerturbation(v1, 1f));
	}

	/**
	 * Apply the perturbatio to the given vertice. Equilavalent to
	 * v1.addLocal(getPerturbation(v1))
	 * 
	 * @param v1 the vertice to perturbate
	 */
	public static void perturbate(Vector3f v1, float coefficient) {
		v1.addLocal(getPerturbation(v1, coefficient));
	}
	
}
