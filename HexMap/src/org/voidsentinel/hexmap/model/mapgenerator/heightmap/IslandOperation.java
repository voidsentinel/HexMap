/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap;

/**
 * @author guipatry
 *
 */
public class IslandOperation extends AbstractTerrainOperation {

	public float[][] generate(float[][] heights) {
		LOG.info("   Generation : " + this.getClass().getSimpleName());
		float[][] copy = new float[heights.length][heights[0].length];
		
		int centerX = heights[0].length / 2;
		int centerY = heights.length / 2;
		
		for (int y = 0; y < heights.length; y++) {
			float coeffY= 1f - ((float)Math.abs(y-centerY)/ (float)(centerY)); 
			for (int x = 0; x < heights[0].length; x++) {
				float coeffX= 1f - ((float)Math.abs(x-centerX)/ (float)(centerX)); 
				copy[y][x] = coeffX*coeffY;
				heights[y][x] = heights[y][x]*copy[y][x];
			}
		}
		this.normalize(heights);

		return copy;
	}
}
