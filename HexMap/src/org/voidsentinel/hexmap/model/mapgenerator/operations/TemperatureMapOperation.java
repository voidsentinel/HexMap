package org.voidsentinel.hexmap.model.mapgenerator.operations;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexCoordinates;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.AbstractTerrainAction;
import org.voidsentinel.hexmap.utils.OpenSimplexNoise;
import org.voidsentinel.hexmap.utils.TerrainImage;

/**
 * Set The temperature value for each cell of the map based on height, latitude
 * and water status
 * 
 * @author voidSentinel
 *
 */
public class TemperatureMapOperation extends AbstractTerrainAction implements IMapOperation {

	public float			minTemperature		= -25;
	public float			maxTemperature		= 35;
	public final float	HEIGHTATTENUATION	= 1f;	// expressed in °/elevation

	@Override
	public void filter(HexMap map) {
		LOG.info("   Operation : " + this.getClass().getSimpleName());

		final float LATITATTENUATION = (maxTemperature - minTemperature) / map.HEIGHT; // expressed in °/ cell

		float[][] temperature = new float[map.HEIGHT][map.WIDTH];
		OpenSimplexNoise osn = new OpenSimplexNoise();

		// calculate lattitude heat value
		int middle = map.HEIGHT / 2;

		for (int y = 0; y < map.HEIGHT; y++) {
			//

			// temp depends on latitude
			int latitude = Math.abs(y - middle); // 0 at equateur, middle at pole
			float latitudeAttenuation = LATITATTENUATION * latitude; // 0.5° / cell
			float baseTemperatrure = maxTemperature - latitudeAttenuation;

			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(new HexCoordinates(x, y));
				// less heat in altitude
				float heightattenuation = HEIGHTATTENUATION * cell.getElevation(); // 1.2° / elevation
				// local variation
				float variation = (float) (osn.eval(x / 10f, y / 10f)) * 2f;

				float value = (baseTemperatrure - heightattenuation + variation);

				if (cell.getDistanceToWater() == 0) {
					value = value - 3f;
				}

				value = (value - minTemperature) / (maxTemperature - minTemperature);

				float cellvalue = Math.min(Math.max(value, 0), 1f);
				temperature[y][x] = cellvalue;

			}
		}
		this.normalize(temperature);

		TerrainImage.generateImage(temperature, "temperature");

		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(new HexCoordinates(x, y));
				cell.setTemperature(temperature[y][x]);
			}
		}
	}

}
