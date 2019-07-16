/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.operations;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexCoordinates;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.AbstractTerrainAction;
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
		noise.SetNoiseType(NoiseType.Perlin);
		noise.SetFractalOctaves(6);
		noise.SetGradientPerturbAmp(1.5f);
      FastNoise noise = new FastNoise(Alea.nextInt());
		LOG.info("   Operation : " + this.getClass().getSimpleName());
		int distance = 0;
		float toWater = 0f;
		float local = 0f;
		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(new HexCoordinates(x, y));
				distance = cell.getDistanceToWater();
				float water = (float) (Math.pow(0.98d, cell.getDistanceToWater()));
				float variation = (noise.GetPerlin((float)x*2f, (float)y*2f)+1f)/2f;
				
				if (distance < 0) { // unknow
					cell.setHumidity(0f);
				} else if (distance == 0) {
					cell.setHumidity(1f);
				} else {
					//cell.setHumidity(Math.min(variation, 1f));
					cell.setHumidity(Math.min(variation*0.25f+water*0.80f, 1f));
				}
				values[y][x] = cell.getHumidity();

			}
		}
		TerrainImage.generateImage(values, this.getClass().getSimpleName());

	}

}
