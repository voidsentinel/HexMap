/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.operations;

import org.voidsentinel.hexmap.model.ClimateData;
import org.voidsentinel.hexmap.model.Direction;
import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexCoordinates;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.AbstractTerrainAction;
import org.voidsentinel.hexmap.utils.TerrainImage;

/**
 * Set The Humidity value for each cell of the map based on distance to water
 * 
 * @author voidSentinel
 *
 */
public class HumidityMapOperation extends AbstractTerrainAction implements IMapOperation {

	private static final float	EVAPORATIONFACTOR		= 0.5f;
	private static final float	PRECIPITATIONFACTOR	= 0.25f;

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

		ClimateData[][] values = new ClimateData[map.HEIGHT][map.WIDTH];
		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				values[y][x] = new ClimateData();
			}
		}

		for (int i = 0; i < map.HEIGHT*map.WIDTH; i++) {
			for (int y = 0; y < map.HEIGHT; y++) {
				for (int x = 0; x < map.WIDTH; x++) {
					HexCell cell = map.getCell(x, y);
					evolveClimate(map, cell, x, y, values);
				}
			}
		}

		float [][] moisture = normalizeMoisture(map, values);
		TerrainImage.generateImage(moisture, this.getClass().getSimpleName());
	}

	public void evolveClimate(HexMap map, HexCell cell, int x, int y, ClimateData[][] values) {
		ClimateData climate = values[y][x];
		if (cell.getBooleanData(HexCell.UNDERWATER)) {
			climate.moisture = 1f;
			climate.clouds = climate.clouds + EVAPORATIONFACTOR;
		} else {
			float evaporation = climate.moisture * EVAPORATIONFACTOR;
			climate.moisture = climate.moisture - evaporation;
			climate.clouds = climate.clouds + evaporation;
		}

		float precipitation = climate.clouds * PRECIPITATIONFACTOR;
		climate.clouds = climate.clouds - precipitation;
		climate.moisture = climate.moisture + precipitation;

		// disperse clouds
		float cloudDispersal = climate.clouds * (1f / 6f);
		for (Direction direction : Direction.values()) {
			HexCoordinates c2 = cell.hexCoordinates.direction(direction);
			int x1 = c2.column;
			int y1 = c2.row;
			if (x1 >= 0 && x1 < map.WIDTH && y1 >= 0 && y1 < map.HEIGHT) {
				values[y1][x1].clouds += cloudDispersal;
			}
		}

		climate.clouds = 0F;
		values[y][x] = climate;
	}

	public float[][]  normalizeMoisture (HexMap map, ClimateData[][] climate) {
		float[][] values = new float[map.HEIGHT][map.WIDTH];
		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				values[y][x] = climate[y][x].moisture;
			}
		}
		normalize(values);
		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(x, y);
				if (cell != null) {
					cell.setData(HexCell.HUMIDITY_DATA, values[y][x]);
				}
			}
		}
		return values;
	}

}
