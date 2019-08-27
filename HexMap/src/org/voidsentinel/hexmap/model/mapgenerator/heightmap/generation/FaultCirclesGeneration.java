/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation;

import java.util.List;

import org.voidsentinel.hexmap.model.HexCoordinates;
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

	private int lineCount = 1;

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
				splitCircle(copy, size, 0.1f);
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

		HexCoordinates center = new HexCoordinates(x1, y1);
		List<HexCoordinates> list = center.inRange(radius);
		for (HexCoordinates hexCoordinates : list) {
			if (hexCoordinates.X >= 0 && hexCoordinates.X < xSize && hexCoordinates.Z >= 0 && hexCoordinates.Z < ySize) {
				heights[hexCoordinates.Z][hexCoordinates.X] = heights[hexCoordinates.Z][hexCoordinates.X] + variation;
			}
		}

		return heights;
	}

}
