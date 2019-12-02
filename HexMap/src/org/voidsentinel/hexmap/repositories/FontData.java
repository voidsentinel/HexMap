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
public class FontData extends RepositoryData {

	protected String		filename	= null;
	protected BitmapFont	font		= null;

	/**
	 * @param id
	 */
	public FontData(String id) {
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
		this.filename = ((FontData) data).filename;
		this.font = ((FontData) data).font;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.voidsentinel.hexmap.model.repositories.RepositoryData#addDataParameters(
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addDataParameters(String name, String value, String additional) {
		boolean used = false;
		if ("file".equalsIgnoreCase(name)) {
			setFilename(additional + value);
			used = true;
		}
		return used;
	}

	/**
	 * 
	 * @return
	 */
	public BitmapFont getFont() {
		if (font == null) {
			font = GuiGlobals.getInstance().loadFont(filename);
		}
		return font;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		if (font != null) {
			// TODO : raise exception : non modifiable once font is used
		}
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
}
