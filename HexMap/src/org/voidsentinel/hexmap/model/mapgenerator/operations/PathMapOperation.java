package org.voidsentinel.hexmap.model.mapgenerator.operations;

import java.util.List;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexCoordinates;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.HexMapAStar;
import org.voidsentinel.hexmap.utils.TerrainImage;

/**
 * Set The PathPrevalence value for each cell of the map. This is done by
 * running a number of path serach, adding a small value to each cell of each
 * path. It is supposed that cell with a high number of path that goe sthrought
 * are important
 * 
 * @author voidSentinel
 *
 */
public class PathMapOperation extends AbstractMapOperation {

	@Override
	public void specificFilter(HexMap map) {
		int count = 0;

		float[][] prevalence = new float[map.HEIGHT][map.WIDTH];
		HexMapAStar star = new HexMapAStar(map);

		for (int x = 0; x < map.WIDTH; x++) {
			HexCell start = map.getCell(x, 0);
			HexCell end = map.getCell(map.WIDTH - 1 - x, map.HEIGHT - 1);
			if (start.getDistanceToWater() > 0 && end.getDistanceToWater() > 0) {
				count++;
				List<HexCell> list = star.aStarSearch(start, end);
				for (HexCell hexCell : list) {
					prevalence[hexCell.hexCoordinates.row][hexCell.hexCoordinates.column]++;
				}
			}
		}

		for (int y = 0; y < map.HEIGHT; y++) {
			HexCell start = map.getCell(0, y);
			HexCell end = map.getCell(map.WIDTH - 1, map.HEIGHT - 1 - y);
			if (start.getDistanceToWater() > 0 && end.getDistanceToWater() > 0) {
				count++;
				List<HexCell> list = star.aStarSearch(start, end);
				for (HexCell hexCell : list) {
					prevalence[hexCell.hexCoordinates.row][hexCell.hexCoordinates.column]++;
				}
			}
		}
		LOG.info("      Nb Path calculated  : " + count);

		this.normalize(prevalence);
		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(new HexCoordinates(x, y));
				cell.setData(HexCell.PATH_DATA, prevalence[y][x]);
			}
		}

		TerrainImage.generateImage(prevalence, this.getClass().getSimpleName());

	}

}
