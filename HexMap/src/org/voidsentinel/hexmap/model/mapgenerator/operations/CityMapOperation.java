package org.voidsentinel.hexmap.model.mapgenerator.operations;

import java.util.List;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexCoordinates;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.utils.TerrainImage;

/**
 * Set The city for each cell of the map. This is done by looking at
 * pathprevalence (ie a path that is commonly used is a good place to get a
 * city), cell fertility (a good thing to have) temperature (moderate is
 * privilegied), humidity (moderate is priviliegied), height (mountain get a
 * malus). Place in border of an water are favored (less for river than open
 * water).
 * <p>
 * Place 4 big and 8 small cities on tghe map, based on thoses values. each city
 * have an interdiction zone where no other city can be created. the size is
 * dependent on the city size.
 * 
 * @author voidSentinel
 *
 */
public class CityMapOperation extends AbstractMapOperation {

	@Override
	public void specificFilter(HexMap map) {
		float[][] cities = new float[map.HEIGHT][map.WIDTH];

		float value = 0f;
		HexCell selectedCell = null;
		LOG.info("   Operation : " + this.getClass().getSimpleName());

		// set the city value
		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(x, y);
				if (cell != null) {
					value = 0f;
					value = value + cell.getFloatData(HexCell.SOIL_DATA) * 0.35f;
					value = value + (1f - 2 * Math.abs(0.5f - cell.getFloatData(HexCell.TEMPERATURE_DATA))) * 0.2f;
					value = value + (cell.getFloatData(HexCell.HUMIDITY_DATA)) * 0.15f;
					value = value + (1f - cell.getFloatData(HexCell.HEIGHT_DATA)) * 0.15f;
//					value = value + cell.getFloatData(HexCell.PATH_DATA) * 0.25f;
					// add a little bit if on shore
					if (cell.getDistanceToWater() == 1) {
						value = value + 0.05f;
					}
					cities[y][x] = value;
					// cell.setCityValue(value);
				}
			}
		}

		normalize(cities);

		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(x, y);
				cell.setData(HexCell.CITY_DATA, cities[y][x]);
				cell.setHasCity(false);
			}
		}

		TerrainImage.generateImage(cities, this.getClass().getSimpleName());

		// put a city
		int stepx = map.WIDTH / 4;
		for (int i = 0; i < 3; i++) {
			selectedCell = findCityPosition(map, cities, 0, map.WIDTH, 0, map.HEIGHT);
			createCity(selectedCell, map, cities, 3);
		}

		for (int i = 0; i < 4; i++) {
			selectedCell = findCityPosition(map, cities, stepx * i, stepx * (i + 1), 0, map.HEIGHT);
			if (selectedCell != null)
				createCity(selectedCell, map, cities, 4);
			selectedCell = findCityPosition(map, cities, stepx * i, stepx * (i + 1), 0, map.HEIGHT);
			if (selectedCell != null)
				createCity(selectedCell, map, cities, 6);
		}

	}

	private HexCell findCityPosition(HexMap map, float[][] cities, int xmin, int xmax, int ymin, int ymax) {
		LOG.info("      searching city between " + xmin + "-" + xmax);
		float maxvalue = -500;
		float value;
		HexCell selectedCell = null;
		xmin = Math.max(xmin, map.WIDTH / 10);
		xmax = Math.min(xmax, map.WIDTH - map.WIDTH / 10);
		ymin = Math.max(ymin, map.HEIGHT / 10);
		ymax = Math.max(ymax, map.HEIGHT - map.HEIGHT / 10);
		for (int y = Math.max(ymin, 0); y < Math.min(ymax, map.HEIGHT); y++) {
			for (int x = Math.max(xmin, 0); x < Math.min(xmax, map.WIDTH); x++) {
				HexCell cell = map.getCell(x, y);
				if (cell != null) {
					value = cities[y][x];
					if (value > maxvalue) {
						selectedCell = cell;
						maxvalue = value;
					}
				}
			}
		}
		LOG.info("      best value found  " + maxvalue);

		return selectedCell;
	}

	private void createCity(HexCell cell, HexMap map, float[][] cities, int radius) {
		if (cell != null) {
			LOG.info(" City at " + cell.hexCoordinates);
			List<HexCoordinates> list = cell.hexCoordinates.inRange(radius);
			for (HexCoordinates coord : list) {
				HexCell cell2 = map.getCell(coord);
				if (cell2 != null) {
					cities[cell2.hexCoordinates.row][cell2.hexCoordinates.column] = 0f;
					cell2.setData(HexCell.CITY_DATA, 0f);
				}
			}
			cities[cell.hexCoordinates.row][cell.hexCoordinates.column] = 0f;
			cell.setData(HexCell.CITY_DATA, 1f);
		}
	}
}
