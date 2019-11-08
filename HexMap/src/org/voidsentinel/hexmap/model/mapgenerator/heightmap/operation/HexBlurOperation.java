/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.operation;

import java.util.logging.Logger;

import org.voidsentinel.hexmap.model.Direction;
import org.voidsentinel.hexmap.model.HexCoordinates;

/**
 * Blur the heights using 6-neighborgs (ie supposing an hex representation of
 * the map
 * 
 * @author voidSentinel
 *
 */
public class HexBlurOperation extends AbstractTerrainOperation {

	private static final Logger	LOG			= Logger.getLogger(HexBlurOperation.class.toString());

	private int							centerCoeff	= 1;
	private int							outerCoeff	= 1;

	/**
	 * constructor for the Blur operation
	 * 
	 * @param centerCoeff
	 *           weight of the center value
	 * @param outerCoeff
	 *           weight of each of the neighborgs
	 */
	public HexBlurOperation(int centerCoeff, int outerCoeff) {
		this.centerCoeff = centerCoeff;
		this.outerCoeff = outerCoeff;
	}

	public HexBlurOperation() {
		this(1, 1);
	}

	/**
	 * Blur the table using the coefficient defined in constructor
	 * 
	 * @param height
	 *           : the 2D table of height to blur. Will be modified
	 * @return the blurred 2D table
	 */
	public void filter(float[][] height) {
		LOG.info("   Operation : " + this.getClass().getSimpleName());

		float[][] copy = new float[height.length][height[0].length];

		HexCoordinates center;
		HexCoordinates neighbor;
		float value = 0;
		int count;
		for (int y = 0; y < height.length; y++) {
			for (int x = 0; x < height[0].length; x++) {
				center = new HexCoordinates(x, y);
				value = height[y][x] * centerCoeff;
				count = centerCoeff;
				for (Direction dir : Direction.values()) {
					neighbor = center.direction(dir);
					if (neighbor.column > 0 && neighbor.column < height[0].length && neighbor.row > 0
							&& neighbor.row < height.length) {
						value = value + height[neighbor.row][neighbor.column] * outerCoeff;
						count = count + outerCoeff;
					}
				}

				copy[y][x] = value / (float) count;
			}
		}

//		this.normalize(copy);

		for (int y = 0; y < height.length; y++) {
			for (int x = 0; x < height[0].length; x++) {
				height[y][x] = copy[y][x];
			}
		}
	}

}
