/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap;

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

	public float[][] generate(float[][] height) {
		LOG.info("   Operation : " + FlatGeneration.class.getSimpleName() + "(" + height + ")");
		for (int y = 0; y < height.length; y++) {
			for (int x = 0; x < height[0].length; x++) {
				height[y][x] = elevation;
			}
		}
		return height;
	}

}
