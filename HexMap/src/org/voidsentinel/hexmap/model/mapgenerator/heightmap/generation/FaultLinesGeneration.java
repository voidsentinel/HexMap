/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation;

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

	private int lineCount = 1;

	/**
	 * constructor
	 * 
	 * @param count
	 *           the number of fault-line to use.
	 */
	public FaultLinesGeneration(int count) {
		lineCount = count;
	}

	/**
	 * 
	 */
	public float[][] generate(int xSize, int ySize) {
		LOG.info("   Operation : " + this.getClass().getSimpleName() + " " + lineCount);
		float[][] copy = new float[ySize][xSize];
		float size = 1;
		for (int i = 0; i < lineCount; i++) {
			if (Alea.nextBoolean()) {
				size = 0.1f;
			} else {
				size = -0.1f;
			}
			if (Alea.nextBoolean()) {
				splitH(copy, size, xSize, ySize);
			} else {
				splitV(copy, size, xSize, ySize);
			}
		}
		this.normalize(copy);
		return copy;
	}

	/**
	 * set an imaginary horizontal line. Change the height of any cell over and
	 * under the line
	 * 
	 * @param heights
	 *           the map on which we work.
	 * @param variation
	 *           the height modification
	 * @param side
	 *           if 1, cell over the line are lowered and cell under are heightened.
	 *           if -1 the inverse is true.
	 */
	private float[][] splitH(float[][] heights, float variation, int xSize, int ySize) {
		int y1 = Alea.nextInt(ySize);
		int y2 = Alea.nextInt(ySize);

		for (int x = 0; x < xSize; x++) {
			int y3 = y1 + (int) (((float) (x)) / ((float) xSize) * (y2 - y1));
			for (int y = 0; y < y3; y++) {
				heights[y][x] += +variation;
			}
			for (int y = y3 + 1; y < ySize; y++) {
				heights[y][x] += -variation;
			}
		}

		return heights;
	}

	/**
	 * set an imaginary Vertical line. Change the height of any cell before and
	 * after the line
	 * 
	 * @param heights
	 *           the map on which we work.
	 * @param variation
	 *           the height modification
	 */
	private float[][] splitV(float[][] heights, float variation, int xSize, int ySize) {
		int x1 = Alea.nextInt(xSize);
		int x2 = Alea.nextInt(xSize);

		for (int y = 0; y < ySize; y++) {
			int x3 = x1 + (int) (((float) (y)) / ((float) ySize) * (x2 - x1));
			for (int x = 0; x < x3; x++) {
				heights[y][x] += variation;
			}
			for (int x = x3 + 1; x < xSize; x++) {
				heights[y][x] += -variation;
			}
		}

		return heights;
	}
}
