package org.voidsentinel.hexmap.model.mapgenerator;

import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation.CellularGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation.FaultCirclesGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation.FlatGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.operation.HexBlurOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.BiomeOperation2;
import org.voidsentinel.hexmap.model.mapgenerator.operations.CityMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.ElevationMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.FertilityMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.HeightMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.HumidityMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.PathMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.TemperatureMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.WaterLevelOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.WaterPropagationOperation;

public class CapitalismGenerator extends MapGenerator {

	public void generate(HexMap map) {
		LOG.info("Generating map " + map.WIDTH + "/" + map.HEIGHT + " using " + this.getClass().getSimpleName());

		HeightMapOperation heightmap = new HeightMapOperation();
		heightmap.addGenerator(new FlatGeneration(0f));
		heightmap.addGenerator(new FaultCirclesGeneration(2000));
		heightmap.addGenerator(new CellularGeneration(1f, 1f, 30f, 30f), 0.25f);
		
		heightmap.addOperation(new HexBlurOperation(3, 3));

		heightmap.filter(map);

		new WaterLevelOperation(0.30f).filter(map);
		new ElevationMapOperation(5, 20).filter(map);
		new WaterPropagationOperation().filter(map);
		new TemperatureMapOperation().filter(map);
		new HumidityMapOperation().filter(map);
		new BiomeOperation2("assets/mod/standard/biome.png").filter(map);;
		new FertilityMapOperation().filter(map);
		new PathMapOperation().filter(map);
		new CityMapOperation().filter(map);
		
	}
}
