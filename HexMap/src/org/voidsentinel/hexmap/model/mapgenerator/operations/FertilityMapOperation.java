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

/**
 * Set the fertility value of the map based on the biome (80%) and a noise (20%)
 * 
 * @author VoidSentinel
 *
 */
public class FertilityMapOperation extends AbstractTerrainAction implements IMapOperation {

	@Override
	public void filter(HexMap map) {
		LOG.info("   Operation : " + this.getClass().getSimpleName());

		float[][] values = new float[map.HEIGHT][map.WIDTH];

		FastNoise noise = new FastNoise();
		float value;
		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(new HexCoordinates(x, y));
				float cellHeight = cell.getFloatData(HexCell.HEIGHT_DATA);
				
				if (cellHeight <= map.getWaterHeight()) {
					value = 0f;
				} else {
					value = (noise.GetSimplex(x * 3, y * 3) + 1f) / 2f;
				}
				values[y][x] = value;
				cell.setData(HexCell.SOIL_DATA, value);
			}
		}

		TerrainImage.generateImage(values, this.getClass().getSimpleName());

	}

}
