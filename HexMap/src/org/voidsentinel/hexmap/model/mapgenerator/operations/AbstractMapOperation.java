/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.operations;

import java.util.logging.Logger;

import org.voidsentinel.hexmap.model.HexMap;

/**
 * This class is an abstract implementation of the interface IMapOperation.
 * Children classes should implement the specificFilter method to perform the
 * filter. Utility method are also provided
 * 
 * @author voidSentinel
 *
 */
public abstract class AbstractMapOperation implements IMapOperation {

	protected static final Logger LOG = Logger.getLogger(AbstractMapOperation.class.toString());

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.voidsentinel.hexmap.model.mapgenerator.operations.IMapOperation#filter(
	 * org.voidsentinel.hexmap.model.HexMap)
	 */
	@Override
	public void filter(HexMap map) {
		long startTime = System.nanoTime();
		LOG.info("   Operation : " + this.getClass().getSimpleName());
		specificFilter(map);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1000000; // divide by 1000000 to get milliseconds.
		LOG.info("               Execution in " + duration +" ms");
	}

	/**
	 * 
	 * @param map
	 */
	abstract protected void specificFilter(HexMap map);

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
	 * find the maximum value of a float 2D table
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
