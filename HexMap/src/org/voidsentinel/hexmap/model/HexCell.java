/**
 * 
 */
package org.voidsentinel.hexmap.model;

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

	// general data
	private float						height				= 0.5f;
	private int							elevation			= 0;												// the elevation of the Hex
	private float						temperature			= 0.5f;
	private float						humidity				= 0f;
	private float						fertility			= 0.5f;
	private boolean					hasCity				= false;

	// technical data
	private int							distanceToWater	= -1;												// unknown
	private float						pathPrevalence		= 0f;												// path going by this point
	private float						cityValue			= 0f;
	private HexCell[]					neighbor				= new HexCell[Direction.NBDIRECTIONS];	// neighbors

	// biome
	private TerrainData				terrain				= null;

	public HexCell(int x, int z) {
		hexCoordinates = new HexCoordinates(x, z);
		random = Alea.nextInt(256);
		elevation = Alea.nextInt(3);
	}

	public void setElevation(int elevation) {
		this.elevation = elevation;
	}

	public int getElevation() {
		return elevation;
	}

	public HexCell getNeighbor(Direction direction) {
		return neighbor[direction.ordinal()];
	}

	public void setNeighbor(Direction direction, HexCell cell) {
		neighbor[direction.ordinal()] = cell;
	}

	/**
	 * @return the terrain
	 */
	public TerrainData getTerrain() {
		return terrain;
	}

	/**
	 * @param terrain
	 *           the terrain to set
	 */
	public void setTerrain(TerrainData terrain) {
		this.terrain = terrain;
	}

	/**
	 * @return the temperature
	 */
	public float getTemperature() {
		return temperature;
	}

	/**
	 * @param temperature
	 *           the temperature to set
	 */
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	/**
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * @param height
	 *           the height to set
	 */
	public void setHeight(float height) {
		this.height = height;
	}

	/**
	 * @return the humidity
	 */
	public float getHumidity() {
		return humidity;
	}

	/**
	 * @param humidity
	 *           the humidity to set
	 */
	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}

	/**
	 * @return the neighbor
	 */
	public HexCell[] getNeighbor() {
		return neighbor;
	}

	/**
	 * @return the fertility
	 */
	public float getFertility() {
		return fertility;
	}

	/**
	 * @param fertility
	 *           the fertility to set
	 */
	public void setFertility(float fertility) {
		this.fertility = fertility;
	}

	/**
	 * @return the distanceToWater
	 */
	public int getDistanceToWater() {
		return distanceToWater;
	}

	/**
	 * @param distanceToWater
	 *           the distanceToWater to set
	 */
	public void setDistanceToWater(int distanceToWater) {
		this.distanceToWater = distanceToWater;
	}

	/**
	 * @return the pathPrevalence
	 */
	public float getPathPrevalence() {
		return pathPrevalence;
	}

	/**
	 * @param pathPrevalence
	 *           the pathPrevalence to set
	 */
	public void setPathPrevalence(float pathPrevalence) {
		this.pathPrevalence = pathPrevalence;
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

	/**
	 * @return the cityValue
	 */
	public float getCityValue() {
		return cityValue;
	}

	/**
	 * @param cityValue
	 *           the cityValue to set
	 */
	public void setCityValue(float cityValue) {
		this.cityValue = cityValue;
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

}
