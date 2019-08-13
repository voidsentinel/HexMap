/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.operations;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexCoordinates;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.AbstractTerrainAction;

/**
 * 
 * @author voidSentinel
 *
 */
public class ElevationMapOperation extends AbstractTerrainAction implements IMapOperation {

	private int	waterLevel;
	private int	groundLevel;

	/**
	 * 
	 */
	public ElevationMapOperation(int waterLevel, int groundLevel) {
		this.waterLevel = waterLevel;
		this.groundLevel = groundLevel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.voidsentinel.hexmap.model.mapgenerator.operations.IMapOperation#filter(
	 * org.voidsentinel.hexmap.model.HexMap)
	 */
	@Override
	public void filter(HexMap map) {
		LOG.info("   Operation : " + this.getClass().getSimpleName());
		LOG.info("        from  0");
		LOG.info("          to " + groundLevel + waterLevel);

		float percent = 0f;
		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(new HexCoordinates(x, y));
				float cellHeight = cell.getFloatData(HexCell.HEIGHT_DATA);
				if (cellHeight <= map.getWaterHeight()) {
					percent = cellHeight / map.getWaterHeight();
					cell.setData(HexCell.ELEVATION_DATA, (int) Math.floor(waterLevel * percent));
				} else {
					percent = (cellHeight - map.getWaterHeight()) / (1f - map.getWaterHeight());
					cell.setData(HexCell.ELEVATION_DATA, (int) Math.floor(waterLevel + groundLevel * percent));
				}
			}
		}

	}

}
