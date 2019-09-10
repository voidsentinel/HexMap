/**
 * 
 */
package org.voidsentinel.hexmap.view.mapColor;

import java.util.logging.Logger;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.repositories.RepositoryData;

import com.jme3.math.ColorRGBA;

/**
 * This class is the base class for a cell color extractor. Depending on a cell
 * on a map, it return the 7 colors (center, and each point)
 * 
 * @author Xerces
 *
 */
public abstract class AbstractCellColorExtractor extends RepositoryData {

	protected static final Logger	LOG				= Logger.getLogger(AbstractCellColorExtractor.class.toString());

	protected String					iconName			= null;
	protected String					textName			= null;
	protected String					tooltipName		= null;
	protected boolean					defaultMapper	= false;
	protected boolean					ignoreWater		= false;

	/**
	 * Contructor with an Id
	 * 
	 * @param id
	 */
	public AbstractCellColorExtractor(String id) {
		super(id);
	}

	/**
	 * return the base color of the cell
	 * 
	 * @param cell the cell whose color is needed.
	 * @param map  the whole map
	 * @return
	 */
	public final ColorRGBA getColor(HexCell cell, HexMap map) {
		if (!ignoreWater || !cell.getBooleanData(HexCell.UNDERWATER)) {
			return this.getColorSpecialized(cell, map);
		} else {
			return ColorRGBA.Black;
		}
	}

	/**
	 * @return the iconName
	 */
	public String getIconName() {
		return iconName;
	}

	/**
	 * @param iconName the iconName to set
	 */
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	/**
	 * add an information to the object. Since this is mostly used for object
	 * generated from config file, additionnal data can be added.
	 * 
	 * @param name       the name if the information
	 * @param value      the value of the information
	 * @param additional additional data.
	 */
	public void addDataParameters(String name, String value, String additional) {
		if ("default".equalsIgnoreCase(name)) {
			this.defaultMapper = "true".equalsIgnoreCase(value);
		}
		if ("ignorewater".equalsIgnoreCase(name)) {
			this.ignoreWater = "true".equalsIgnoreCase(value);
		}
	}

	/**
	 * @return the textName
	 */
	public String getTextName() {
		return textName;
	}

	/**
	 * @param textName the textName to set
	 */
	public void setTextName(String textName) {
		this.textName = textName;
	}

	/**
	 * @return the tooltipName
	 */
	public String getTooltipName() {
		return tooltipName;
	}

	/**
	 * @param tooltipName the tooltipName to set
	 */
	public void setTooltipName(String tooltipName) {
		this.tooltipName = tooltipName;
	}

	/**
	 * @return the ignoreWater status
	 */
	public boolean isIgnoreWater() {
		return ignoreWater;
	}

	/**
	 * set the Ignorewater status.
	 * 
	 * @param ignoreWater the status to set. If True, then
	 */
	public void setIgnoreWater(boolean ignoreWater) {
		this.ignoreWater = ignoreWater;
	}

	/**
	 * 
	 * @param cell
	 * @param map
	 * @return
	 */
	abstract protected ColorRGBA getColorSpecialized(HexCell cell, HexMap map);

}
