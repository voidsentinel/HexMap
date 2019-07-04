/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.operation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.voidsentinel.hexmap.model.Direction;
import org.voidsentinel.hexmap.model.HexCoordinates;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.operation.AbstractTerrainOperation;

/**
 * perform an erosion operation on the map. Each cell loose a little bit of it's
 * height to it's lowest neighbour if the difference between the to height is
 * big enough
 * 
 * @author guipatry
 *
 */
public class FastErosionOperation extends AbstractTerrainOperation {

	private float	maxSlope		= 0.2f;
	private int		iterations	= 1;

	/**
	 * 
	 * @param maxSlope
	 *           difference between the 2 cells that allow the erosion to perform.
	 *           This should be betwen 0 and 1 (as the heightmap is normalized)
	 * @param nbIteration
	 *           number of time we perform this
	 */
	public FastErosionOperation(float maxSlope, int nbIteration) {
		this.maxSlope = maxSlope;
		this.iterations = nbIteration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.voidsentinel.hexmap.model.mapgenerator.heightmap.AbstractTerrainOperation
	 * #filter(float[][])
	 */
	@Override
	public void filter(float[][] height) {

		float delta = 0.0f;
		float maxdelta = 0.0f;

		HexCoordinates center;
		HexCoordinates neighbor = null;
		HexCoordinates selected = null;
		boolean change = false;

		List<Direction> directions = new ArrayList<Direction>();
		for (Direction dir : Direction.values()) {
			directions.add(dir);
		}

		for (int i = 0; i < iterations; i++) {
			change = false;
			for (int y = 0; y < height.length; y++) {
				for (int x = 0; x < height[0].length; x++) {
					maxdelta = 0f;
					selected = null;
					center = new HexCoordinates(x, y);

					Collections.shuffle(directions);

					for (Direction dir : directions) {
						neighbor = center.direction(dir);
						if (neighbor.column > 0 && neighbor.column < height[0].length && neighbor.row > 0
								&& neighbor.row < height.length) {
							delta = height[y][x] - height[neighbor.row][neighbor.column];
							if (delta > maxdelta) {
								maxdelta = delta;
								selected = neighbor;
							}
						}
					}

					if (maxdelta >= maxSlope && selected != null) {
						height[y][x] = height[y][x] - maxdelta / 4f;
						height[selected.row][selected.column] = height[selected.row][selected.column] + maxdelta / 4f;
						change = true;
					}
				}
			}
		}
		if (!change) { // no need to perform remaining iteration, since there was no change
			return;
		}
	}

}
