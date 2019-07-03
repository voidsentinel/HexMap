/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author    VoidSentinel
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap;

import java.util.logging.Logger;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexCoordinates;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.TerrainData;
import org.voidsentinel.hexmap.model.repositories.TerrainRepository;

/**
 * This operation set the terrain depending on characteristics
 * 
 * @author Xerces
 *
 */
public class GenericTerrainTypeOperation extends AbstractTerrainOperation {
	private static final Logger	LOG	= Logger.getLogger(GenericTerrainTypeOperation.class.toString());

	private HexMap						map;
	private String[]					terrains;

	public GenericTerrainTypeOperation(HexMap map, String[] terrainstypes, int levels) {
		this.map = map;
		this.terrains = new String[terrainstypes.length * levels];
		int index = 0;
		for (String string : terrainstypes) {
			for (int i = 0; i < levels; i++) {
				terrains[index++] = string;
			}

		}

	}

	public GenericTerrainTypeOperation(HexMap map, String[] terrainsTypes, int[] terrainsCoeff) {
		this.map = map;
		int sum = 0;
		for (int i : terrainsCoeff) {
			sum += i;
		}

		this.terrains = new String[sum];
		int index = 0;
		for (int t = 0; t < terrainsTypes.length; t++) {
			for (int i = 0; i < terrainsCoeff[t]; i++) {
				terrains[index++] = terrainsTypes[t];
			}
		}
	}

	/**
	 * Set the terrain depending on it's height. <ui>
	 * <li>0 = sea
	 * <li>1 = reef
	 * <li>2 = swamp
	 * <li>3 = plains
	 * <li>4 = moutains
	 * </ul>
	 */
	public float[][] filter(float[][] heights) {
		LOG.info("   Operation : " + GenericTerrainTypeOperation.class.getSimpleName());
		LOG.info("       " + terrains.length + " terrains");

		int nblevel = terrains.length - 1;
		float stepsize = 1f / terrains.length;

		TerrainData standard = TerrainRepository.datas.getData("standard");
		if (standard == null) {
			LOG.severe("   Impossible de trouver le terrain standard");
		}

		this.normalize(heights);
		int elevation = 0;
		for (int y = 0; y < heights.length; y++) {
			for (int x = 0; x < heights[0].length; x++) {
				HexCell cell = map.getCell(new HexCoordinates(x, y));
				if (cell.getHeight() <= map.getWaterHeight()) {
					elevation = (int) Math.floor(heights[y][x]* nblevel);
				} else {
					elevation = (int) Math.floor(heights[y][x] * nblevel+1f);
				}
				elevation = Math.max(0, elevation);
				elevation = Math.min(nblevel, elevation);

				cell.setElevation(elevation);
				cell.setHeight(heights[y][x]);
				cell.setTerrain(TerrainRepository.datas.getData(terrains[elevation]));
			}
		}

		return heights;
	}

}
