package org.voidsentinel.hexmap.model.mapgenerator;

import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.CellularGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.DiamondSquareGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.FaultLinesGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.FeastErosionOperation;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.FlatGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.GenericTerrainTypeOperation;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.HexBlurOperation;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.HexParticleDepositionGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.IslandOperation;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.PowerOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.CityMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.ElevationMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.FertilityMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.HumidityMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.PathMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.TemperatureMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.WaterLevelOperation;
import org.voidsentinel.hexmap.utils.TerrainImage;

public class CaptitalimeGenerator extends MapGenerator {

	public void generate(HexMap map) {
		LOG.info("Generating map " + map.WIDTH + "/" + map.HEIGHT + " using " + this.getClass().getSimpleName());

		float[][] heights = new float[map.HEIGHT][map.WIDTH];
//    generating heightMap
		new FlatGeneration(0f).generate(heights);
//		new HexParticleDepositionGeneration(map).generate(heights);
		new DiamondSquareGeneration().generate(heights);
		new CellularGeneration(1f).generate(heights);
//		new FaultLinesGeneration(map).generate(heights);
//		new IslandOperation().generate(heights);

//		new PowerOperation(1.3f).filter(heights);
//		new FeastErosionOperation(0.05f, 1000).filter(heights);
		new HexBlurOperation(6, 1).filter(heights);

		TerrainImage.generateImage(heights);

//    using heightmap for terrain & elevation
		new GenericTerrainTypeOperation(map,
		      new String[] { "FMP-sea", "FMP-reef", "FMP-swamp", "FMP-plain", "FMP-mountain" }, 5).filter(heights);

		map.reCalculateProperties();

		new WaterLevelOperation(0.2f).filter(map);
		new ElevationMapOperation(5, 20).filter(map);
		new TemperatureMapOperation().filter(map);
		new HumidityMapOperation().filter(map);
		new FertilityMapOperation().filter(map);
		new PathMapOperation().filter(map);
		new CityMapOperation().filter(map);
	}
}
