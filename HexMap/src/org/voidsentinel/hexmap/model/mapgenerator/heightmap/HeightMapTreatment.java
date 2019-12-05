/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap;

import org.voidsentinel.hexmap.repositories.ImageRepository;
import org.voidsentinel.hexmap.repositories.RepositoryData;
import org.voidsentinel.hexmap.utils.I18nMultiFile;

/**
 * @author guipatry
 *
 */
public abstract class HeightMapTreatment extends RepositoryData {

	protected String	iconName		= null;
	protected String	textName		= null;
	protected String	tooltipName	= null;

	
	public HeightMapTreatment() {
		super(HeightMapTreatment.class.getSimpleName());
	}
	
	public HeightMapTreatment(String id) {
		super(id);
	}

	protected void addDataParameters(RepositoryData data) {
		HeightMapTreatment source = (HeightMapTreatment) data;
		iconName = source.iconName;
		textName = source.textName;
		tooltipName = source.tooltipName;
	}
		
	public boolean addDataParameters(String name, String value, String additional) {
		boolean used = false;
		if ("icon".equalsIgnoreCase(name)) {
			setIconName(value);
			used = true;
		}
		if ("text".equalsIgnoreCase(name)) {
			setTextName(value);
			used = true;
		}
		if ("tooltip".equalsIgnoreCase(name)) {
			setTooltipName(value);
			used = true;
		}
		if ("class".equalsIgnoreCase(name)) {
			//setTooltipName(value);
			used = true;
		}
		return used;
	}

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

	
	
	
	protected void normalize(float[][] map) {

		float min = findMinHeight(map);
		float max = findMaxHeight(map);
		float coeff = (1f) / (max - min);

		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				map[y][x] = (map[y][x] - min) * coeff;
			}
		}
	}

	/**
	 * find the minimum value of a float 2D table
	 * 
	 * @param map the float map
	 * @return the min value
	 */
	protected float findMinHeight(float[][] map) {
		float minValue = Float.MAX_VALUE;
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				if (map[y][x] < minValue) {
					minValue = map[y][x];
				}
			}
		}
		return minValue;
	}

	/**
	 * find the minimum value of a float 2D table
	 * 
	 * @param map the float map
	 * @return the max value
	 */
	protected float findMaxHeight(float[][] map) {
		float maxValue = Float.MIN_VALUE;
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				if (map[y][x] > maxValue) {
					maxValue = map[y][x];
				}
			}
		}
		return maxValue;
	}

}
