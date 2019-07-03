package org.voidsentinel.hexmap.model.mapgenerator.operations;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexCoordinates;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.AbstractTerrainAction;
import org.voidsentinel.hexmap.utils.OpenSimplexNoise;

/**
 * Set The temperature value for each cell of the map based on height, latitude and water status
 * 
 * @author voidSentinel
 *
 */
public class TemperatureMapOperation extends AbstractTerrainAction implements IMapOperation {

	@Override
	public void filter(HexMap map) {
		LOG.info("   Operation : " + this.getClass().getSimpleName());

		float[][] temperature = new float[map.HEIGHT][map.WIDTH];
		OpenSimplexNoise osn = new OpenSimplexNoise();

		// calculate longitude heat value
		int middle = map.HEIGHT / 2;

		for (int y = 0; y < map.HEIGHT; y++) {

			// temp depends on latitude
			int temp = Math.abs(y - middle); // 0 at equateur, middle at pole
			float tempf = 1.2f - (float) (temp) / (float) (middle); // 1.2f to 0f

			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(new HexCoordinates(x, y));
				// less heat in altitude
				float heightattenuation = 0.15f * cell.getHeight();
				// and a small variation
				float cellvalue = tempf - heightattenuation - (float) (osn.eval(x / 10f, y / 10f)) * 0.1f;
				if (cell.getHeight() <= map.getWaterHeight()) {
					cellvalue = Math.min(Math.max(cellvalue - 0.15f, 0), 1f);
				} else {
					cellvalue = Math.min(Math.max(cellvalue, 0), 1f);
				}
				temperature[y][x] = cellvalue;
			}
		}
		this.normalize(temperature);

		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(new HexCoordinates(x, y));
				cell.setTemperature(temperature[y][x]);
			}
		}

	}

}
