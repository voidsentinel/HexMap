/**
 * 
 */
package org.voidsentinel.hexmap.repositories;

import java.io.InputStream;

import com.jme3.asset.AssetInfo;
import com.jme3.font.BitmapFont;
import com.jme3.font.plugins.BitmapFontLoader;
import com.simsilica.lemur.GuiGlobals;

/**
 * @author guipatry
 *
 */
public class GenericData extends RepositoryData {

	protected enum DataType {
		FLOAT_TYPE, INT_TYPE, STRING_TYPE
	};

	protected Object		data	= null;
	protected DataType	type	= DataType.STRING_TYPE;

	/**
	 * @param id
	 */
	public GenericData(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.voidsentinel.hexmap.model.repositories.RepositoryData#addDataParameters(
	 * org.voidsentinel.hexmap.model.repositories.RepositoryData)
	 */
	@Override
	protected void addDataParameters(RepositoryData data) {
		this.data = ((GenericData) data).data;
	}

	/*
	 * type (non-Javadoc)
	 * 
	 * @see
	 * org.voidsentinel.hexmap.model.repositories.RepositoryData#addDataParameters(
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addDataParameters(String name, String value, String additional) {
		boolean used = false;
		if ("type".equalsIgnoreCase(name)) {
			used = true;
		}
		if ("value".equalsIgnoreCase(name)) {
			used = true;
		}
		return used;
	}

	public void setData(DataType type, String value) {
		this.type = type;
	}

}
