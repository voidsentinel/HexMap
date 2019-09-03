/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author VoidSentinel
 */
package org.voidsentinel.hexmap.model.repositories;

import org.voidsentinel.hexmap.model.TerrainData;

/**
 * @author guipatry
 *
 */
public class TerrainRepository extends BaseRepository<TerrainData> {

//	private static Material terrainMaterial = null;
   private static float    uvSize  = 0.5f;
	static public TerrainRepository datas = new TerrainRepository();

//	public static void setTerrainMaterial(String file) {
//		AssetManager assets = HexTuto.getInstance().getAssetManager();
//		terrainMaterial = (Material) assets.loadMaterial(file);
//	}
//
//	public static Material getTerrainMaterial() {
//		return terrainMaterial;
//	}

	public static void setUVSize(float value) {
		uvSize = value;
	}

	public static float getUVSize() {
		return uvSize;
	}

}
