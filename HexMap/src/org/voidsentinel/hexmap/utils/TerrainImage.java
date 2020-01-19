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

	/**
	 * Generate an image from the map based on the TerrainData for eachCell
	 * 
	 * @param map
	 * @param colored
	 */
	static public void generateImage(HexMap map, boolean colored, String filename) {
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
				int elevation = cell.getIntData(HexCell.ELEVATION_DATA);
				if (!colored) {
					float height = (float) (elevation - min) * coeff;
					float gray = Math.max(Math.min(height / 255f, 1f), 0f);
					rgb = new Color(gray, gray, gray, 1f).getRGB();
				} else {
					TerrainData terrain = (TerrainData) cell.getData(HexCell.TERRAIN_DATA);
					if (terrain == null) {
						LOG.warning(x + "/" + y + " possess no terrain");
					}
					ColorRGBA color = terrain.getBaseColor(cell.random);
					rgb = color.asIntARGB();
				}
				if (y % 2 == 0) {
					int px = x * 2;
					int py = (map.HEIGHT * 2) - (y * 2) - 1;
					image.setRGB(px + 0, py - 0, rgb);
					image.setRGB(px + 1, py - 0, rgb);
					image.setRGB(px + 0, py - 1, rgb);
					image.setRGB(px + 1, py - 1, rgb);
				} else {
					int px = x * 2 + 1;
					int py = (map.HEIGHT * 2) - (y * 2) - 1;
					image.setRGB(px + 0, py - 0, rgb);
					image.setRGB(px + 1, py - 0, rgb);
					image.setRGB(px + 0, py - 1, rgb);
					image.setRGB(px + 1, py - 1, rgb);
				}
			}
		}

		File outputfile = new File(filename + ".png");
		try {
			ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Generate an image from the map based on the value for each cell of the key
	 * given
	 * 
	 * @param map
	 * @param key
	 *           The key to the data. This data should be a normalized float [0..1]
	 */
	static public void generateImage(HexMap map, String key, String filename) {
		LOG.info("Generating Image : " + TerrainImage.class.getSimpleName());
		BufferedImage image = new BufferedImage(map.WIDTH * 2 + 1, map.HEIGHT * 2, BufferedImage.TYPE_INT_ARGB);

		int rgb = 0;

		for (int x = 0; x < map.WIDTH; x++) {
			for (int y = 0; y < map.HEIGHT; y++) {
				HexCell cell = map.getCell(x, y);
				if (cell != null) {
					float gray = cell.getFloatData(key);
					rgb = new Color(gray, gray, gray, 1f).getRGB();
				}
				if (y % 2 == 0) {
					int px = x * 2;
					int py = (map.HEIGHT * 2) - (y * 2) - 1;
					image.setRGB(px + 0, py - 0, rgb);
					image.setRGB(px + 1, py - 0, rgb);
					image.setRGB(px + 0, py - 1, rgb);
					image.setRGB(px + 1, py - 1, rgb);
				} else {
					int px = x * 2 + 1;
					int py = (map.HEIGHT * 2) - (y * 2) - 1;
					image.setRGB(px + 0, py - 0, rgb);
					image.setRGB(px + 1, py - 0, rgb);
					image.setRGB(px + 0, py - 1, rgb);
					image.setRGB(px + 1, py - 1, rgb);
				}
			}
		}

		File outputfile = new File(filename + ".png");
		try {
			ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generate a greyscale image from the 2D array given. values should be a
	 * normalized float
	 * 
	 * @param values
	 *           the array of float to tranform into an image
	 * @param waterLevel
	 *           the value at wihi the data is considered water (and blue instead of
	 * @param filename
	 */
	static public void generateImage(float[][] values, float waterLevel, String filename) {
		LOG.info("Generating Image : " + TerrainImage.class.getSimpleName());
		BufferedImage image = getImage(values, waterLevel);
		File outputfile = new File(filename + ".png");
		try {
			ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generate a greyscale image from the 2D array given. values should be a
	 * normalized float
	 * 
	 * @param values
	 * @param filename
	 */
	static public void generateImage(float[][] values, String filename) {
		generateImage(values, 0f, filename);
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

	static public float findPercenHeight(float[][] heights, float searchedPercent) {
		final int WIDTH = heights[0].length;
		final int HEIGHT = heights.length;
		int[] number = new int[10000];
		int height = 0;
		// check how many hex at a given altitude (by increment of 1/10000)
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				height = (int) Math.max(0, Math.min(9999f, heights[y][x] * 10000f));
				number[height]++;
			}
		}

		// find what is the last height that contains < percent cells (cumulated)
		int current = 0;
		int total = HEIGHT * WIDTH;
		int limit = (int) (total * searchedPercent);
		int waterlevel = 0;
		for (int i = 0; i < number.length; i++) {
			current += number[i];
			if (current < limit) {
				waterlevel = i;
			}
		}
		return ((float) (waterlevel * 1f)) / 10000f;
	}

	static protected float findMinHeight(HexMap map) {
		float minValue = Float.MAX_VALUE;
		if (map != null)
			for (int x = 0; x < map.WIDTH; x++) {
				for (int y = 0; y < map.HEIGHT; y++) {
					HexCell cell = map.getCell(x, y);
					if (cell != null) {
						if (cell.getIntData(HexCell.ELEVATION_DATA) < minValue) {
							minValue = cell.getIntData(HexCell.ELEVATION_DATA);
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
						if (cell.getIntData(HexCell.ELEVATION_DATA) > maxValue) {
							maxValue = cell.getIntData(HexCell.ELEVATION_DATA);
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

	static protected BufferedImage getImage(float[][] values, float water) {
		final int WIDTH = values[0].length;
		final int HEIGHT = values.length;
		BufferedImage image = new BufferedImage(WIDTH * 2 + 1, HEIGHT * 2, BufferedImage.TYPE_INT_ARGB);
		float waterHeight = findPercenHeight(values, water);
		float min = 0f;
		float max = 1f;
		float coeff = 1f / (max - min);
		int rgb = 0;
		int px = 0;
		int py = 0;
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {

				float gray = Math.max(Math.min(1f, values[y][x] * coeff), 0f);
				rgb = new Color(gray, gray, gray, 1f).getRGB();
				if (values[y][x] <= waterHeight) {
					gray = Math.min(1f, gray * 2);
					rgb = new Color(gray / 2f, gray / 2f, gray, 1f).getRGB();
				}

				if (y % 2 == 0) {
					px = x * 2;
					py = (HEIGHT * 2) - (y * 2) - 1;
				} else {
					px = x * 2 + 1;
					py = (HEIGHT * 2) - (y * 2) - 1;
				}
				image.setRGB(px + 0, py - 0, rgb);
				image.setRGB(px + 1, py - 0, rgb);
				image.setRGB(px + 0, py - 1, rgb);
				image.setRGB(px + 1, py - 1, rgb);
			}
		}
		return image;
	}

}
