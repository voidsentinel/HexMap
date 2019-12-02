/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author VoidSentinel
 */
package org.voidsentinel.hexmap.repositories;

import org.voidsentinel.hexmap.model.TerrainData;

/**
 * @author guipatry
 *
 */
public class TerrainRepository extends BaseRepository<TerrainData> {

//	private static Material terrainMaterial = null;
   private static float    uvSize  = 0.5f;
	static public TerrainRepository terrains = new TerrainRepository();

	public static void setUVSize(float value) {
		uvSize = value;
	}

	public static float getUVSize() {
		return uvSize;
	}

}
