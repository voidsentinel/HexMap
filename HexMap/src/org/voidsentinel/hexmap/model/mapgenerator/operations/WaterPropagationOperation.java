/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.operations;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;

/**
 * get the height at wich the water start (based on the % of cells under), and
 * then set the distance to nearest water cell for each cell. Positive
 * difference of height is taken into account for the distance : if you are 1
 * case higher than the nearest source, then distance is 1 more... (if you are 1
 * level lower it is not taken into account.
 * 
 * @author voidSentinel
 *
 */
public class WaterPropagationOperation extends AbstractMapOperation {

	public WaterPropagationOperation() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.voidsentinel.hexmap.model.mapgenerator.operations.IMapOperation#filter(
	 * org.voidsentinel.hexmap.model.HexMap)
	 */
	@Override
	public void specificFilter(HexMap map) {
		float waterHeight = map.getWaterHeight();
		int count = -1;
		int max = 0;
		// while there is cell in a higher distance
		while (count <= max) {
			// for each cell
			for (int y = 0; y < map.HEIGHT; y++) {
				for (int x = 0; x < map.WIDTH; x++) {
					HexCell cell = map.getCell(x, y);
					if (cell != null) {
						float cellHeight = cell.getFloatData(HexCell.HEIGHT_DATA);
						if (count == -1) {
							// initialize water cell to 0
							if (cellHeight <= waterHeight) {
								cell.setDistanceToWater(0);
								cell.setData(HexCell.UNDERWATER, true);
							}
							// if it contains the current checked distance...
						} else if (cell.getDistanceToWater() == count) {
							for (HexCell neighbor : cell.getNeighbor()) {
								if (neighbor != null) {
									int diff = Math.max(neighbor.getElevation() - cell.getElevation(), 0);
									int distance = count + 1 + diff;
									if (neighbor.getDistanceToWater() < 0 || neighbor.getDistanceToWater() > distance) {
										neighbor.setDistanceToWater(distance);
										neighbor.setData(HexCell.UNDERWATER, false);
										if (distance > max) {
											max = distance;
										}
									}
								}
							}
						}
					}
				}
			}
			count++;
		}
		LOG.info("               " + count + " max distance to water");

	}

}
