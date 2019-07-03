package org.voidsentinel.hexmap.model.mapgenerator.operations;

import java.util.ArrayList;
import java.util.List;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexCoordinates;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.AbstractTerrainAction;

/**
 * Set The PathPrevalence value for each cell of the map. This is done by
 * running a number of path serach, adding a small value to each cell of each
 * path. It is supposed that cell with a high number of path that goe sthrought
 * are important
 * 
 * @author voidSentinel
 *
 */
public class CityMapOperation extends AbstractTerrainAction implements IMapOperation {

	@Override
	public void filter(HexMap map) {
		float value = 0f;
		float maxvalue = 0f;
		boolean ok = true;
		HexCell selectedCell = null;
		List<HexCell> cities = new ArrayList<HexCell>();
		LOG.info("   Operation : " + this.getClass().getSimpleName());

		// set the city value
		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(x, y);
				if (cell != null) {
					value = cell.getFertility() * 0.25f + (1f - 2 * Math.abs(0.5f - cell.getTemperature())) * 0.175f
					      + (cell.getHumidity()) * 0.175f + (1f - cell.getHeight()) * 0.15f
					      + cell.getPathPrevalence() * 0.25f;
					if (cell.getDistanceToWater() == 1) {
						value = value + 0.05f;
					}
					cell.setCityValue(value);
				}
			}
		}

//		// put a city
//		for (int i = 0; i < 4; i++) {
//			selectedCell = findCityPosition(map);
//			createCity(selectedCell, map);
//		}
	}

	private HexCell findCityPosition(HexMap map) {
		float maxvalue = 0;
		float value;
		HexCell selectedCell = null;
		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(x, y);
				if (cell != null) {
					value = cell.getCityValue();

					if (value > maxvalue) {
						selectedCell = cell;
						maxvalue = value;
					}
				}
			}
		}
		return selectedCell;
	}

	private void createCity(HexCell cell, HexMap map) {
		LOG.info(" City at " + cell.hexCoordinates);
		List<HexCoordinates> list = cell.hexCoordinates.inRange(20);
		for (HexCoordinates coord : list) {
			HexCell cell2 = map.getCell(coord);
			if (cell2 != null) {
				cell2.setCityValue(0f);
			}
		}
//		cell.setCityValue(1f);
	}
}
