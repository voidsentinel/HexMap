/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author    VoidSentinel
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation;

import org.voidsentinel.hexmap.utils.FastNoise;
import org.voidsentinel.hexmap.utils.FastNoise.CellularDistanceFunction;
import org.voidsentinel.hexmap.utils.FastNoise.CellularReturnType;
import org.voidsentinel.hexmap.utils.FastNoise.NoiseType;

/**
 * @author Xerces
 *
 */
public class CellularGeneration extends AbstractTerrainGenerator {

	float	xscale	= 0f;
	float	zscale	= 0f;
	float	xoffset	= 0f;
	float	zoffset	= 0f;

	/**
	 * Constructor
	 * 
	 * @param scale used to scale the coordinates by multipliing it in the simplex
	 *              noise. Higher values give flatter map, since the values between
	 *              2 points change more slowly
	 */
	public CellularGeneration(float xscale, float zscale, float xoffset, float zoffset) {
		this.xscale = xscale;
		this.zscale = zscale;
		this.xoffset = xoffset;
		this.zoffset = zoffset;

	}

	/**
	 * Constructor
	 * 
	 * @param scale used to scale the coordinates by multipliing it in the simplex
	 *              noise. Higher values give flatter map, since the values between
	 *              2 points change more slowly
	 */
	public CellularGeneration(float xscale, float zscale) {
		this(xscale, zscale, 0f, 0f);
	}

	/**
	 * Constructor
	 * 
	 * @param scale used to scale the coordinates by multipliing it in the simplex
	 *              noise. Higher values give flatter map, since the values between
	 *              2 points change more slowly
	 */
	public CellularGeneration(float scale) {
		this(scale, scale, 0f, 0f);
	}

	public float[][] generate(int xSize, int ySize) {
		LOG.info("   Operation : " + CellularGeneration.class.getSimpleName());

		FastNoise noise = new FastNoise();
		noise.SetNoiseType(NoiseType.Cellular);
		noise.SetCellularDistanceFunction(CellularDistanceFunction.Euclidean);
		noise.SetCellularReturnType(CellularReturnType.Distance2Sub);

		float[][] copy = new float[ySize][xSize];

		for (int y = 0; y < ySize; y++) {
			for (int x = 0; x < xSize; x++) {
				copy[y][x] = (noise.GetNoise(x / xscale + xoffset, y / zscale + zoffset) + 1f) / 2f;
			}
		}
		this.normalize(copy);

		return copy;
	}

}
