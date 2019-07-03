/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap;

import java.util.logging.Logger;

/**
 * @author guipatry
 *
 */
public abstract class AbstractTerrainAction {

	protected static final Logger LOG = Logger.getLogger(AbstractTerrainAction.class.toString());


	/**
	 * Normalize the vales of the map to 0..1
	 * 
	 * @param map
	 */
	protected void normalize(float[][] map) {

		float min = findMinHeight(map);
		float max = findMaxHeight(map);
		float coeff = (1f) / (max - min);

		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				map[y][x] = (map[y][x] - min) * coeff;
			}
		}
	}

	/**
	 * find the minimum value of a float 2D table
	 * 
	 * @param map the float map
	 * @return the min value
	 */
	protected float findMinHeight(float[][] map) {
		float minValue = Float.MAX_VALUE;
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				if (map[y][x] < minValue) {
					minValue = map[y][x];
				}
			}
		}
		return minValue;
	}

	/**
	 * find the minimum value of a float 2D table
	 * 
	 * @param map the float map
	 * @return the max value
	 */
	protected float findMaxHeight(float[][] map) {
		float maxValue = Float.MIN_VALUE;
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				if (map[y][x] > maxValue) {
					maxValue = map[y][x];
				}
			}
		}
		return maxValue;
	}

}
