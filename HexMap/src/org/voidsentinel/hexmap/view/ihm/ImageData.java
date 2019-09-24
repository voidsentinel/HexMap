/**
 * 
 */
package org.voidsentinel.hexmap.view.ihm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import org.voidsentinel.hexmap.model.repositories.RepositoryData;

/**
 * @author guipatry
 *
 */
public class ImageData extends RepositoryData {

	private String				filename;
	private BufferedImage	image	= null;

	/**
	 * Constructor for a given Id
	 * 
	 * @param id
	 */
	public ImageData(String id, String file) {
		super(id);
		this.filename = file;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.voidsentinel.hexmap.model.repositories.RepositoryData#addDataParameters(
	 * org.voidsentinel.hexmap.model.repositories.RepositoryData)
	 */
	@Override
	public void addDataParameters(RepositoryData data) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.voidsentinel.hexmap.model.repositories.RepositoryData#addDataParameters(
	 * java.lang.String, java.lang.String)
	 */
	public boolean addDataParameters(String name, String value, String additional) {
		boolean used = false;
		if ("file".equalsIgnoreCase(name)) {
			this.filename = additional +value;
			used = true;
		}
		return used;
	}

	public BufferedImage getBufferedImage() {
		if (image == null) {
			File file = new File(filename);
			try {
				image = ImageIO.read(file);
			} catch (IOException e) {
				LOG.log(Level.SEVERE, "Impossible to read file " + filename);
			}
		}
		return image;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}
}
