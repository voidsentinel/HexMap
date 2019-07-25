/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author    VoidSentinel
 */
package org.voidsentinel.hexmap.model.mapgenerator.operations;

import java.util.logging.Logger;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.TerrainData;
import org.voidsentinel.hexmap.model.repositories.TerrainRepository;

/**
 * This operation set the terrain depending on characteristics
 * 
 * @author Xerces
 *
 */
public class BiomeOperation implements IMapOperation {
	private static final Logger	LOG	= Logger.getLogger(BiomeOperation.class.toString());

	private HexMap						map;
	private String[]					terrains;

	public BiomeOperation(String[] terrainstypes, int levels) {
		this.terrains = new String[terrainstypes.length * levels];
		int index = 0;
		for (String string : terrainstypes) {
			for (int i = 0; i < levels; i++) {
				terrains[index++] = string;
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
	public void filter(HexMap map) {
		LOG.info("   Operation : " + BiomeOperation.class.getSimpleName());
		LOG.info("       " + terrains.length + " terrains");

		int nblevel = terrains.length - 1;

		TerrainData standard = TerrainRepository.datas.getData("standard");
		if (standard == null) {
			LOG.severe("   Impossible de trouver le terrain standard");
		}

		int elevation = 0;
		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(x, y);
				if (cell.getHeight() <= map.getWaterHeight()) {
					elevation = (int) Math.floor(cell.getHeight() * nblevel);
				} else {
					elevation = (int) Math.floor(cell.getHeight() * nblevel + 1f);
				}
				elevation = Math.max(0, elevation);
				elevation = Math.min(nblevel, elevation);

				cell.setElevation(elevation);
				cell.setTerrain(TerrainRepository.datas.getData(terrains[elevation]));
			}
		}
		map.reCalculateProperties();
	}

}
