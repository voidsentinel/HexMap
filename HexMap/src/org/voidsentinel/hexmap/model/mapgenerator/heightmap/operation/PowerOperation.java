/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.operation;

/**
 * Change the value of each cell to it's nth power, the n being set in
 * constructor
 * 
 * @author voidSentinel
 *
 */
public class PowerOperation extends AbstractTerrainOperation {

	double power = 1;
	double multiplier = 1;

	public PowerOperation(double power, double multiplier) {
		this.power = power;
		this.multiplier = multiplier;
	}

	/**
	 * Blur the
	 * 
	 * @param height
	 *           : the 2D table of height to blur. Will be modified
	 * @return the blurred 2D table
	 */
	public void filter(float[][] height) {
		LOG.info("   Operation : " + this.getClass().getSimpleName() + "(" + power + ")");
		for (int y = 0; y < height.length; y++) {
			for (int x = 0; x < height[0].length; x++) {
				height[y][x] = (float) (Math.pow(height[y][x], power)*multiplier);
			}
		}

	}
}
