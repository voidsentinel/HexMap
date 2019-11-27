/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.operation;

/**
 * @author guipatry
 *
 */
public class TorusOperation extends AbstractTerrainOperation {


	public TorusOperation() {
	}

	public void filter(float[][] height) {
		LOG.info("   Operation : " + this.getClass().getSimpleName());

		double centerX = height[0].length / 2;
		double centerY = height.length / 2;

		float[][] copy = new float[height.length][height[0].length];

		for (int y = 0; y < height.length; y++) {
			for (int x = 0; x < height[0].length; x++) {
				double cx = Math.abs(x - centerX) / centerX; // range 0-1 (going from 1 to 0 to 1)
				cx = Math.pow(cx, 4d) * 0.7d;

				double cy = Math.abs(y - centerY) / centerY;
				cy = Math.pow(cy, 4d) * 0.7d;

				float v1 = height[height.length - 1 - y][x] * (float) cy / 2f;
				float v2 = height[y][height[0].length - 1 - x] * (float) cx / 2f;
				float v3 = height[height.length - 1 - y][height[0].length - 1 - x] * (float) ((cx + cy) / 4d);

				float cfinal = 1f - (float) (cx / 2d + cy / 2d + ((cx + cy) / 4d));
				float v0 = height[y][x] * cfinal;

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
