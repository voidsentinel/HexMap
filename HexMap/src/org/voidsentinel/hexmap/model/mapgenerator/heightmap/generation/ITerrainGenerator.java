/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation;

/**
 * A TerrainGenerator is used to return a heightField created with a specific
 * method.
 * 
 * @author voidSentinal
 *
 */
public interface ITerrainGenerator {

	/**
	 * generate a heightfield
	 * 
	 * @param xSize
	 *           the xsize of the map to create
	 * @param ySize
	 *           the ysize of the map to create
	 * @return a float table of size [ySize][xSize]
	 */
	public float[][] generate(int xSize, int ySize);

}
