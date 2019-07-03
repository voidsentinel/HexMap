/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author VoidSentinel
 */
package org.voidsentinel.hexmap.model;

import java.util.ArrayList;
import java.util.List;

import org.voidsentinel.hexmap.model.repositories.RepositoryData;
import org.voidsentinel.hexmap.utils.ColorParser;
import org.voidsentinel.hexmap.utils.VectorUtils;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;

public class TerrainData extends RepositoryData {

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
		if (terrain.borderColor != null) {
			this.borderColor = terrain.borderColor;
		}
	}

	@Override
	public void addDataParameters(String name, String value, String additional) {
		if ("uvcenter".equalsIgnoreCase(name)) {
			this.uvCenter.add(VectorUtils.vector2fFromString(value));
		}
		if ("uvsize".equalsIgnoreCase(name)) {
			this.uvSize = Float.parseFloat(value);
		}
		if ("basecolor".equalsIgnoreCase(name)) {
			this.baseColors.add(ColorParser.parse(value));
		}
	}

	public ColorRGBA getBaseColor(int val) {
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

	/**
	 * @param uvSize the uvSize to set
	 */
	public void setUVSize(float uvSize) {
		this.uvSize = uvSize;
	}

}
