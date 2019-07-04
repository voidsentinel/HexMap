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

	float									scale	= 0f;

	public CellularGeneration(float scale) {
		this.scale = scale;
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
				copy[y][x] = (noise.GetNoise(x / scale, y / scale)+1f) / 2f;
			}
		}
		this.normalize(copy);

		return copy;
	}

}
