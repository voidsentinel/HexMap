/**
 * 
 */
package org.voidsentinel.hexmap.view.mapColor;

import java.awt.image.BufferedImage;
import java.util.logging.Level;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;

import com.jme3.math.ColorRGBA;

/**
 * This extractor return a color based on the height level of the cell. This
 * value is a float between 0-1, and is used as the % of the width of the
 * colorMap to get the color from. In the case of Height, two color Map are
 * used, depending on the fact that the cell is water or ground
 * 
 * @author VoidSentinel
 */
public class HeightColorExtractor extends FileMappedColorExtractor {

	private BufferedImage waterColorMap = null;

	/**
	 * Constructor
	 * 
	 * @param id unique Id of the extractor
	 */
	public HeightColorExtractor(String id) {
		super(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.voidsentinel.hexmap.model.repositories.RepositoryData#addDataParameters(
	 * java.lang.String, java.lang.String)
	 */
	public void addDataParameters(String name, String value, String additional) {
		super.addDataParameters(name, value, additional);      
		if ("groundColorMap".equalsIgnoreCase(name)) {
			LOG.log(Level.INFO, "Loading color map " + additional + value);
			colorMap = this.getImage(additional + value);
		}
		if ("waterColorMap".equalsIgnoreCase(name)) {
			LOG.log(Level.INFO, "Loading color map " + additional + value);
			waterColorMap = this.getImage(additional + value);
		}

	}

	/**
	 * return the global color for a cell
	 * 
	 * @param cell the cell to get the color from, based on the attribute of the
	 *             extractor
	 * @param map  the map of cell
	 * @return the color at attribute %
	 */
	public ColorRGBA getColor(HexCell cell, HexMap map) {
		if (cell.getHeight() <= map.getWaterHeight()) {
			return getColor(waterColorMap, cell.getHeight() / map.getWaterHeight());
		} else {
			return getColor(colorMap, (cell.getHeight() - map.getWaterHeight()) / (1f - map.getWaterHeight()));
		}
	}

}
