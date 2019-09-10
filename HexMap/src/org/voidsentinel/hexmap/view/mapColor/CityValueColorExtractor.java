/**
 * 
 */
package org.voidsentinel.hexmap.view.mapColor;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;

import com.jme3.math.ColorRGBA;

/**
 * This extractor return a color based on the soil fertility level of the cell.
 * This value is a float between 0-1, and is used as the % of the width of the
 * colorMap to get the color from.
 * 
 * @author VoidSentinel
 */
public class CityValueColorExtractor extends FileMappedColorExtractor {

	/**
	 * Constructor
	 * 
	 * @param id unique Id of the extractor
	 */
	public CityValueColorExtractor(String id) {
		super(id);
	}

	/**
	 * return the global color for a cell
	 * 
	 * @param cell the cell to get the color from, based on the attribute of the
	 *             extractor
	 * @param map  the map of cell
	 * @return the color at attribute %
	 */
	protected ColorRGBA getColorSpecialized(HexCell cell, HexMap map) {
		float value;
		
		if (cell.getDistanceToWater() == 0) {
			value = 0f;
		} else {
			value = cell.getFloatData(HexCell.CITY_DATA);			      ;			
		}
		return getColorSpecialized(value);
	}

}
