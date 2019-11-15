/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap;

/**
 * @author guipatry
 *
 */
public abstract class HeightMapGeneration extends HeighMapTreatment {

	public HeightMapGeneration(String id) {
		super(id);
	}

	public abstract float[][] generate(int xSize, int ySize);

}
