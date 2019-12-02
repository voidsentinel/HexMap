/**
 * 
 */
package org.voidsentinel.hexmap.view.representation;

import org.voidsentinel.hexmap.repositories.RepositoryData;

/**
 * @author Xerces
 *
 */
public class MapRepresentation extends RepositoryData {

	String	className		= null;

	String	labelName		= "";
	String	materialName	= "";
	String	iconName			= "";
	String	tooltipName		= "";
	boolean	defaultMapper	= false;
	boolean	perturbated		= false;
   
	public MapRepresentation(String id) {
		super(id);
	}

	@Override
	public void addDataParameters(RepositoryData data) {
		// TODO Auto-generated method stub
	}

	/**
	 * add an information to the object. Since this is mostly used for object
	 * generated from config file, additionnal data can be added.
	 * 
	 * @param name       the name if the information
	 * @param value      the value of the information
	 * @param additional additional data.
	 */
	@Override
	public boolean addDataParameters(String name, String value, String additional) {
		boolean used = false;
		if ("class".equalsIgnoreCase(name)) {
			this.setClassName(value);
			this.defaultMapper = "true".equalsIgnoreCase(value);
			used = true;
		}
		
		if ("default".equalsIgnoreCase(name)) {
			this.defaultMapper = "true".equalsIgnoreCase(value);
			used = true;
		}
		
		if ("material".equalsIgnoreCase(name)) {
			setMaterialName(value);
			used = true;
		}

		if ("perturbation".equalsIgnoreCase(name)) {
			this.setPerturbated(Boolean.parseBoolean(value));
			used = true;
		}
		
		if ("icon".equalsIgnoreCase(name)) {
			setIconName(value);
			used = true;
		}
		if ("text".equalsIgnoreCase(name)) {
			setLabelName(value);
			used = true;
		}
		if ("tooltip".equalsIgnoreCase(name)) {
			setTooltipName(value);
			used = true;
		}
		
		return used;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return the perturbated
	 */
	public boolean isPerturbated() {
		return perturbated;
	}

	/**
	 * @param perturbated the perturbated to set
	 */
	public void setPerturbated(boolean perturbated) {
		this.perturbated = perturbated;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the labelName
	 */
	public String getLabelName() {
		return labelName;
	}

	/**
	 * @param labelName the labelName to set
	 */
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	/**
	 * @return the materialName
	 */
	public String getMaterialName() {
		return materialName;
	}

	/**
	 * @param materialName the materialName to set
	 */
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
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
	 * @return the defaultMapper
	 */
	public boolean isDefaultMapper() {
		return defaultMapper;
	}

	/**
	 * @param defaultMapper the defaultMapper to set
	 */
	public void setDefaultMapper(boolean defaultMapper) {
		this.defaultMapper = defaultMapper;
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

}
