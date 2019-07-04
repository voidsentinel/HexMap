/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap;

import org.voidsentinel.hexmap.utils.Alea;
import org.voidsentinel.hexmap.utils.ImprovedNoise;

/**
 * This Generator create a map by placing 0.1 to a random point, moving one
 * square and doing it again a given number of time. As for all generators, the
 * (normalized) final value is added to the initial table
 * 
 * @author voidSentinel
 *
 */
public class FBMGeneration extends AbstractTerrainGenerator {

	private double		delta	= 16;	// scales down all
	                              // layers,
	                              // probably would be
	                              // better in
	                              // the constructor
	public int			octaves;		// number of raw Perlin
	                              // noise
	                              // samples
	private double[]	offset;		// picks the z-value for
	                              // each
	                              // layer
	private double[]	cos;			// cosine table for
	                              // rotating
	                              // the noise layers
	private double[]	sin;
	private double[]	amplitude;	// how prominent each
	                              // layer is
	private double[]	frequency;	// scale of each layer

	private int			scale	= 1;

	public FBMGeneration(int octaves) {
		this(octaves, .5, 10);
	}

	public FBMGeneration(int octaves, double persistence, int scale) { // .5 is a good value for persistence. Watch out
		// for very high octaves too.
		this.octaves = octaves;
		this.scale = scale;

		offset = new double[octaves];
		cos = new double[octaves];
		sin = new double[octaves];
		amplitude = new double[octaves];
		this.frequency = new double[octaves];
		for (int oc = 0; oc < octaves; oc++) { // for every octave/layer
			double angle = Alea.nextDouble() * 360; // I like degrees. Okay?
			cos[oc] = Math.cos(Math.toRadians(angle));
			sin[oc] = Math.sin(Math.toRadians(angle));

			offset[oc] = Alea.nextDouble() * 256;

			frequency[oc] = Math.pow(2, oc);
			amplitude[oc] = Math.pow(persistence, oc);
		}

	}

	public double noise(double x, double y) {
		double total = 0;
		for (int i = 0; i < this.octaves; i++) { // for all layers
			double xV = x * cos[i] + y * -sin[i]; // rotated value of x
			double yV = x * sin[i] + y * cos[i]; // rotated value of y
			double val = (ImprovedNoise.noise(xV * frequency[i] / delta, yV * frequency[i] / delta, offset[i]));
			val = (val * amplitude[i]); // amplitude decreases with every octave
			total += val;
		}
		// WARNING: PERLIN NOISE DOESN'T GO BETWEEN -1,1 NORMALLY.
		// THIS RETURNS /APPROXIMATELY/ 0 to 1 (can be a bit negative, can go a bit
		// above 1)
		return (total + 1) / 2; // tries to get it from 0 to 1
	}

	public float[][] generate(float[][] height) {
		LOG.info("   Operation : " + this.getClass().getSimpleName());

		float[][] copy = new float[height.length][height[0].length];
		for (int y = 0; y < height.length; y++) {
			for (int x = 0; x < height[0].length; x++) {
				copy[y][x] = (float) (noise(x / scale, y / scale));
			}
		}

		this.normalize(copy);
		for (int y = 0; y < height.length; y++) {
			for (int x = 0; x < height[0].length; x++) {
				height[y][x] += copy[y][x];
			}
		}

		return copy;
	}

}
