package org.voidsentinel.hexmap.model.mapgenerator;

import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation.CellularGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation.FaultCirclesGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation.FaultLinesGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation.FlatGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.operation.HexBlurOperation;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.operation.TorusOperation;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.operation.TorusOperation.Direction;
import org.voidsentinel.hexmap.model.mapgenerator.operations.ElevationMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.FertilityMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.HeightMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.HumidityMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.TemperatureMapOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.WaterLevelOperation;
import org.voidsentinel.hexmap.model.mapgenerator.operations.WaterPropagationOperation;

public class CapitalismGenerator extends MapGenerator {

	public void generate(HexMap map) {
		LOG.info("Generating map " + map.WIDTH + "/" + map.HEIGHT + " using " + this.getClass().getSimpleName());

		HeightMapOperation heightmap = new HeightMapOperation();

		// define the height
		heightmap.addGenerator(new FlatGeneration(0f));

//		heightmap.addGenerator(new IslandGeneration(1d), 0.60f);
//		heightmap.addGenerator(new DiamondSquareGeneration(), 0.40f);
		heightmap.addGenerator(new CellularGeneration(1f), 0.20f);
		heightmap.addGenerator(new FaultLinesGeneration(2000), 0.4f);
		heightmap.addGenerator(new FaultCirclesGeneration(1000), 0.4f);

		heightmap.addOperation(new HexBlurOperation(3, 3));
//		heightmap.addGenerator(new CellularGeneration(1f), 0.30f);
//		heightmap.addGenerator(new FaultCirclesGeneration(2000), 0.70f);
		heightmap.addOperation(new TorusOperation(Direction.both));
//		heightmap.addOperation(new HexBlurOperation(3, 3));
//		heightmap.addOperation(new IslandOperation());
//		heightmap.addOperation(new PowerOperation(4d, 1.0d));
		heightmap.filter(map);

		// water level
		new WaterLevelOperation(0.4f).filter(map);

		new ElevationMapOperation(3, 20).filter(map);
		new WaterPropagationOperation().filter(map);
		new TemperatureMapOperation().filter(map);
		new HumidityMapOperation().filter(map);
//		new BiomeOperation2("assets/mod/standard/biome.png").filter(map);;
		new FertilityMapOperation().filter(map);
//		new PathMapOperation().filter(map);
//		new CityMapOperation().filter(map);

	}
}
