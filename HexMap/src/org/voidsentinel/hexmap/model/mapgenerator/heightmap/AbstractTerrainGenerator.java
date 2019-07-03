/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap;

/**
 * @author guipatry
 *
 */
public abstract class AbstractTerrainGenerator extends AbstractTerrainAction implements ITerrainGenerator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.voidsentinel.hexmap.model.mapgenerator.generator.ITerrainGenerator#filter
	 * (float[][])
	 */
	@Override
	public float[][] generate(float[][] data) {
		return data;
	}

}
