/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation;

import org.voidsentinel.hexmap.utils.Alea;

/**
 * This Generator create a map by repetively drawing a circle and changing the
 * height of all hex in this circle. As for all generators, the normalized final
 * value is returned
 * 
 * @author voidSentinel
 *
 */
public class FaultCirclesGeneration extends AbstractTerrainGenerator {

	private int			lineCount	= 1;

	private boolean	HTORUS		= true;
	private boolean	VTORUS		= true;

	/**
	 * constructor
	 * 
	 * @param count the number of fault-line to use.
	 */
	public FaultCirclesGeneration(int count) {
		lineCount = count;
	}

	/**
	 * 
	 */
	public float[][] generate(int xSize, int ySize) {
		LOG.info("   Operation : " + this.getClass().getSimpleName() + " " + lineCount);
		float[][] copy = new float[ySize][xSize];
		int size = (Math.min(xSize, ySize) / 2);

		for (int i = 0; i < lineCount; i++) {
			if (Alea.nextBoolean()) {
				splitCircle(copy, size, +0.1f);
			} else {
				splitCircle(copy, size, -0.1f);
			}
		}
		this.normalize(copy);
		return copy;
	}

	/**
	 * create a random circle and move all points in this circle up (or down)
	 * 
	 * @param heights   the map on which we work.
	 * @param maxSize   the max size of the circle to create
	 * @param variation the height modification
	 */
	private float[][] splitCircle(float[][] heights, int maxSize, float variation) {
		int xSize = heights[0].length;
		int ySize = heights.length;
		int x1 = Alea.nextInt(xSize);
		int y1 = Alea.nextInt(ySize);
		int radius = Alea.nextInt(maxSize);
		double maxDistance = radius * radius;

		for (int x = x1 - radius; x <= x1 + radius; x++) {
			for (int y = y1 - radius; y <= y1 + radius; y++) {
				int xa = x;
				int ya = y;
				if (xa < 0 && HTORUS) {
					xa = xSize + xa;
				}
				if (xa >= xSize && HTORUS) {
					xa = xa - xSize;
				}

				if (ya < 0 && VTORUS) {
					ya = ySize + ya;
				}
				if (ya >= ySize && VTORUS) {
					ya = ya - xSize;
				}

				if (xa >= 0 && xa < xSize && ya >= 0 && ya < ySize) {
					double distance = Math.pow((double) Math.abs(x1 - x), 2d) + Math.pow((double) Math.abs(y1 - y), 2d);
					if (distance <= maxDistance) {
						heights[ya][xa] = heights[ya][xa] + variation;
					}
				}
			}
		}
		return heights;
	}

}
