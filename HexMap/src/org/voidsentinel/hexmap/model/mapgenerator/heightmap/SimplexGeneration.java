/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author    VoidSentinel
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap;

import org.voidsentinel.hexmap.utils.FastNoise;
import org.voidsentinel.hexmap.utils.OpenSimplexNoise;

/**
 * @author Xerces
 *
 */
public class SimplexGeneration extends AbstractTerrainGenerator {

	float									scale	= 0f;

	public SimplexGeneration(float scale) {
		this.scale = scale;
	}

	public float[][] generate(float[][] height) {
		LOG.info("   Operation : " + SimplexGeneration.class.getSimpleName());
		
		FastNoise noise = new FastNoise();
		
		float[][] copy = new float[height.length][height[0].length];

		for (int y = 0; y < height.length; y++) {
			for (int x = 0; x < height[0].length; x++) {
				copy[y][x] = (noise.GetNoise(x / scale, y / scale)+1f) / 2f;
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
