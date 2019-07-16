package org.voidsentinel.hexmap.model.mapgenerator;

import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation.CellularGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation.DiamondSquareGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation.FaultLinesGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation.FlatGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation.PerlinGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.operation.HexBlurOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.BiomeOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.CityMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.ElevationMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.FertilityMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.HeightMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.HumidityMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.PathMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.TemperatureMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.WaterLevelOperation;

public class CapitalismGenerator extends MapGenerator {

	public void generate(HexMap map) {
		LOG.info("Generating map " + map.WIDTH + "/" + map.HEIGHT + " using " + this.getClass().getSimpleName());

		HeightMapOperation heightmap = new HeightMapOperation();
		heightmap.addGenerator(new FlatGeneration(0f));
		heightmap.addGenerator(new PerlinGeneration(0.25f));
//
		heightmap.addGenerator(new FaultLinesGeneration(map.WIDTH + map.HEIGHT));
//		heightmap.addGenerator(new DiamondSquareGeneration());
//		heightmap.addGenerator(new DiamondSquareGeneration());
//		heightmap.addGenerator(new CellularGeneration(1f));

//		heightmap.addOperation(new HexBlurOperation(3, 1));

		heightmap.filter(map);

		new BiomeOperation(new String[] { "FMP-sea", "FMP-reef", "FMP-swamp", "FMP-plain", "FMP-mountain" }, 5)
		      .filter(map);
		new WaterLevelOperation(0.2f).filter(map);
		new ElevationMapOperation(5, 20).filter(map);
		new TemperatureMapOperation().filter(map);
		new HumidityMapOperation().filter(map);
		new FertilityMapOperation().filter(map);
		new PathMapOperation().filter(map);
		new CityMapOperation().filter(map);
	}
}
