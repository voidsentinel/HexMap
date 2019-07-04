/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation;

/**
 * This operation transform the terrain as a a flat one, at the given height
 * 
 * @author guipatry
 *
 */
public class FlatGeneration extends AbstractTerrainGenerator {
	private float						elevation	= 1f;

	public FlatGeneration(float elevation) {
		this.elevation = elevation;
	}

	public float[][] generate(int xSize, int ySize) {
		LOG.info("   Operation : " + FlatGeneration.class.getSimpleName() + "(" + elevation + ")");
		float[][] copy = new float[ySize][xSize];
		for (int y = 0; y < ySize; y++) {
			for (int x = 0; x < xSize; x++) {
				copy[y][x] = elevation;
			}
		}
		return copy;
	}

}
