/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap;

/**
 * @author guipatry
 *
 */
public class PowerOperation extends AbstractTerrainOperation {

	double power = 1;

	public PowerOperation(double power) {
		this.power = power;
	}

	/**
	 * Blur the
	 * 
	 * @param height : the 2D table of height to blur. Will be modified
	 * @return the blurred 2D table
	 */
	public float[][] filter(float[][] height) {
//		LOG.info("   Operation : " + this.getClass().getSimpleName());

		for (int y = 0; y < height.length; y++) {
			for (int x = 0; x < height[0].length; x++) {
				height[y][x] = (float) Math.pow(height[y][x], power);
			}
		}

		return height;
	}
}
