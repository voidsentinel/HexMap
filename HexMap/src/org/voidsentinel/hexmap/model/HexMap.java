/**
 * 
 */
package org.voidsentinel.hexmap.model;

/**
 * This map manage a set of HexCell
 * 
 * @author guipatry
 *
 */
public class HexMap {
	public final int	WIDTH;
	public final int	HEIGHT;
	private HexCell[]	cells				= null;
	private int			minimumHeight	= Integer.MAX_VALUE;
	private int			maximumHeight	= Integer.MIN_VALUE;
	private HexCell	centerCell		= null;
	private float		waterHeight		= 0.2f;

	/**
	 * create an empty map of the given size
	 * 
	 * @param width
	 * @param height
	 */
	public HexMap(int width, int height) {
		WIDTH = width;
		HEIGHT = height;

		cells = new HexCell[WIDTH * HEIGHT];
		for (int z = 0; z < HEIGHT; z++) {
			for (int x = 0; x < WIDTH; x++) {
				int index = z * WIDTH + x;
				cells[index] = new HexCell(x, z);
				// set the neighbors that have already been created
				// (with reverse relation, so that all directions are filled)
				if (x > 0) {
					cells[index].setNeighbor(Direction.WEST, cells[index - 1]);
					cells[index - 1].setNeighbor(Direction.EAST, cells[index]);
				}
				if (z > 0) {
					if (z % 2 == 0) {
						cells[index].setNeighbor(Direction.SE, cells[index - WIDTH]);
						cells[index - WIDTH].setNeighbor(Direction.NW, cells[index]);
						if (x > 0) {
							cells[index].setNeighbor(Direction.SW, cells[index - WIDTH - 1]);
							cells[index - WIDTH - 1].setNeighbor(Direction.NE, cells[index]);
						}
					} else {
						cells[index].setNeighbor(Direction.SW, cells[index - WIDTH]);
						cells[index - WIDTH].setNeighbor(Direction.NE, cells[index]);
						if (x < WIDTH - 1) {
							cells[index].setNeighbor(Direction.SE, cells[index - WIDTH + 1]);
							cells[index - WIDTH + 1].setNeighbor(Direction.NW, cells[index]);
						}
					}
				}
			}
		}
		centerCell = this.getCell(WIDTH / 2, HEIGHT / 2);
	}

	/**
	 * return the cell at the given coordinate
	 * 
	 * @param coord
	 * @return
	 */
	public HexCell getCell(HexCoordinates coord) {
		return getCell(coord.column, coord.row);

	}

	/**
	 * return the cell at the center of the map
	 * 
	 * @return the cell at the center of the map
	 */
	public HexCell getCenterCell() {
		return centerCell;
	}

	public HexCell getCell(int x, int z) {
		if (x >= 0 && x < WIDTH && z >= 0 && z < HEIGHT) {
			return cells[z * WIDTH + x];
		} else {
			return null;
		}
	}

	public int getMinHeight() {
		return minimumHeight;
	}

	public int getMaxHeight() {
		return maximumHeight;
	}

	public void reCalculateProperties() {
		minimumHeight = Integer.MAX_VALUE;
		maximumHeight = Integer.MIN_VALUE;
		for (int z = 0; z < HEIGHT; z++) {
			for (int x = 0; x < WIDTH; x++) {
				int index = z * WIDTH + x;
				if (cells[index] != null) {
					int elevation = (cells[index].getIntData(HexCell.ELEVATION_DATA));
					if (elevation < minimumHeight) {
						minimumHeight = elevation;
					}
					if (elevation > maximumHeight) {
						maximumHeight = elevation;
					}
				}
			}
		}
	}

	/**
	 * @return the waterHeight
	 */
	public float getWaterHeight() {
		return waterHeight;
	}

	/**
	 * @param waterHeight the waterHeight to set
	 */
	public void setWaterHeight(float waterHeight) {
		this.waterHeight = waterHeight;
	}

}
