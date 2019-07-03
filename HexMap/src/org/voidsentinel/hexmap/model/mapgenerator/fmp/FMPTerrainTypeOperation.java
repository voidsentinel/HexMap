/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author    VoidSentinel
 */
package org.voidsentinel.hexmap.model.mapgenerator.fmp;

import java.util.logging.Logger;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexCoordinates;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.TerrainData;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.AbstractTerrainOperation;
import org.voidsentinel.hexmap.model.repositories.TerrainRepository;

import com.jme3.terrain.Terrain;

/**
 * This operation set the terrain depending on characteristics
 * 
 * @author Xerces
 *
 */
public class FMPTerrainTypeOperation extends AbstractTerrainOperation {
	private static final Logger	LOG		= Logger.getLogger(FMPTerrainTypeOperation.class.toString());

	final static String[]			terrains	= new String[] { "FMP-sea", "FMP-reef", "FMP-swamp", "FMP-plain",
	      "FMP-mountain" };
	private HexMap						map;

	public FMPTerrainTypeOperation(HexMap map) {
		this.map = map;
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
		LOG.info("   Operation : " + FMPTerrainTypeOperation.class.getSimpleName());

		TerrainData standard = TerrainRepository.datas.getData("standard");
		if (standard == null) {
			LOG.severe("   Impossible de trouver le terrain standard");
		}

		this.normalize(heights);

		for (int y = 0; y < heights.length; y++) {
			for (int x = 0; x < heights[0].length; x++) {
				HexCell cell = map.getCell(new HexCoordinates(x, y));
				cell.setElevation((int) (heights[y][x] * 100f) / 20);
				if (cell.getElevation()>=terrains.length) {
					cell.setElevation(terrains.length -1);
				}
				cell.setTerrain(TerrainRepository.datas.getData(terrains[cell.getElevation()]));
				
			}
		}

		return heights;
	}

}
