/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author    VoidSentinel
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap;

import org.voidsentinel.hexmap.utils.FastNoise;
import org.voidsentinel.hexmap.utils.FastNoise.CellularDistanceFunction;
import org.voidsentinel.hexmap.utils.FastNoise.CellularReturnType;
import org.voidsentinel.hexmap.utils.FastNoise.NoiseType;

/**
 * @author Xerces
 *
 */
public class CellularGeneration extends AbstractTerrainGenerator {

	float									scale	= 0f;

	public CellularGeneration(float scale) {
		this.scale = scale;
	}

	public float[][] generate(float[][] height) {
		LOG.info("   Operation : " + CellularGeneration.class.getSimpleName());
		
		FastNoise noise = new FastNoise();
//		noise.SetNoiseType(NoiseType.Value);
		noise.SetNoiseType(NoiseType.Cellular);
//		noise.SetInterp(Interp.Quintic);
		noise.SetCellularDistanceFunction(CellularDistanceFunction.Euclidean);
		noise.SetCellularReturnType(CellularReturnType.Distance2Sub);
		
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
