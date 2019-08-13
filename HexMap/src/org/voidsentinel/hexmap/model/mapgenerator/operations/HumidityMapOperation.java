/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.operations;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexCoordinates;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.AbstractTerrainAction;
import org.voidsentinel.hexmap.utils.FastNoise;
import org.voidsentinel.hexmap.utils.TerrainImage;
import org.voidsentinel.hexmap.utils.FastNoise.NoiseType;

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
		float[][] values = new float[map.HEIGHT][map.WIDTH];

		FastNoise noise = new FastNoise();
		noise.SetNoiseType(NoiseType.Perlin);
		noise.SetFractalOctaves(6);
		noise.SetGradientPerturbAmp(1.5f);

		LOG.info("   Operation : " + this.getClass().getSimpleName());
		int distance = 0;
		float value = 0f;
		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(new HexCoordinates(x, y));
				distance = cell.getDistanceToWater();
				float water = (float) (Math.pow(0.98d, cell.getDistanceToWater()));
				float variation = (noise.GetPerlin((float) x * 2f, (float) y * 2f) + 1f) / 2f;

				if (distance < 0) { // unknow
					value = 0f;
				} else if (distance == 0) {
					value = 1f;
				} else {
					value = Math.min(variation * 0.25f + water * 0.80f, 1f);
				}
				cell.setData(HexCell.HUMIDITY_DATA, value);
				values[y][x] = value;

			}
		}
		TerrainImage.generateImage(values, this.getClass().getSimpleName());

	}

}
