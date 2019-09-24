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
public class WaterLevelOperation extends AbstractMapOperation {
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
	public void specificFilter(HexMap map) {
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
	}

}
