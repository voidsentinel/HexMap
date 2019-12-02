/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author VoidSentinel
 */
package org.voidsentinel.hexmap.model;

import java.util.ArrayList;
import java.util.List;

import org.voidsentinel.hexmap.repositories.RepositoryData;
import org.voidsentinel.hexmap.utils.ColorParser;
import org.voidsentinel.hexmap.utils.VectorUtils;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class TerrainData extends RepositoryData {

	private ColorRGBA			colorKey		= ColorRGBA.Black;
	private float				uvSize		= 0.5f;
	public List<Vector2f>	uvCenter		= new ArrayList<Vector2f>();
	public List<ColorRGBA>	baseColors	= new ArrayList<ColorRGBA>();
	public ColorRGBA			borderColor	= null;

	/**
	 * Constructor for a given Id
	 * 
	 * @param id
	 */
	public TerrainData(String id) {
		super(id);
	}

	/**
	 * add the content from the given object to the current one
	 * 
	 * @param data the object whose content is to be added. It will be
	 */
	public void addDataParameters(RepositoryData data) throws IllegalArgumentException {
		TerrainData terrain = (TerrainData) data;
		this.uvSize = terrain.uvSize;
		this.uvCenter.addAll(terrain.uvCenter);
		this.baseColors.addAll(terrain.baseColors);
		this.colorKey = terrain.colorKey;
		if (terrain.borderColor != null) {
			this.borderColor = terrain.borderColor;
		}
	}

	@Override
	public boolean addDataParameters(String name, String value, String additional) {
		boolean used = false;
		if ("uvcenter".equalsIgnoreCase(name)) {
			this.uvCenter.add(VectorUtils.vector2fFromString(value));
			used = true;
		}
		if ("uvsize".equalsIgnoreCase(name)) {
			this.uvSize = Float.parseFloat(value);
			used = true;
		}
		if ("color".equalsIgnoreCase(name)) {
			this.baseColors.add(ColorParser.parse(value));
			used = true;
		}
		if ("colorkey".equalsIgnoreCase(name)) {
			this.colorKey = (ColorParser.parse(value));
			used = true;
		}
		return used;
	}

	public ColorRGBA getBaseColor(int val) {
		if (baseColors.isEmpty())
			return ColorRGBA.Red;
		if (baseColors.size() == 1)
			return baseColors.get(0);
		return baseColors.get(val % baseColors.size());
	}

	public Vector2f getCenterUV(int val) {
		if (uvCenter.size() == 1)
			return uvCenter.get(0);
		return uvCenter.get(val % uvCenter.size());
	}

	public float getUVSize() {
		return uvSize;
	}

	public ColorRGBA getColorKey() {
		return colorKey;
	}

	/**
	 * @param uvSize the uvSize to set
	 */
	public void setUVSize(float uvSize) {
		this.uvSize = uvSize;
	}

}
