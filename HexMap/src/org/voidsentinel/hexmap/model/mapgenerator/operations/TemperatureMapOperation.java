package org.voidsentinel.hexmap.model.mapgenerator.operations;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexCoordinates;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.utils.Alea;
import org.voidsentinel.hexmap.utils.FastNoise;
import org.voidsentinel.hexmap.utils.TerrainImage;

/**
 * Set The temperature value for each cell of the map based on height, latitude
 * and water status
 * 
 * @author voidSentinel
 *
 */
public class TemperatureMapOperation extends AbstractMapOperation {

	public float			minTemperature		= -25;
	public float			maxTemperature		= 35;
	public final float	HEIGHTATTENUATION	= 1f;	// expressed in �/elevation

	@Override
	public void specificFilter(HexMap map) {
		final float LATITATTENUATION = (maxTemperature - minTemperature) / (map.HEIGHT*1.1f); // expressed in �/ cell

		float[][] temperature = new float[map.HEIGHT][map.WIDTH];
		FastNoise osn = new FastNoise(Alea.nextInt());

		// calculate lattitude heat value
		int middle = map.HEIGHT / 2;

		for (int y = 0; y < map.HEIGHT; y++) {
			//

			// temp depends on latitude
			int latitude = Math.abs(y - middle); // 0 at equateur, middle at pole
			float latitudeAttenuation = LATITATTENUATION * latitude; // 0.5� / cell
			float baseTemperature = maxTemperature - latitudeAttenuation;

			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(new HexCoordinates(x, y));
				// less heat in altitude
				float heightattenuation = HEIGHTATTENUATION * cell.getIntData(HexCell.ELEVATION_DATA); // 1.2� / elevation
				// local variation
				float variation = (float) (osn.GetNoise((float)x / 10f, (float)y / 10f)) * 10f -5f ;

				float value = (baseTemperature - heightattenuation + variation);

				if (cell.getDistanceToWater() == 0) {
					value = value - 5f;
				}

				value = (value - minTemperature) / (maxTemperature - minTemperature);

				float cellvalue = Math.min(Math.max(value, 0), 1f);
				temperature[y][x] = cellvalue;

			}
		}
		this.normalize(temperature);

		TerrainImage.generateImage(temperature, this.getClass().getSimpleName());

		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(new HexCoordinates(x, y));
				cell.setData(HexCell.TEMPERATURE_DATA, temperature[y][x]);
			}
		}
	}

}
