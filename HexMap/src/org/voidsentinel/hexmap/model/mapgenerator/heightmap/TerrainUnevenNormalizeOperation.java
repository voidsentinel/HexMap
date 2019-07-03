/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap;

import java.util.logging.Logger;

/**
 * @author Xerces
 *
 */
public class TerrainUnevenNormalizeOperation extends AbstractTerrainOperation {

	private static final Logger	LOG	= Logger.getLogger(TerrainUnevenNormalizeOperation.class.toString());

	float[]								heights;
	int									steps	= 1;
	float[]								percents;

	public TerrainUnevenNormalizeOperation(float[] percents) {
		this.percents = percents;
		steps = percents.length;
	}

	public TerrainUnevenNormalizeOperation(int steps) {
		this.steps = steps;
		percents = new float[steps];
		for (int i = 0; i < steps; i++) {
			percents[i] = 100f / (float) steps;
		}
	}

	public float[][] filter(float[][] map) {
		LOG.info("   Operation : " + this.getClass().getSimpleName());

		return map;
//		heights = new float[map.getXSize() * map.getYSize()];
//		int index = 0;
//		
//		float min = findMinHeight(map);
//		float max = findMaxHeight(map);
//		LOG.info("      Min : " + min + " Max : " + max);
//
//		// get the repartition
//		for (int x = 0; x < map.getXSize(); x++) {
//			for (int y = 0; y < map.getYSize(); y++) {
//				HexCell cell = map.getCell(x, y);
//				if (cell != null) {
//					heights[index++] = cell.getValue();
//				}
//			}
//		}
//		int nbcells = (index - 1) / steps;
//		Arrays.sort(heights);
//		LOG.info(" total nb cells    : "+ heights.length);			
//		LOG.info(" nb cells by level : " + nbcells);
//
//		int start = 0;
//		int end = 0;
//		for (int stp = 0; stp < steps; stp++) {
//			end = Math.min(end + (int)(heights.length * (percents[stp]/100f)), heights.length-1);
//         float heightstart = heights[start];
//         float heightend = heights[end];
//   		for (int x = 0; x < map.getXSize(); x++) {
//   			for (int y = 0; y < map.getYSize(); y++) {
//   				HexCell cell = map.getCell(x, y);
//   				if (cell != null) {
//   					if (cell.getValue()>=heightstart && cell.getValue()<=heightend) {
//							cell.setHeight(stp);   						
//   					}
//   				}
//   			}
//   		}
//   		start = Math.min(end + 1, heights.length-1);
//		}		

	}

}
