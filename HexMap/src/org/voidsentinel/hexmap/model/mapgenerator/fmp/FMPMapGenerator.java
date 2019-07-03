/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author    VoidSentinel
 */
package org.voidsentinel.hexmap.model.mapgenerator.fmp;

import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.mapgenerator.MapGenerator;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.FaultLinesGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.FlatGeneration;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.GenericTerrainTypeOperation;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.HexBlurOperation;
import org.voidsentinel.hexmap.utils.TerrainImage;

/**
 * This a default map generator for full metal planet
 * 
 * @author Xerces
 *
 */
public class FMPMapGenerator extends MapGenerator {

	/**
	 * Map
	 */
	public FMPMapGenerator() {
	}

	/**
	 * generate the map along the method described in the URL given in class header
	 */
	public void generate(HexMap map) {
		LOG.info("Generating map " + map.WIDTH + "/" + map.HEIGHT + " using " + FMPMapGenerator.class.getSimpleName());

		float[][] heights = new float[map.HEIGHT][map.WIDTH];

		new FlatGeneration(0f).generate(heights);

		heights = new FaultLinesGeneration(map, 0.5f).generate(heights);
//		new DiamondSquareGeneration().filter(heights);
//		new FBMGeneration(1, 0.5f, 10).filter(heights);
//      new HexParticleDepositionGeneration(map, 10, map.WIDTH*map.HEIGHT).filter(heights);
//		new PowerOperation(1.5).filter(heights);
		new HexBlurOperation(6, 1).filter(heights);

		TerrainImage.generateImage(heights, "heights");

		heights = new GenericTerrainTypeOperation(map,
		      new String[] { "FMP-sea", "FMP-reef", "FMP-swamp", "FMP-plain", "FMP-mountain" }, 1).filter(heights);

		map.reCalculateProperties();

	}

}
