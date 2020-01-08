/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap;

/**
 * @author guipatry
 *
 */
public abstract class HeightMapOperation extends HeightMapExecutor {

	public HeightMapOperation(String id) {
		super(id);
	}

	public abstract void affect(float[][] map);
	
}
