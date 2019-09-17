/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.operations;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.AbstractTerrainAction;

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
public class WaterLevelOperation extends AbstractTerrainAction implements IMapOperation {
	private float percent = 0.2f;

	public WaterLevelOperation(float percent) {
		this.percent = percent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.voidsentinel.hexmap.model.mapgenerator.operations.IMapOperation#filter(
	 * org.voidsentinel.hexmap.model.HexMap)
	 */
	@Override
	public void filter(HexMap map) {
		LOG.info("   Operation : " + this.getClass().getSimpleName());

		int[] number = new int[10000];
		int height = 0;
		// check how many hex at a given altitude (by increment of 1/10000)
		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(x, y);

				height = (int) Math.max(0, Math.min(9999f, cell.getFloatData(HexCell.HEIGHT_DATA) * 10000f));
				number[height]++;
			}
		}
		// find what is the last height that contains < percent cells (cumulated)
		int current = 0;
		int total = map.HEIGHT * map.WIDTH;
		int limit = (int) (total * percent);
		int waterlevel = 0;
		for (int i = 0; i < number.length; i++) {
			current += number[i];
			if (current < limit) {
				waterlevel = i;
			}
		}

		float waterHeight = ((float) (waterlevel * 1f)) / 10000f;
		map.setWaterHeight(waterHeight);

		for (int count = -1; count < map.WIDTH; count++) {
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
						} else if (cell.getDistanceToWater() == count) {
							for (HexCell neighbor : cell.getNeighbor()) {
								if (neighbor != null) {
									int diff = Math.max(neighbor.getElevation() - cell.getElevation(), 0);
									int distance = count + 1 + diff;
									if (neighbor.getDistanceToWater() < 0 || neighbor.getDistanceToWater() > distance) {
										neighbor.setDistanceToWater(distance);
										neighbor.setData(HexCell.UNDERWATER, false);
									}
								}
							}
						}
					}
				}
			}

//		// now set the distance to water for each cell
//		for (int i = 0; i < 200; i++) {
//			for (int y = 0; y < map.HEIGHT; y++) {
//				for (int x = 0; x < map.WIDTH; x++) {
//					HexCell cell = map.getCell(x, y);
//					if (cell != null) {
//						float cellHeight = cell.getFloatData(HexCell.HEIGHT_DATA);
//						// if water : set the value to and the neighborg to 1
//						if (cellHeight <= waterHeight && cell.getDistanceToWater() < 0) {
//							cell.setDistanceToWater(0);
//							cell.setData(HexCell.UNDERWATER, true);
//							for (HexCell neighbor : cell.getNeighbor()) {
//								if (neighbor != null && neighbor.getHeight() > waterHeight) {
//									neighbor.setDistanceToWater(1);
//								}
//							}
//						}
//
//						if (cellHeight > waterHeight) {
//							cell.setData(HexCell.UNDERWATER, false);
//							// otherwise check any neighbor and modify the
//							for (HexCell neighbor : cell.getNeighbor()) {
//								if (neighbor != null) {
//									int diff = Math.min(neighbor.getElevation() - cell.getElevation(), 0);
//									int distance = neighbor.getDistanceToWater() + diff;
//									if (distance >= 0) {
//										if (cell.getDistanceToWater() < 0) {
//											cell.setDistanceToWater(distance + 1);
//										} else if (distance < cell.getDistanceToWater()) {
//											cell.setDistanceToWater(distance + 1);
//										}
//									}
//								}
//							}
//						}
//					}
//				}
//			}
		}

	}

}
