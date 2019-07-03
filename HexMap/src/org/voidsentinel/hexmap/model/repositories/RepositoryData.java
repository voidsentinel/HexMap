/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author guipatry
 */
package org.voidsentinel.hexmap.model.repositories;

import java.util.logging.Logger;

/**
 * @author guipatry
 *
 */
public abstract class RepositoryData {
	protected static final Logger	LOG	= Logger.getLogger(RepositoryData.class.toString());

	public final String				id;

	public RepositoryData(String id) {
		this.id = id.toLowerCase();
	}

	/**
	 * add the content from the given object to the current one.
	 * 
	 * @param data the objetc whose data is to be added
	 * @exception IllegalArgumentException is the data are not of the same type or
	 *                                     if data is null
	 */
	public final void addData(RepositoryData data) throws IllegalArgumentException {
		if (data.getClass() != this.getClass() || data == null) {
			throw new IllegalArgumentException();
		}
		this.addDataParameters(data);
	};

	/**
	 * add the element from the given object to the current one. The given object
	 * will be not null and of the correct type.
	 * 
	 * @param data the objetc whose elements/content is to be added
	 * @exception IllegalArgumentException is the data are not of the same type or
	 *                                     if data is null
	 */
	public abstract void addDataParameters(RepositoryData data);

	/**
	 * add an information to the object. Since this is mostly used for object generated from
	 * config file, additionnal data can be added. 
	 * @param name  the name if the information
	 * @param value the value of the information
	 * @param additional additional data. 
	 */
	public abstract void addDataParameters(String name, String value, String additional);

}
