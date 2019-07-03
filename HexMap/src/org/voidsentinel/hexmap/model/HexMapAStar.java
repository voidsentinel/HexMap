/**
 * 
 */
package org.voidsentinel.hexmap.model;

import java.util.Arrays;
import java.util.List;

/**
 * Implementation of the AStar Algorithm for the HexMap / HexCell
 * 
 * @author voidSentinel
 *
 */
public class HexMapAStar extends AbstractAStar<HexCell> {

	// the map used to
	private HexMap						map	= null;

	public HexMapAStar(HexMap map) {
		this.map = map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.voidsentinel.hexmap.model.AbstractAStar#getNeihbours(java.lang.Object)
	 */
	@Override
	public List<HexCell> getNeihbours(HexCell source) {
		return Arrays.asList(source.getNeighbor());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.voidsentinel.hexmap.model.AbstractAStar#getCost(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public float getCost(HexCell source, HexCell dest) {
		if (dest.getDistanceToWater() == 0) {
			return 10000;
		}
		float value = 1f;
		value = value + (float)Math.pow((double)Math.abs(source.getElevation() - dest.getElevation()), (double)(1.5f+dest.getHeight()));
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.voidsentinel.hexmap.model.AbstractAStar#getDistance(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public float getDistance(HexCell source, HexCell dest) {
		return source.hexCoordinates.distance(dest.hexCoordinates);
	}

}
