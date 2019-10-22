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

	static final FastNoise fn = new FastNoise(Alea.nextInt());

	private final static float[] xcoeff = new float[] {11f, 23f, 370f};
	private final static float[] ycoeff = new float[] {17f, 31f, 410f};
	
	/**
	 * Return the perturbation of a given vertex.
	 * 
	 * @param v1 the vertex whose pertubation is requested
	 * @return the perturbation to be added
	 */
	public static Vector3f getPerturbation(Vector3f v1) {
		return getPerturbation(v1, 1f);
	}

	public static Vector3f getPerturbation(Vector3f v1, float coefficient, int band) {
		band = band % xcoeff.length;
		float x = (float) Math.floor(v1.x * 100d) / 10f;
		float z = (float) Math.floor(v1.z * 100d) / 10f;
		final float VARIATION = HexMetrics.INNERRADIUS * 0.75f / 2f;
		float o1 = (fn.GetPerlin(x * xcoeff[band], z * ycoeff[band])) * VARIATION;
		float o2 = (fn.GetPerlin(z * xcoeff[band], x * ycoeff[band])) * VARIATION;
		return new Vector3f(o1, 0f, o2).multLocal(coefficient);
	}

	public static Vector3f getPerturbation(Vector3f v1, float coefficient) {
		return getPerturbation(v1, coefficient, 0);
	}
	
	/**
	 * Apply the perturbatio to the given vertice. Equiavalent to
	 * v1.addLocal(getPerturbation(v1))
	 * 
	 * @param v1 the vertice to perturbate
	 */
	public static void perturbate(Vector3f v1) {
		v1.addLocal(getPerturbation(v1, 1f, 0));
	}

	/**
	 * Apply the perturbatio to the given vertice. Equilavalent to
	 * v1.addLocal(getPerturbation(v1))
	 * 
	 * @param v1           the vertice to perturbate
	 * @param acoefficient a value to scale amplitude
	 */
	public static void perturbate(Vector3f v1, float coefficient) {
		v1.addLocal(getPerturbation(v1, coefficient, 0));
	}

	public static void perturbate(Vector3f v1, float coefficient, int band) {
		v1.addLocal(getPerturbation(v1, coefficient, band));
	}
	
}
