/**
 * 
 */
package org.voidsentinel.hexmap.view.mapColor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import org.voidsentinel.hexmap.model.repositories.RepositoryData;

import com.jme3.math.ColorRGBA;

/**
 * The FileMappedColorExtractor use an image to get the requested color. a int
 * value between 0 and imageWidth-1 is needed and will be used to get the color
 * at pixel (value, 0).
 * 
 * @author VoidSentinel
 */
public class FileMappedColorExtractor extends KeyColorExtractor {

	protected BufferedImage colorMap = null;

	/**
	 * Public constructor
	 * 
	 * @param id the id of th extractor
	 * @see RepositoryData
	 */
	public FileMappedColorExtractor(String id) {
		super(id);
	}

	/**
	 * get the parameters from another extractor of the same type.
	 * 
	 * @param data another FileMappedColorExtractor whose data (image) will be used
	 * @see RepositoryData
	 */
	@Override
	public void addDataParameters(RepositoryData data) {
		super.addDataParameters(data);
		this.colorMap = ((FileMappedColorExtractor) (data)).colorMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.voidsentinel.hexmap.model.repositories.RepositoryData#addDataParameters(
	 * java.lang.String, java.lang.String)
	 */
	public void addDataParameters(String name, String value, String additional) {
		super.addDataParameters(name, value, additional);
		if ("colorMap".equalsIgnoreCase(name)) {
			this.setColorMap(additional + value);
		}
	}

	public void setColorMap(String filename) {
		LOG.log(Level.INFO, "Loading color map " + filename);
		colorMap = this.getImage(filename);
	}

	/**
	 * return the color based on float value and the instance colorMap. The value
	 * is converted into a int value between 0 and the imagewidth. The returned
	 * color is the color of the pixel at this point.
	 * 
	 * @param value the % of the image to get
	 * @return the color at (value%, 0) in the image
	 */
	protected ColorRGBA getColor(float value) {
		return getColor(colorMap, value);
	}

	/**
	 * return the color based on float value and a given colorMap. The value is
	 * converted into a int value between 0 and the imagewidth. The returned color
	 * is the color of the pixel at this point.
	 * 
	 * @param value    the % of the image to get
	 * @param colorMap the colorMap to use
	 * @return the color at (value%, 0) in the image
	 */
	protected ColorRGBA getColor(BufferedImage colorMap, float value) {
		int index = Math.min(Math.max((int) ((colorMap.getWidth() - 1) * value), 0), colorMap.getWidth() - 1);

		int clr = colorMap.getRGB(index, 0);
		int red = (clr & 0x00ff0000) >> 16;
		int green = (clr & 0x0000ff00) >> 8;
		int blue = clr & 0x000000ff;

		return new ColorRGBA(red / 255f, green / 255f, blue / 255f, 1f);

	}

	/**
	 * get the image from the filename
	 * 
	 * @param filename
	 * @return
	 */
	protected BufferedImage getImage(String filename) {
		File file = new File(filename);
		try {
			return ImageIO.read(file);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, "Impossible to read file " + filename);
		}
		return null;
	}

}
