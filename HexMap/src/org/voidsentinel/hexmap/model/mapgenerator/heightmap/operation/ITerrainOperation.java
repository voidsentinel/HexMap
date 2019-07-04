/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap.operation;

/**
 * a TerrainOperation perform a specific operation on a heightfield, modifying
 * it
 * 
 * @author voidSentinel
 *
 */
public interface ITerrainOperation {

	/**
	 * Act on the given map height to perform a specific operation
	 * 
	 * @param map
	 *           the heightfield that will be modified.
	 */
	public void filter(float[][] height);

}
