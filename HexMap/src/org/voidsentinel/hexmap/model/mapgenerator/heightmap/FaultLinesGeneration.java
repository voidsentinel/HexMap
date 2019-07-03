/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap;

import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.utils.Alea;

/**
 * This Generator create a map repetively drawing a line from one side of the
 * map to the other side and lowering one side and adding to the other side. As
 * for all generators, the (normalized) final value is added to the initial
 * table
 * 
 * @author voidSentinel
 *
 */
public class FaultLinesGeneration extends AbstractTerrainGenerator {

	private int							lineCount	= 1;
	private HexMap						map			= null;

	/**
	 * constructor. By defaut, the number of fault-line will be the horizontal +
	 * vertical size of the map
	 * 
	 * @param map the map to be generated
	 **/
	public FaultLinesGeneration(HexMap map) {
		this(map, 1f);
	}

	/**
	 * constructor
	 * 
	 * @param map   the map to be generated
	 * @param scale a value that is used as a coefficient to the default number of
	 *              fault-line to be created.
	 */
	public FaultLinesGeneration(HexMap map, float scale) {
		this(map, (int) ((map.WIDTH + map.HEIGHT) * scale));
	}

	/**
	 * constructor
	 * 
	 * @param map   the map to be generated
	 * @param count the number of fault-line to use.
	 */
	public FaultLinesGeneration(HexMap map, int count) {
		lineCount = count;
		this.map = map;
	}

	/**
	 * 
	 */
	public float[][] generate(float[][] heights) {
		LOG.info("   Operation : " + this.getClass().getSimpleName() + " " + lineCount);
		float[][] copy = new float[map.HEIGHT][map.WIDTH];
		float size = 1;
		for (int i = 0; i < lineCount; i++) {
			if (Alea.nextBoolean()) {
				size = 0.1f;
			} else {
				size = -0.1f;
			}
			if (Alea.nextBoolean()) {
				splitH(copy, size);
			} else {
				splitV(copy, size);
			}
		}
		this.normalize(copy);

		for (int y = 0; y < heights.length; y++) {
			for (int x = 0; x < heights[0].length; x++) {
				heights[y][x] += copy[y][x];
			}
		}

		return copy;
	}

	/**
	 * set an imaginary horizontal line. Change the height of any cell over and
	 * under the line
	 * 
	 * @param heights the map on which we work.
	 * @param size    the height modification
	 * @param side    if 1, cell over the line are lowered and cell under are
	 *                heightened. if -1 the inverse is true.
	 */
	private float[][] splitH(float[][] heights, float size) {
		int y1 = Alea.nextInt(map.HEIGHT);
		int y2 = Alea.nextInt(map.HEIGHT);

		for (int x = 0; x < map.WIDTH; x++) {
			int y3 = y1 + (int) (((float) (x)) / ((float) map.WIDTH) * (y2 - y1));
			for (int y = 0; y < y3; y++) {
				heights[y][x] += +size;
			}
			for (int y = y3 + 1; y < map.HEIGHT; y++) {
				heights[y][x] += -size;
			}
		}

		return heights;
	}

	/**
	 * set an imaginary Vertical line. Change the height of any cell before and
	 * after the line
	 * 
	 * @param map  the map on which we work.
	 * @param size the height modification
	 * @param side if 1, cell before the line are lowered and cell after are
	 *             heightened. if -1 the inverse is true.
	 */
	private float[][] splitV(float[][] heights, float size) {
		int x1 = Alea.nextInt(map.WIDTH);
		int x2 = Alea.nextInt(map.WIDTH);

		for (int y = 0; y < map.HEIGHT; y++) {
			int x3 = x1 + (int) (((float) (y)) / ((float) map.HEIGHT) * (x2 - x1));
			for (int x = 0; x < x3; x++) {
				heights[y][x] += size;
			}
			for (int x = x3 + 1; x < map.WIDTH; x++) {
				heights[y][x] += -size;
			}
		}

		return heights;
	}
}
