/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.operations;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.utils.FastNoise;
import org.voidsentinel.hexmap.utils.TerrainImage;

/**
 * Set The Humidity value for each cell of the map based on distance to water
 * 
 * @author voidSentinel
 *
 */
public class HumidityMapOperation extends AbstractMapOperation {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.voidsentinel.hexmap.model.mapgenerator.operations.IMapOperation#filter(
	 * org.voidsentinel.hexmap.model.HexMap)
	 */
	@Override
	public void specificFilter(HexMap map) {
		FastNoise noise = new FastNoise();
		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(x, y);
				if (cell.getBooleanData(HexCell.UNDERWATER)) {
					cell.setData(HexCell.HUMIDITY_DATA, 1f);
				} else {
					double waterdist = Math.pow(0.98d, (double) cell.getDistanceToWater());
					double perlin = Math.min(noise.GetPerlin((float) (x)*3, (float) (y)*3), 0f)*0.2f;
					
					float humidity = (float)Math.min(waterdist+perlin, 1d);
					cell.setData(HexCell.HUMIDITY_DATA, humidity );
				}
			}
		}
		map.normalizeData(HexCell.HUMIDITY_DATA);
		TerrainImage.generateImage(map, HexCell.HUMIDITY_DATA, this.getClass().getSimpleName());
	}

}
