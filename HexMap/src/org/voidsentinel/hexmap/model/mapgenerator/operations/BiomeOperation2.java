/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author    VoidSentinel
 */
package org.voidsentinel.hexmap.model.mapgenerator.operations;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.TerrainData;
import org.voidsentinel.hexmap.model.repositories.TerrainRepository;
import org.voidsentinel.hexmap.utils.Alea;
import org.voidsentinel.hexmap.utils.ColorParser;
import org.voidsentinel.hexmap.utils.TerrainImage;

import com.jme3.math.ColorRGBA;

/**
 * This operation set the terrain / biome depending on humidity and temperature
 * This use a bitmap. The pixel will depend on humidity (y) and temperature (x).
 * The pixel's color will be used to find the terrain with the corresponding
 * colorKey. (If several terrain exist that possess the same colorKey, one will
 * be taken at random)
 * 
 * @author voidSentinel
 *
 */

public class BiomeOperation2 extends AbstractMapOperation {

	private BufferedImage							biomeMap			= null;
	private Map<ColorRGBA, List<TerrainData>>	terrains			= new HashMap<ColorRGBA, List<TerrainData>>();
	private int											terrainCount	= 0;

	/**
	 * 
	 * @param filename the filename of the biome bitmap
	 */
	public BiomeOperation2(String filename) {
		// get the image
		biomeMap = this.getImage(filename);
		// file the available biomes
		Iterator<String> it = TerrainRepository.terrains.datas.keySet().iterator();
		while (it.hasNext()) {
			TerrainData data = TerrainRepository.terrains.getData(it.next());
			ColorRGBA key = data.getColorKey();
			LOG.info("               " + data.id + " color " + key.toString());
			List<TerrainData> list = terrains.get(key);
			if (list == null) {
				list = new ArrayList<TerrainData>();
				terrains.put(key, list);
			}
			list.add(data);
			terrainCount++;
		}
		LOG.info("               " + terrainCount + " referenced terrains in");
		LOG.info("               " + terrains.size() + " biomes");
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

	/**
	 * Set the terrain depending on it's height. <ui>
	 * <li>0 = sea
	 * <li>1 = reef
	 * <li>2 = swamp
	 * <li>3 = plains
	 * <li>4 = moutains
	 * </ul>
	 */
	public void specificFilter(HexMap map) {
		TerrainData standard = TerrainRepository.terrains.getData("standard");
		TerrainData ocean = TerrainRepository.terrains.getData("Ocean");

		if (ocean == null) {
			LOG.severe("   Impossible de trouver le terrain ocean");
		}
		if (standard == null) {
			LOG.severe("   Impossible de trouver le terrain standard");
		}
		for (int y = 0; y < map.HEIGHT; y++) {
			for (int x = 0; x < map.WIDTH; x++) {
				HexCell cell = map.getCell(x, y);
				if (cell != null) {
					float cellHeight = cell.getFloatData(HexCell.HEIGHT_DATA);
					float humidity = cell.getFloatData(HexCell.HUMIDITY_DATA);
					float temperature = cell.getFloatData(HexCell.TEMPERATURE_DATA);
					if (cellHeight <= map.getWaterHeight()) {
						cell.setData(HexCell.TERRAIN_DATA, ocean);
					} else {
						// change them into pixel coordinate
						int hpixel = (int) (humidity * (biomeMap.getHeight() - 1));
						hpixel = Math.max(Math.min(hpixel, biomeMap.getHeight() - 1), 0);
						int tpixel = (int) (temperature * (biomeMap.getWidth() - 1));
						tpixel = Math.max(Math.min(tpixel, biomeMap.getWidth() - 1), 0);
						// and get the pixel
						int clr = biomeMap.getRGB(tpixel, hpixel);
						ColorRGBA color = ColorParser.parse(clr);
						// now search all terrain that are linked to this color as key
						List<TerrainData> choosen = terrains.get(color);
						if (choosen == null) {
							LOG.severe("Pb : No terrain found for color " + color + " at " + tpixel + "/" + hpixel);
							cell.setData(HexCell.TERRAIN_DATA, standard);
						} else {
							cell.setData(HexCell.TERRAIN_DATA, choosen.get(Alea.nextInt(choosen.size())));
						}
					}
				}
			}
		}
		TerrainImage.generateImage(map, true, this.getClass().getSimpleName());
	}

}
