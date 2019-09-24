/**
 * 
 */
package org.voidsentinel.hexmap.view.mapColor;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.TerrainData;
import org.voidsentinel.hexmap.model.repositories.RepositoryData;

import com.jme3.math.ColorRGBA;

/**
 * The KeyColorExtractor use a string id to know which value to get from the
 * cell. It then call an abtsract function to get the color from this value
 * Subclasses should implement this function
 * 
 * @see HexCell
 * @author VoidSentinel
 */
public class BiomeColorExtractor extends AbstractCellColorExtractor {

	/**
	 * Public constructor
	 * 
	 * @param id the id of th extractor
	 * @see RepositoryData
	 */
	public BiomeColorExtractor(String id) {
		super(id);
	}

	/**
	 * return the global color for a cell
	 * 
	 * @param cell the cell to get the color from, based on the attribute of the
	 *             extractor
	 * @param map  the map of cell
	 * @return the color at attribute
	 */
	protected ColorRGBA getColorSpecialized(HexCell cell, HexMap map) {
		try {
			int random = cell.random;
			TerrainData terrain = cell.getTerrain();
			return terrain.getBaseColor(random);
		} catch (Exception e) {
			LOG.severe(cell.hexCoordinates.toString() + " " + e.getMessage());
			throw e;
		}
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
		return used;
	}

	/**
	 * get the parameters from another extractor of the same type.
	 * 
	 * @param data another FileMappedColorExtractor whose data (image) will be used
	 * @see RepositoryData
	 */
	@Override
	public void addDataParameters(RepositoryData data) {
	}

}
