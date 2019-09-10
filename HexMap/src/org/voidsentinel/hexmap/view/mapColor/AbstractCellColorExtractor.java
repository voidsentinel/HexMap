/**
 * 
 */
package org.voidsentinel.hexmap.view.mapColor;

import java.util.logging.Logger;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.repositories.RepositoryData;
import org.voidsentinel.hexmap.utils.I18nMultiFile;
import org.voidsentinel.hexmap.view.ihm.ImageRepository;

import com.jme3.math.ColorRGBA;

/**
 * This class is the base class for a cell color extractor. Depending on the
 * implementation it return the base color of the cell
 * 
 * @author Void Sentinel
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
	 * @return the color of the cell, or black if the cell is underwater and
	 *         ignoreWater is set
	 */
	public final ColorRGBA getColor(HexCell cell, HexMap map) {
		if (!ignoreWater || !cell.getBooleanData(HexCell.UNDERWATER)) {
			return this.getColorSpecialized(cell, map);
		} else {
			return ColorRGBA.Black;
		}
	}

	/**
	 * Specific implementation of the extractor by a child
	 * 
	 * @param cell the cell whose color is needed. If ignoreWtare is set, it will
	 *             not be called for underwater cells
	 * @param map  the whole map
	 * @return the color of the cell
	 */
	abstract protected ColorRGBA getColorSpecialized(HexCell cell, HexMap map);

	/**
	 * The icon name for the color extractor (to be used in IHM)
	 * 
	 * @see ImageRepository
	 * @return the iconName
	 */
	final public String getIconName() {
		return iconName;
	}

	/**
	 * set the icon name for the color extractor (to be used in IHM)
	 * 
	 * @see ImageRepository
	 * @param iconName the iconName associate with the extractor
	 */
	final public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	/**
	 * add an information to the object. Since this is mostly used for object
	 * generated from config file, additionnal data can be added.
	 * <ul>
	 * <li>default : is the extractor the default one ?</li>
	 * <li>ignorewater : is the extractor supposed to be used for water (false) or
	 * not (true)</li>
	 * </ul>
	 * This method should be called first by child implementation
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
	 * return the textId of the extractor name
	 * 
	 * @see I18nMultiFile
	 * @return the textName
	 */
	public String getTextName() {
		return textName;
	}

	/**
	 * set the textid of the extractor's name.
	 * 
	 * @param textName the tetx id to associate with the extractor
	 */
	public void setTextName(String textName) {
		this.textName = textName;
	}

	/**
	 * return the textId of the extractor's tooltip
	 * 
	 * @see I18nMultiFile
	 * @return the tooltipName
	 */
	public String getTooltipName() {
		return tooltipName;
	}

	/**
	 * set the textid of the extractor's tooltip.
	 * 
	 * @param tooltipName the tooltipName to set
	 */
	public void setTooltipName(String tooltipName) {
		this.tooltipName = tooltipName;
	}

	/**
	 * indicate if water should have a default (black) color (true) or not
	 * 
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

}
