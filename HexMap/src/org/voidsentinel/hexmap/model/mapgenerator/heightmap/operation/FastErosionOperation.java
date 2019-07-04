/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.voidsentinel.hexmap.model.Direction;
import org.voidsentinel.hexmap.model.HexCoordinates;

/**
 * @author guipatry
 *
 */
public class FeastErosionOperation extends AbstractTerrainOperation {

	private float	maxSlope		= 0.2f;
	private int		iterations	= 1;

	public FeastErosionOperation(float maxSlope, int nbIteration) {
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
	public float[][] filter(float[][] height) {

		float delta = 0.0f;
		float maxdelta = 0.0f;

		HexCoordinates center;
		HexCoordinates neighbor = null;
		HexCoordinates selected = null;

		List<Direction> directions = new ArrayList<Direction>();
		for (Direction dir : Direction.values()) {
			directions.add(dir);
		}

		for (int i = 0; i < iterations; i++) {
			for (int y = 0; y < height.length; y++) {
				for (int x = 0; x < height[0].length; x++) {
					maxdelta = 0f;
					selected = null;
					center = new HexCoordinates(x, y);

					Collections.shuffle(directions);

					for (Direction dir : directions) {
						neighbor = center.direction(dir);
						if (neighbor.column > 0 && neighbor.column < height[0].length && neighbor.row > 0 && neighbor.row < height.length) {
							delta = height[y][x] - height[neighbor.row][neighbor.column];
							if (delta > maxdelta) {
								maxdelta = delta;
								selected = neighbor;
							}
						}
					}

//					List<HexCoordinates> neighbors = center.inRange(1);
//					Collections.shuffle(neighbors);
//					for (HexCoordinates neighbor : neighbors) {
//						if (neighbor.X > 0 && neighbor.X < height[0].length && neighbor.Z > 0 && neighbor.Z < height.length) {
//							delta = height[y][x] - height[neighbor.Z][neighbor.X];
//							if (delta > maxdelta) {
//								maxdelta = delta;
//								selected = neighbor;
//							}
//						}
//					}
					if (maxdelta >= maxSlope && selected != null) {
						height[y][x] = height[y][x] - maxdelta / 4f;
						height[selected.row][selected.column] = height[selected.row][selected.column] + maxdelta / 4f;
					}
				}
			}
		}
		return height;
	}

}
