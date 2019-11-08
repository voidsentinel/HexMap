/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation;

import org.voidsentinel.hexmap.model.HexMap;

/**
 * return a copy of the given map's height
 * This is useful to generate a terrain based on another one
 * @author guipatry
 *
 */
public class MapInitialization extends AbstractTerrainGenerator {
	private HexMap map = null;

	public MapInitialization(HexMap map) {
		this.map = map;
	}

	public float[][] generate(int xSize, int ySize) {
		LOG.info("   Operation : " + MapInitialization.class.getSimpleName());
		float[][] copy = new float[ySize][xSize];
		for (int y = 0; y < ySize; y++) {
			for (int x = 0; x < xSize; x++) {
				copy[y][x] = map.getCell(x, y).getHeight();
			}
		}
		return copy;
	}

}
