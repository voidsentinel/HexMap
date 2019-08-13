/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.operations;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.AbstractTerrainAction;

/**
 * get the height at wich the water start (based on the % of cells under), and
 * then set the distance to nearest water cell for each cell
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

		// now set the distance to water for each cell
		for (int i = 0; i < 200; i++) {
			for (int y = 0; y < map.HEIGHT; y++) {
				for (int x = 0; x < map.WIDTH; x++) {
					HexCell cell = map.getCell(x, y);
					float cellHeight = cell.getFloatData(HexCell.HEIGHT_DATA);
					// if water : set the value to and the neighborg to 1
					if (cellHeight <= waterHeight && cell.getDistanceToWater() < 0) {
						cell.setDistanceToWater(0);
						for (HexCell neighbor : cell.getNeighbor()) {
							if (neighbor != null && neighbor.getHeight() > waterHeight) {
								neighbor.setDistanceToWater(1);
							}
						}
					}

					if (cellHeight > waterHeight) {
						// otherwise check any neighbor and modify the
						for (HexCell neighbor : cell.getNeighbor()) {
							if (neighbor != null) {
								int distance = neighbor.getDistanceToWater();
								if (distance >= 0) {
									if (cell.getDistanceToWater() < 0) {
										cell.setDistanceToWater(distance + 1);
									} else if (distance < cell.getDistanceToWater()) {
										cell.setDistanceToWater(distance + 1);
									}
								}
							}
						}
					}
				}
			}
		}

	}

}
