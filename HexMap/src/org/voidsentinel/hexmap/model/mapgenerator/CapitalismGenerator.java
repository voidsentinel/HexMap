package org.voidsentinel.hexmap.model.mapgenerator;

import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation.DiamondSquareGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation.FlatGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation.HexParticleDepositionGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation.PerlinGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.operation.ContrastOperation;
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
		heightmap.addGenerator(new HexParticleDepositionGeneration(map.HEIGHT + map.WIDTH, 100), 0.5f);
		heightmap.addGenerator(new DiamondSquareGeneration());
		heightmap.addGenerator(new PerlinGeneration(0.3f), 0.25f);
		heightmap.addOperation(new HexBlurOperation(3, 1));
		heightmap.addOperation(new ContrastOperation(-0.25f));

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
