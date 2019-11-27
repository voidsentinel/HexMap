/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.operation;

/**
 * @author guipatry
 *
 */
public class IslandOperation extends AbstractTerrainOperation {

	private float constMult = 1.35f;

	public void filter(float[][] height) {
		LOG.info("   Operation : " + this.getClass().getSimpleName());

		int xSize = height[0].length;
		int ySize = height.length;
		int xcenter = xSize / 2;
		int ycenter = ySize / 2;
		double side = Math.min(Math.pow(xcenter, 2), Math.pow(ycenter, 2));
		double maxDistance = Math.sqrt( side + side);
		for (int y = 0; y < ySize; y++) {
			for (int x = 0; x < xSize; x++) {
				double distance = Math.min(Math.sqrt(Math.pow(xcenter - x, 2) + Math.pow(ycenter - y, 2))
						/ maxDistance, 1d);
            double val = Math.pow( (Math.cos(Math.PI*distance)+1d)/2d, 2f);				
				height[y][x] = height[y][x]+(float)val;
			}
		}
		
		normalize(height);
		
	}
}
