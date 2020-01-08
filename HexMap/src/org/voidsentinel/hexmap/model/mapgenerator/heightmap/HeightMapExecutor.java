/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap;

import java.util.ArrayList;
import java.util.List;

import org.voidsentinel.hexmap.model.mapgenerator.heightmap.generation.DiamondSquareGeneration;
import org.voidsentinel.hexmap.repositories.ImageRepository;
import org.voidsentinel.hexmap.repositories.RepositoryData;
import org.voidsentinel.hexmap.utils.I18nMultiFile;

/**
 * @author guipatry
 *
 */
public abstract class HeightMapExecutor extends RepositoryData implements Runnable {
	// The Presentation information
	protected String								iconName		= null;
	protected String								textName		= null;
	protected String								tooltipName	= null;
	// Parameters implementation
	protected List<ParameterDescription>	parameters	= new ArrayList<ParameterDescription>();

	// Execution information
	public enum Operation {
		ADD, MULTIPLY
	};

	protected int									xSize			= 0;
	protected int									ySize			= 0;
	protected float[][]							heightMap;
	protected HeightMapExecutor.Operation	operation	= HeightMapExecutor.Operation.MULTIPLY;
	protected volatile float					execution	= 0.0f;

	public HeightMapExecutor() {
		super(HeightMapExecutor.class.getSimpleName());
	}

	public HeightMapExecutor(String id) {
		super(id);
	}

	protected void addDataParameters(RepositoryData data) {
		HeightMapExecutor source = (HeightMapExecutor) data;
		iconName = source.iconName;
		textName = source.textName;
		tooltipName = source.tooltipName;
		parameters = source.parameters;
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
			// setTooltipName(value);
			used = true;
		}
		return used;
	}

	/**
	 * return the icon name/id in the image repository (or null if none)
	 * 
	 * @see ImageRepository
	 * @return the iconName
	 */
	final public String getIconName() {
		return iconName;
	}

	/**
	 * set the icon name/id in the image repository (or null if none)
	 * 
	 * @see ImageRepository
	 * @param iconName
	 *           the iconName associate with the extractor
	 */
	final public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	/**
	 * return the text name/id in the text repository (or null if none)
	 * 
	 * @see I18nMultiFile
	 * @return the textName
	 */
	public String getTextName() {
		return textName;
	}

	/**
	 * set the text name/id in the text repository (or null if none)
	 * 
	 * @param textName
	 *           the tetx id to associate with the extractor
	 * @see I18nMultiFile
	 */
	public void setTextName(String textName) {
		this.textName = textName;
	}

	/**
	 * return the tooltip name/id in the text repository (or null if none)
	 * 
	 * @see I18nMultiFile
	 * @return the tooltipName
	 */
	public String getTooltipName() {
		return tooltipName;
	}

	/**
	 * set the tooltip name/id in the text repository (or null if none)
	 * 
	 * @param tooltipName
	 *           the tooltipName to set
	 */
	public void setTooltipName(String tooltipName) {
		this.tooltipName = tooltipName;

	}

	/**
	 * return the list of parameters for this executor
	 * 
	 * @return
	 */
	public List<ParameterDescription> getParameters() {
		return parameters;
	}

	/**
	 * normalize the given map (aa float 2D table) to 0.0f-1.0f
	 * 
	 * @param map
	 */
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
	 * @param map
	 *           the float map
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
	 * @param map
	 *           the float map
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

	/**
	 * @return the operation
	 */
	public HeightMapExecutor.Operation getOperation() {
		return operation;
	}

	/**
	 * @param operation
	 *           the operation to set
	 */
	public void setOperation(HeightMapExecutor.Operation operation) {
		this.operation = operation;
	}

	/**
	 * @return the heightMap
	 */
	public float[][] getHeightMap() {
		return heightMap;
	}

	/**
	 * @param heightMap
	 *           the heightMap to set
	 */
	public void setHeightMap(float[][] heightMap) {
		this.heightMap = heightMap;
		this.xSize = heightMap[0].length;
		this.ySize = heightMap.length;
	}

	/**
	 * The Runnable interface. Will be called when instanciating a new task with
	 * this executor. Either run add or mulitply, depedning on the value of filed
	 * operation
	 * 
	 * @see HeightMapExecutor.Operation
	 */
	public void run() {
		LOG.info("   Operation : " + this.getClass().getSimpleName());
		if (xSize == 0 || ySize == 0) {
			LOG.info("      No valid initial map set");
		} else {
			this.performOperation();
		}
	}

	abstract public void performOperation();

}
