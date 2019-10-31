/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.operations;

import org.voidsentinel.hexmap.model.Direction;
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
					double perlin = 0d; //Math.min(noise.GetPerlin((float) (x) * 3, (float) (y) * 3), 0f) * 0.2f;

					float humidity = (float) Math.min(waterdist + perlin, 1d);
					cell.setData(HexCell.HUMIDITY_DATA, humidity);
				}
			}
		}
		map.normalizeData(HexCell.HUMIDITY_DATA);
		initialHumidity(map, noise);
		map.normalizeData(HexCell.HUMIDITY_DATA);
		TerrainImage.generateImage(map, HexCell.HUMIDITY_DATA, this.getClass().getSimpleName());
	}

	private void initialHumidity(HexMap map, FastNoise noise) {
		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(x, y);
				float perlin = Math.abs(noise.GetPerlin((float) (x) * 3, (float) (y) * 3)) * 0.5f;
				cell.setData(HexCell.HUMIDITY_DATA, cell.getFloatData(HexCell.HUMIDITY_DATA)+perlin);
			}
		}
	}

	private void overWater(HexMap map) {
		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(x, y);
				if (cell.getBooleanData(HexCell.UNDERWATER)) {
					HexCell neighbor = cell.getNeighbor(Direction.EAST);
					if (neighbor != null) {
						neighbor.setData(HexCell.HUMIDITY_DATA,
						      Math.max(cell.getFloatData(HexCell.HUMIDITY_DATA), neighbor.getFloatData(HexCell.HUMIDITY_DATA))
						            + 0.2f);
					}
				}
			}
		}
	}

	private void overGround(HexMap map) {
		float humidity = 0f;
		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(x, y);
				if (cell.getBooleanData(HexCell.UNDERWATER)) {
					humidity = Math.max(humidity, cell.getFloatData(HexCell.HUMIDITY_DATA));
				}
				HexCell neighbor = cell.getNeighbor(Direction.EAST);
				humidity = transfertHumidity(cell, neighbor, humidity);
				neighbor = cell.getNeighbor(Direction.NE);
				humidity = transfertHumidity(cell, neighbor, humidity);
				neighbor = cell.getNeighbor(Direction.SE);
				humidity = transfertHumidity(cell, neighbor, humidity);
			}
		}
	}

	private void resetWater(HexMap map, float value) {
		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(x, y);
				if (cell.getBooleanData(HexCell.UNDERWATER)) {
					HexCell neighbor = cell.getNeighbor(Direction.EAST);
					if (neighbor != null) {
						neighbor.setData(HexCell.HUMIDITY_DATA, value);
					}
				}
			}
		}
	}

	private float transfertHumidity(HexCell source, HexCell destination, float humidity) {
		float copie = humidity;
		float pluie = 0f;
		if (source == null || destination == null) {
			return humidity;
		}
		if (!destination.getBooleanData(HexCell.UNDERWATER)) {
			int diff = destination.getElevation() - source.getElevation();
			if (diff <= 1) {
				pluie = humidity * 0.05f;
			} else {
				pluie = humidity * 0.1f;
			}
			destination.setData(HexCell.HUMIDITY_DATA, destination.getFloatData(HexCell.HUMIDITY_DATA) + pluie);
			copie = copie - pluie;
		}

		return copie;
	}

}
