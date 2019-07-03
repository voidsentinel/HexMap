/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author    VoidSentinel
 */
package org.voidsentinel.hexmap.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.TerrainData;

import com.jme3.math.ColorRGBA;

/**
 * @author Xerces
 *
 */
public class TerrainImage {
	private static final Logger LOG = Logger.getLogger(TerrainImage.class.toString());

	static public void generateImage(HexMap map, boolean colored) {
		LOG.info("Generating Image : " + TerrainImage.class.getSimpleName());

		BufferedImage image = new BufferedImage(map.WIDTH * 2 + 1, map.HEIGHT * 2, BufferedImage.TYPE_INT_ARGB);
		float min = findMinHeight(map);
		float max = findMaxHeight(map);
		float coeff = 255f / (max - min);
		LOG.info("   Terrain min height    : " + min);
		LOG.info("   Terrain max height    : " + max);
		int rgb = 0;

		for (int x = 0; x < map.WIDTH; x++) {
			for (int y = 0; y < map.HEIGHT; y++) {
				HexCell cell = map.getCell(x, y);
				if (!colored) {
					float height = (float) (cell.getElevation() - min) * coeff;
					float gray = Math.max(Math.min(height / 255f, 1f), 0f);
					rgb = new Color(gray, gray, gray, 1f).getRGB();
				} else {					
					TerrainData terrain = cell.getTerrain();
					if (terrain == null) {
						LOG.warning(x+"/"+y+" possess no terrain");
					}
					ColorRGBA color = terrain.getBaseColor(cell.random);
					rgb = color.asIntARGB();
				}
				if (y % 2 == 0) {
					image.setRGB(x * 2, y * 2, rgb);
					image.setRGB(x * 2 + 1, y * 2, rgb);
					image.setRGB(x * 2, y * 2 + 1, rgb);
					image.setRGB(x * 2 + 1, y * 2 + 1, rgb);
				} else {
					image.setRGB(x * 2 + 1, y * 2, rgb);
					image.setRGB(x * 2 + 2, y * 2, rgb);
					image.setRGB(x * 2 + 1, y * 2 + 1, rgb);
					image.setRGB(x * 2 + 2, y * 2 + 1, rgb);
				}
			}
		}

		File outputfile = new File("terrain.jpg");
		try {
			ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	static public void generateImage(float[][] heights) {
		LOG.info("Generating Image : " + TerrainImage.class.getSimpleName());

		BufferedImage image = new BufferedImage(heights[0].length * 2 + 1, heights.length * 2,
		      BufferedImage.TYPE_INT_ARGB);
		float min = findMinHeight(heights);
		float max = findMaxHeight(heights);
		float coeff = 1f / (max - min);
		int rgb = 0;

		for (int x = 0; x < heights[0].length; x++) {
			for (int y = 0; y < heights.length; y++) {

				float gray = Math.max(Math.min(1f, heights[y][x] * coeff), 0f);
				try {
					rgb = new Color(gray, gray, gray, 1f).getRGB();
				} catch (Exception e) {
					LOG.info("gray value : " + gray);
					return;
				}
				if (y % 2 == 0) {
					image.setRGB(x * 2, y * 2, rgb);
					image.setRGB(x * 2 + 1, y * 2, rgb);
					image.setRGB(x * 2, y * 2 + 1, rgb);
					image.setRGB(x * 2 + 1, y * 2 + 1, rgb);
				} else {
					image.setRGB(x * 2 + 1, y * 2, rgb);
					image.setRGB(x * 2 + 2, y * 2, rgb);
					image.setRGB(x * 2 + 1, y * 2 + 1, rgb);
					image.setRGB(x * 2 + 2, y * 2 + 1, rgb);
				}
			}
		}

		File outputfile = new File("heights.jpg");
		try {
			ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	static protected float findMinHeight(float[][] heights) {
		float minValue = Float.MAX_VALUE;
		for (int x = 0; x < heights[0].length; x++) {
			for (int y = 0; y < heights.length; y++) {

				if (heights[y][x] < minValue) {
					minValue = heights[y][x];
				}
			}
		}
		return minValue;
	}

	static protected float findMinHeight(HexMap map) {
		float minValue = Float.MAX_VALUE;
		if (map != null)
			for (int x = 0; x < map.WIDTH; x++) {
				for (int y = 0; y < map.HEIGHT; y++) {
					HexCell cell = map.getCell(x, y);
					if (cell != null) {
						if (cell.getElevation() < minValue) {
							minValue = cell.getElevation();
						}
					}
				}
			}
		return minValue;
	}

	static protected float findMaxHeight(HexMap map) {
		float maxValue = Float.MIN_VALUE;
		if (map != null)
			for (int x = 0; x < map.WIDTH; x++) {
				for (int y = 0; y < map.HEIGHT; y++) {
					HexCell cell = map.getCell(x, y);
					if (cell != null) {
						if (cell.getElevation() > maxValue) {
							maxValue = cell.getElevation();
						}
					}
				}
			}
		return maxValue;
	}

	static protected float findMaxHeight(float[][] heights) {
		float maxValue = Float.MIN_VALUE;
		for (int x = 0; x < heights[0].length; x++) {
			for (int y = 0; y < heights.length; y++) {

				if (heights[y][x] > maxValue) {
					maxValue = heights[y][x];
				}
			}
		}
		return maxValue;
	}

}
