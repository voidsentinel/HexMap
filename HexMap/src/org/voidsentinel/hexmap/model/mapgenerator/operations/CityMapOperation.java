package org.voidsentinel.hexmap.model.mapgenerator.operations;

import java.util.List;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexCoordinates;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.AbstractTerrainAction;
import org.voidsentinel.hexmap.utils.TerrainImage;

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
					value = value + cell.getFertility() * 0.25f;
					value = value + (1f - 2 * Math.abs(0.5f - cell.getTemperature())) * 0.2f;
					value = value + (cell.getHumidity()) * 0.15f;
					value = value + (1f - cell.getHeight()) * 0.15f;
					value = value + cell.getPathPrevalence() * 0.25f;
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
				cell.setCityValue(cities[y][x]);
			}
		}

		TerrainImage.generateImage(cities, this.getClass().getSimpleName());
		
		// put a city
		int step = map.WIDTH / 4;
		 for (int i = 0; i < 3; i++) {
			selectedCell = findCityPosition(map, cities, 0, map.WIDTH, 0, map.HEIGHT);
			createCity(selectedCell, map, cities, step/2);
		}

		for (int i = 0; i < 4; i++) {
			selectedCell = findCityPosition(map, cities, step * i, step * (i + 1), 0, map.HEIGHT);
			if (selectedCell != null)
				createCity(selectedCell, map, cities, step/4);
			selectedCell = findCityPosition(map, cities, step * i, step * (i + 1), 0, map.HEIGHT);
			if (selectedCell != null)
				createCity(selectedCell, map, cities, step/4);
		}

	}


	private HexCell findCityPosition(HexMap map, float[][] cities, int xmin, int xmax, int ymin, int ymax) {
		LOG.info("      searching city between " + xmin + "-" + xmax);
		float maxvalue = -500;
		float value;
		HexCell selectedCell = null;
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
		LOG.info(" City at " + cell.hexCoordinates);
		List<HexCoordinates> list = cell.hexCoordinates.inRange(radius);
		for (HexCoordinates coord : list) {
			HexCell cell2 = map.getCell(coord);
			if (cell2 != null) {
				cities[cell2.hexCoordinates.row][cell2.hexCoordinates.column] = 0f;
				cell2.setCityValue(0f);
			}
		}
		cities[cell.hexCoordinates.row][cell.hexCoordinates.column] = 0f;
		cell.setCityValue(1f);
	}
}
