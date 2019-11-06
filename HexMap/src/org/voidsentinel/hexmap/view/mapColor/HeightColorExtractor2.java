/**
 * 
 */
package org.voidsentinel.hexmap.view.mapColor;

import java.awt.image.BufferedImage;

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
public class HeightColorExtractor2 extends AbstractCellColorExtractor {

	/**
	 * Constructor
	 * 
	 * @param id unique Id of the extractor
	 */
	public HeightColorExtractor2(String id) {
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
		ColorRGBA color = ColorRGBA.White.clone();
		color.multLocal(cell.getHeight());
		return color;		
	}

}
