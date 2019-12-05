/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author    VoidSentinel
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation;

/**
 * @author Xerces 
 * 
 */
public class IslandGeneration extends AbstractTerrainGenerator {

	double power = 1f;

	/**
	 * Constructor
	 * 
	 * @param scale used to scale the coordinates by multipliing it in the simplex
	 *              noise. Higher values give flatter map, since the values between
	 *              2 points change more slowly
	 */
	public IslandGeneration(double power) {
		this.power = power;
	}

	public float[][] generate(int xSize, int ySize) {
		LOG.info("   Operation : " + IslandGeneration.class.getSimpleName());
		float[][] copy = new float[ySize][xSize];

		int xcenter = xSize / 2;
		int ycenter = ySize / 2;
		double side = Math.min(Math.pow(xcenter, 2), Math.pow(ycenter, 2));
		double maxDistance = Math.sqrt( side + side);
		for (int y = 0; y < ySize; y++) {
			for (int x = 0; x < xSize; x++) {
				double distance = Math.min(Math.sqrt(Math.pow(xcenter - x, 2) + Math.pow(ycenter - y, 2))
						/ maxDistance, 1d);
            double height = Math.pow( (Math.cos(Math.PI*distance)+1d)/2d, power);				
				copy[y][x] = (float)height;
			}
		}
		this.normalize(copy);

		return copy;
	}

}
