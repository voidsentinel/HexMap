/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author    VoidSentinel
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation;

import org.voidsentinel.hexmap.utils.FastNoise;

/**
 * @author Xerces
 *
 */
public class SimplexGeneration extends AbstractTerrainGenerator {

	float scale = 0f;

	/**
	 * Constructor
	 * @param scale
	 *           used to scale the coordinates in the simplex noise. Higher values
	 *           give flatter map, since the values between 2 points change more
	 *           slowly
	 */
	public SimplexGeneration(float scale) {
		this.scale = scale;
	}

	/**
	 * will generate a heightmap based on a simplex value. Each cell of the table
	 * get the simplex value at it's coordinate scaled by the parameter from
	 * constructor.
	 * 
	 * @param xSize
	 *           the xsize of the map to create
	 * @param ySize
	 *           the ysize of the map to create
	 * @return a float table of size [ySize][xSize]
	 */
	public float[][] generate(int xSize, int ySize) {
		LOG.info("   Operation : " + SimplexGeneration.class.getSimpleName());

		FastNoise noise = new FastNoise();

		float[][] copy = new float[ySize][xSize];

		for (int y = 0; y < ySize; y++) {
			for (int x = 0; x < xSize; x++) {
				copy[y][x] = noise.GetSimplex((float) (x) / scale, (float) (y) / scale);
			}
		}
		this.normalize(copy);
		return copy;
	}

}
