/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap;

/**
 * A TerrainGenerator is used to return a heightField created with a specific method. 
 * It get a (possibly empty) previous heighField, and will add the new generated heightfield, after having normlized it
 * @author voidSentinal
 *
 */
public interface ITerrainGenerator {

	public float[][] generate(float[][] data);

}
