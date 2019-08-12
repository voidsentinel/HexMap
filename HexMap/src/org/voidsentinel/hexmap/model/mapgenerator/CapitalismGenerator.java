package org.voidsentinel.hexmap.model.mapgenerator;

import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation.CellularGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation.DiamondSquareGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation.FaultLinesGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation.FlatGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation.PerlinGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation.SimplexGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.operation.FastErosionOperation;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.operation.HexBlurOperation;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.operation.TorusOperation;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.operation.TorusOperation.VTreatment;
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
		heightmap.addGenerator(new PerlinGeneration(0.25f), 0.25f);
//		heightmap.addGenerator(new SimplexGeneration(0.25f), 0.33f);
		
		heightmap.addGenerator(new FaultLinesGeneration((map.WIDTH + map.HEIGHT)/2));
//		heightmap.addGenerator(new DiamondSquareGeneration());
//		heightmap.addGenerator(new CellularGeneration(1f), 0.5f);

		heightmap.addOperation(new HexBlurOperation(3, 1));
		heightmap.addOperation(new HexBlurOperation(2, 1));
		heightmap.addOperation(new FastErosionOperation(0.1f, 64));
//		heightmap.addOperation(new TorusOperation(TorusOperation.HTreatment.BOTH , TorusOperation.VTreatment.BOTH));
		//heightmap.addOperation(new IslandOperation());
		
		heightmap.filter(map);

		new BiomeOperation(new String[] { "FMP-sea", "FMP-reef", "FMP-swamp", "FMP-plain", "FMP-mountain" }, 5)
		      .filter(map);
		new WaterLevelOperation(0.30f).filter(map);
		new ElevationMapOperation(5, 20).filter(map);
		new TemperatureMapOperation().filter(map);
		new HumidityMapOperation().filter(map);
		new FertilityMapOperation().filter(map);
		new PathMapOperation().filter(map);
		new CityMapOperation().filter(map);
	}
}
