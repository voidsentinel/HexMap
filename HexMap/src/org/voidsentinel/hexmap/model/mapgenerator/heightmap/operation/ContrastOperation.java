/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.operation;

import org.voidsentinel.hexmap.utils.TerrainImage;

/**
 * @author Xerces
 *
 */
public class ContrastOperation extends AbstractTerrainOperation {

	private float contrast = 32f;

	@Override
	public void filter(float[][] height) {
		// TODO Auto-generated method stub

		TerrainImage.generateImage(height, "before");

		float factor = (259 * (contrast + 255f)) / (255 * (259 - contrast));

		for (int y = 0; y < height.length; y++) {
			for (int x = 0; x < height[0].length; x++) {
				float channelValue = height[y][x] * 255;

				channelValue = (channelValue - 128f) * factor + 128f;
				channelValue = Math.min(channelValue, 255f);
				channelValue = Math.max(0f, channelValue);
				height[y][x] = channelValue / 255f;

			}
		}

		TerrainImage.generateImage(height, "after");
		
	}

}
