/**
 * 
 */
package org.voidsentinel.hexmap.model;

import java.util.HashMap;
import java.util.Map;

import org.voidsentinel.hexmap.utils.Alea;

/**
 * This class is a cell in the map. It possess come caracteristics
 * 
 * @author guipatry
 *
 */
public class HexCell {

	public final HexCoordinates	hexCoordinates;
	public final int					random;

	public static final String		HEIGHT_DATA			= "height";
	public static final String		ELEVATION_DATA		= "elevation";
	public static final String		TEMPERATURE_DATA	= "temperature";
	public static final String		HUMIDITY_DATA		= "humidity";
	public static final String		SOIL_DATA			= "fertility";
	public static final String		CITY_DATA			= "city";
	public static final String		PATH_DATA			= "path";
	public static final String		TERRAIN_DATA		= "biome";

	// neighbor cells
	private HexCell[]					neighbor				= new HexCell[Direction.NBDIRECTIONS];	// neighbors
	
	// generic Datas associated with the cell
	private Map<String, Object>	datas					= new HashMap<String, Object>();

	// to be removed later
	private boolean					hasCity				= false;
	private int							distanceToWater	= -1;												// unknown


	public HexCell(int x, int z) {
		hexCoordinates = new HexCoordinates(x, z);
		random = Alea.nextInt(256);
	}

	/**
	 * Associate a value to the cell
	 * 
	 * @param key   key foir the value
	 * @param value the value
	 */
	public void setData(String key, int value) {
		datas.put(key, new Integer(value));
	}

	/**
	 * Associate a value to the cell
	 * 
	 * @param key   key for the value
	 * @param value the value
	 */
	public void setData(String key, float value) {
		datas.put(key, new Float(value));
	}

	/**
	 * Associate an object to the cell
	 * 
	 * @param key   key for the value
	 * @param value the object
	 */
	public void setData(String key, Object value) {
		datas.put(key, value);
	}

	/**
	 * return the object associated with the key in this cell
	 * 
	 * @param key
	 * @return
	 */
	public Object getData(String key) {
		return datas.get(key);
	}

	/**
	 * return the value associated with the key in this cell. Equivalent to
	 * ((Float)getData(key)).toFloatValue
	 * 
	 * @param key
	 * @return
	 */
	public float getFloatData(String key) {
		return ((Float) datas.get(key)).floatValue();
	}

	/**
	 * return the value associated with the key in this cell. Equivalent to
	 * ((Boolean)getData(key)).BooleanValue
	 * 
	 * @param key
	 * @return the boolean value associated to the key
	 */
	public boolean getBooleanData(String key) {
		return ((Boolean) datas.get(key)).booleanValue();
	}

	/**
	 * return the value associated with the key in this cell. Equivalent to
	 * ((Boolean)getData(key)).BooleanValue
	 * 
	 * @param key
	 * @return the boolean value associated to the key
	 */
	public int getIntData(String key) {
		return ((Integer) datas.get(key)).intValue();
	}

	/**
	 * a shortcut to get height data as a float
	 * 
	 * @return
	 */
	public float getHeight() {
		return getFloatData(HEIGHT_DATA);
	}

	/**
	 * a shortcut to get Elevation data as an int
	 * 
	 * @return
	 */
	public int getElevation() {
		return getIntData(ELEVATION_DATA);
	}

	public TerrainData getTerrain() {
		return (TerrainData) getData(TERRAIN_DATA);
	}

	public HexCell getNeighbor(Direction direction) {
		return neighbor[direction.ordinal()];
	}

	public void setNeighbor(Direction direction, HexCell cell) {
		neighbor[direction.ordinal()] = cell;
	}

	/**
	 * @return the neighbor
	 */
	public HexCell[] getNeighbor() {
		return neighbor;
	}

	/**
	 * @return the distanceToWater
	 */
	public int getDistanceToWater() {
		return distanceToWater;
	}

	/**
	 * @param distanceToWater the distanceToWater to set
	 */
	public void setDistanceToWater(int distanceToWater) {
		this.distanceToWater = distanceToWater;
	}

	/**
	 * @return the hasCity
	 */
	public boolean isHasCity() {
		return hasCity;
	}

	/**
	 * @param hasCity the hasCity to set
	 */
	public void setHasCity(boolean hasCity) {
		this.hasCity = hasCity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hexCoordinates == null) ? 0 : hexCoordinates.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		HexCell other = (HexCell) obj;
		if (hexCoordinates == null) {
			if (other.hexCoordinates != null) {
				return false;
			}
		} else if (!hexCoordinates.equals(other.hexCoordinates)) {
			return false;
		}
		return true;
	}

}
