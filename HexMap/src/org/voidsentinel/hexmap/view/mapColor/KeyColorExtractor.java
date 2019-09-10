/**
 * 
 */
package org.voidsentinel.hexmap.view.mapColor;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.repositories.RepositoryData;

import com.jme3.math.ColorRGBA;

/**
 * The KeyColorExtractor use a string id to know which value to get from the
 * cell. It then call an abtsract function to get the color from this value
 * 
 * @author VoidSentinel
 */
public abstract class KeyColorExtractor extends AbstractCellColorExtractor {

	protected String key = "null";

	/**
	 * Public constructor
	 * 
	 * @param id the id of th extractor
	 * @see RepositoryData
	 */
	public KeyColorExtractor(String id) {
		super(id);
	}

	/**
	 * get the parameters from another extractor of the same type.
	 * 
	 * @param data another FileMappedColorExtractor whose data (image) will be used
	 * @see RepositoryData
	 */
	@Override
	public void addDataParameters(RepositoryData data) {
		this.key = ((KeyColorExtractor) (data)).key;
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
		if ("key".equalsIgnoreCase(name)) {
			this.setKey(value);
		}

	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key.trim();
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
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
			return getColorSpecialized(cell.getFloatData(key));
	}

	abstract protected ColorRGBA getColorSpecialized(float value);

}
