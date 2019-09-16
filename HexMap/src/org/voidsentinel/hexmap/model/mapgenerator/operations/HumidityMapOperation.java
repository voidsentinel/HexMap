/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.operations;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.AbstractTerrainAction;
import org.voidsentinel.hexmap.utils.FastNoise;
import org.voidsentinel.hexmap.utils.TerrainImage;

/**
 * Set The Humidity value for each cell of the map based on distance to water
 * 
 * @author voidSentinel
 *
 */
public class HumidityMapOperation extends AbstractTerrainAction implements IMapOperation {

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

		FastNoise noise = new FastNoise();

		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(x, y);
				if (cell.getBooleanData(HexCell.UNDERWATER)) {
					cell.setData(HexCell.HUMIDITY_DATA, 1f);
				} else {
					double waterdist = Math.pow(0.98d, (double) cell.getDistanceToWater());
					double perlin = noise.GetPerlin((float) (x), (float) (y));
//					cell.setData(HexCell.HUMIDITY_DATA, (float) waterdist * 0.75f + Math.abs((float) perlin) * 0.25f);
//					cell.setData(HexCell.HUMIDITY_DATA, Math.max(0f, 1f - cell.getDistanceToWater() * 0.1f));
					cell.setData(HexCell.HUMIDITY_DATA, (float) waterdist );
				}
			}
		}
		map.normalizeData(HexCell.HUMIDITY_DATA);
		TerrainImage.generateImage(map, HexCell.HUMIDITY_DATA);
	}

}
