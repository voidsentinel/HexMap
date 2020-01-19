/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.treatment;

import org.voidsentinel.hexmap.model.mapgenerator.heightmap.HeightMapExecutor;

/**
 * This HeightMap operation reset the height to 0.
 * 
 * @author Void sentinel
 *
 */
public class ResetOperation extends HeightMapExecutor {

	public void performOperation() {
		execution = 0f;
		float step = 1f / (1f * (xSize + ySize));
		for (int i = 0; i < xSize; i++) {
			for (int j = 0; j < ySize; j++) {
				heightMap[j][i] = 0;
				execution += step;
			}
		}
		execution = 1f;
	}

}
