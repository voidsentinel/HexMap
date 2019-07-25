/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.operation;

/**
 * @author guipatry
 *
 */
public class IslandOperation extends AbstractTerrainOperation  {

	public void filter(float[][] height) {
		LOG.info("   Operation : " + this.getClass().getSimpleName());
		
		int centerX = height[0].length / 2;
		int centerY = height.length / 2;
		float coeff = 0f;
		
		for (int y = 0; y < height.length; y++) {
			float coeffY= 1f - ((float)Math.abs(y-centerY)/ (float)(centerY)); 
			for (int x = 0; x < height[0].length; x++) {
				float coeffX= 1f - ((float)Math.abs(x-centerX)/ (float)(centerX)); 
				coeff = Math.min(coeffX,coeffY);
				height[y][x] = height[y][x]*coeff;
			}
		}

	}
}
