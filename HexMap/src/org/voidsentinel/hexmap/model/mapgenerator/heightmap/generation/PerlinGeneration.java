/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author    VoidSentinel
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation;

import org.voidsentinel.hexmap.utils.FastNoise;

/**
 * a simplex generator that for any cell at coordinate x, z use the simplex
 * value at (x/xscale + xoffset, z/zscale + zoffset), the values comming from
 * the constructor
 * 
 * @author VoidSentinel
 *
 */
public class PerlinGeneration extends AbstractTerrainGenerator {

	float	xscale	= 0f;
	float	zscale	= 0f;
	float	xoffset	= 0f;
	float	zoffset	= 0f;

	/**
	 * Constructor
	 * 
	 * @param scale
	 *           used to scale the coordinates by multipliing it in the simplex
	 *           noise. Higher values give flatter map, since the values between 2
	 *           points change more slowly
	 */
	public PerlinGeneration(float xscale, float zscale, float xoffset, float zoffset) {
		this.xscale = xscale;
		this.zscale = zscale;
		this.xoffset = xoffset;
		this.zoffset = zoffset;

	}

	/**
	 * Constructor
	 * 
	 * @param scale
	 *           used to scale the coordinates by multipliing it in the simplex
	 *           noise. Higher values give flatter map, since the values between 2
	 *           points change more slowly
	 */
	public PerlinGeneration(float xscale, float zscale) {
		this(xscale, zscale, 0f, 0f);
	}

	/**
	 * Constructor
	 * 
	 * @param scale
	 *           used to scale the coordinates by multipliing it in the simplex
	 *           noise. Higher values give flatter map, since the values between 2
	 *           points change more slowly
	 */
	public PerlinGeneration(float scale) {
		this(scale, scale, 0f, 0f);
	}

	/**
	 * will generate a heightmap based on a simplex value. Each cell of the table
	 * get the simplex value at it's coordinate scaled by the parameter from
	 * constructor.
	 * 
	 * @param xSize
	 *           the xsize of the map to create
	 * @param zSize
	 *           the ysize of the map to create
	 * @return a float table of size [zSize][xSize]
	 */
	public float[][] generate(int xSize, int zSize) {
		LOG.info("   Operation : " + PerlinGeneration.class.getSimpleName());

		float xpos = 0f;
		float zpos = 0f;
		FastNoise noise = new FastNoise();
		float[][] copy = new float[zSize][xSize];
		for (int z = 0; z < zSize; z++) {
			zpos = z * zscale + zoffset;
			for (int x = 0; x < xSize; x++) {
				xpos = x * xscale + xoffset;
				copy[z][x] = noise.GetPerlin(xpos, zpos);
			}
		}
		this.normalize(copy);
		return copy;
	}

}
