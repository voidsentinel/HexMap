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
	public boolean addDataParameters(String name, String value, String additional) {
		boolean used = false;
		used = super.addDataParameters(name, value, additional);
		if (!used) {
			if ("groundcolormap".equalsIgnoreCase(name)) {
				LOG.log(Level.INFO, "Loading color map " + additional + value);
				colorMap = this.getImage(additional + "/" + value);
				used = true;
			}
			if ("watercolormap".equalsIgnoreCase(name)) {
				LOG.log(Level.INFO, "Loading color map " + additional + value);
				waterColorMap = this.getImage(additional + "/" + value);
				used = true;
			}
		}
		return used;
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
		if (cell.getHeight() <= map.getWaterHeight()) {
			return getImageColor(waterColorMap, cell.getHeight() / map.getWaterHeight());
		} else {
			return getImageColor(colorMap, (cell.getHeight() - map.getWaterHeight()) / (1f - map.getWaterHeight()));
		}
	}

}
