/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap;

/**
 * @author Xerces
 *
 */
public abstract class AbstractTerrainOperation extends AbstractTerrainAction implements ITerrainOperation {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.voidsentinel.hexagons.model.generator.ITerrainOperation#filter(org.
	 * voidsentinel.hexagons.model.BoardMap)
	 */
	@Override
	public float[][] filter(float[][] height) {
		return height;
	}

}
