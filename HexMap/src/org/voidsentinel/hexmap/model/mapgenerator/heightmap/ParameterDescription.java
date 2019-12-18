/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.heightmap;

/**
 * @author Xerces
 *
 */
public class ParameterDescription {
	public enum ParameterType {
		STRING, FLOAT, INT
	};

	private String name =""; 
	private ParameterType type = ParameterType.FLOAT;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param type the type of the parameter
	 */
	public void setType(ParameterType type) {
		this.type = type;
	}
	
	private ParameterType getType() {
		return type;
	}

	
}
