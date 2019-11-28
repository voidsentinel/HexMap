/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.operation;

/**
 * This operation take a map and modify height so that the map is seamless :
 * left border can be joigned to right border (for horizontal seamless)
 * 
 * @author VoidSentinel
 *
 */
public class TorusOperation extends AbstractTerrainOperation {

	// direction of operation
	public enum Direction {
		vertical, horizontal, both
	};

	private Direction direction = Direction.both;

	/**
	 * 
	 * @param direction the direction of the transformation. If horizontal, only
	 *                  left and right will be matched. if vertical, top and bottom
	 *                  will be matched. if both then both operation will be done
	 */
	public TorusOperation(Direction direction) {
		this.direction = direction;
	}

	public void filter(float[][] height) {
		LOG.info("   Operation : " + this.getClass().getSimpleName());

		double centerX = height[0].length / 2;
		double centerY = height.length / 2;

		float[][] copy = new float[height.length][height[0].length];

		for (int y = 0; y < height.length; y++) {
			for (int x = 0; x < height[0].length; x++) {
				double cx = Math.abs(x - centerX) / centerX; // range 0-1 (going from 1 to 0 to 1)
				if (direction != Direction.vertical) {
					cx = Math.pow(cx, 2d);
				} else {
					cx = 0d;
				}

				double cy = Math.abs(y - centerY) / centerY;
				if (direction != Direction.horizontal) {
					cy = Math.pow(cy, 2d);
				} else {
					cy = 0d;
				}

				double cxy = (cx + cy) / 2d;
				if (direction != Direction.both) {
					cxy = 0d;
				}
				double total = 1f + cx + cy + cxy;

				float v0 = height[y][x] * (float) (1d / total);
				float v1 = height[height.length - 1 - y][x] * (float) (cy / total);
				float v2 = height[y][height[0].length - 1 - x] * (float) (cx / total);
				float v3 = height[height.length - 1 - y][height[0].length - 1 - x] * (float) (cxy / total);

				copy[y][x] = v0 + v1 + v2 + v3;
			}
		}
		for (int y = 0; y < height.length; y++) {
			for (int x = 0; x < height[0].length; x++) {
				height[y][x] = copy[y][x];
			}
		}
	}
}
