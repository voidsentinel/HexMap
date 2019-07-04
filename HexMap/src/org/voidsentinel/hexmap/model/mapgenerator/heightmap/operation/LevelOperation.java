/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.operation;

/**
 * @author Xerces
 *
 */
public class LevelOperation extends AbstractTerrainOperation {

	@Override
	public void filter(float[][] height) {
		// TODO Auto-generated method stub

		float shadowValue = 0f;
		float midtoneValue = 0.75f;
		float highlightValue = 1f;
		float outShadowValue = 0f;
		float outHighlightValue = 1f;

		float gamma = 1f;
		float midtoneNormal = midtoneValue;
		if (midtoneValue < 0.5f) {
			midtoneNormal = midtoneNormal * 2;
			gamma = 1 + (9 * (1 - midtoneNormal));
			gamma = (float) Math.min(gamma, 9.99);
		} else if (midtoneValue > 0.5f) {
			midtoneNormal = (midtoneNormal * 2) - 1;
			gamma = 1 - midtoneNormal;
			gamma = (float) Math.max(gamma, 0.01);
		}
		float gammaCorrection = 1 / gamma;

		for (int y = 0; y < height.length; y++) {
			for (int x = 0; x < height[0].length; x++) {
				float channelValue = height[y][x];
				// apply input correction
				channelValue = ((channelValue - shadowValue) / (highlightValue - shadowValue));
				// apply midtone
				if (midtoneValue != 128) {
					channelValue = (float) Math.pow((double) channelValue, (double) gammaCorrection);
				}
				// apply output correction
				channelValue = channelValue * (outHighlightValue - outShadowValue) + outShadowValue;
				height[y][x] = channelValue;
			}
		}
	}

}
