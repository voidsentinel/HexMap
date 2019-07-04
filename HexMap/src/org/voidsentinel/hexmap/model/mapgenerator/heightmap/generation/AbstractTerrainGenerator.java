/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation;

import org.voidsentinel.hexmap.model.mapgenerator.heightmap.AbstractTerrainAction;

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
	public float[][] generate(int xSize, int ySize) {
		return null;
	}

}
