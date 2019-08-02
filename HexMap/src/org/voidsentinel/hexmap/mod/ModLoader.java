/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author    VoidSentinel
 */
package org.voidsentinel.hexmap.mod;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.voidsentinel.hexmap.model.TerrainData;
import org.voidsentinel.hexmap.model.repositories.TerrainRepository;
import org.voidsentinel.hexmap.utils.ColorParser;
import org.voidsentinel.hexmap.utils.I18nMultiFile;
import org.voidsentinel.hexmap.utils.VectorUtils;
import org.voidsentinel.hexmap.view.ihm.ImageData;
import org.voidsentinel.hexmap.view.ihm.ImageRepository;
import org.voidsentinel.hexmap.view.mapColor.AbstractCellColorExtractor;
import org.voidsentinel.hexmap.view.mapColor.colorMapperRepository;
import org.voidsentinel.hexmap.view.representation.MapRepresentation;
import org.voidsentinel.hexmap.view.representation.MapRepresentationRepository;

import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * This is a utility class to help load Mods. It provide functionnalities to
 * search for mods in (sub)directory, as a mod.xml file. It also allow to
 * load/save a set of "used" mod .
 */
public class ModLoader {
	private static final Logger	LOG		= Logger.getLogger(ModLoader.class.toString());

	private static final String	STANDARD	= "standard";

	/**
	 * get the list of mods available in the theme directory. This is done by
	 * searching for "mod.xml" file in first level subdirectories
	 * 
	 * @see directory
	 * 
	 * @return a list of ModData, each ModData contianing informations on the mod
	 *         stored in this directory
	 */
	public static ModList getModsFromDirectories(String directory) {
		LOG.log(Level.INFO, "...Reading Mod definitions");
		ModList response = new ModList();
		File dir = new File(directory);
		if (!dir.exists()) {
			LOG.severe("base mod directory not found");
		} else {
			String[] dirs = dir.list(DirectoryFileFilter.INSTANCE);
			for (String directoryName : dirs) {
				ModData tsd = getInformations(directory + "/" + directoryName);
				if (tsd != null) {
					response.add(tsd);
				}
			}
		}
		return response;
	}

	/**
	 * return the list of Mod whose Id is present in the given xml file. Any Mod in
	 * the file but not available in the given "available" list will be removed
	 * 
	 * @return a list of ModData
	 */
	public static ModList getModsFromFile(String filename, ModList availables) {
		LOG.log(Level.INFO, "...Reading Mod usage");
		ModList response = new ModList();

		// put "Standard" Mod in the response as first
		ModData standard = availables.get(STANDARD);
		if (standard != null) {
			response.put(standard.getName(), standard);
		} else {
			LOG.log(Level.SEVERE, "Standard Mod not available");
		}

		// load the used list
		Document document;
		final SAXBuilder sxb = new SAXBuilder();
		LOG.log(Level.INFO, "   Loading infos on used mod in file  " + filename);

		try {
			document = sxb.build(new File(filename));
			final Element root = document.getRootElement();
			final List<Element> children = root.getChildren("mod");
			final Iterator<Element> it = children.iterator();
			while (it.hasNext()) {
				Element child = it.next();
				String id = child.getAttributeValue("id");
				if (id != null) {
					ModData mod = availables.get(id);
					if (mod != null) {
						LOG.log(Level.INFO, "      " + id);
						response.add(mod);
					} else {
						LOG.log(Level.INFO, "      " + id + " (not found)");
					}
				}
			}
		} catch (final Exception e) {
			LOG.log(Level.SEVERE, "Impossible to find file " + filename, e);
		}

		return response;
	}

	/**
	 * get informations on the mod stored into a a given directory
	 * 
	 * @param directory where the mod.xml will be searched
	 * @return the mod informations
	 */
	private static ModData getInformations(final String directory) {
		LOG.log(Level.INFO, "......Loading infos on mod in directory  " + directory);
		Document document;
		final SAXBuilder sxb = new SAXBuilder();

		File dir = new File(directory);
		String[] files = dir.list(new NameFileFilter("mod.xml"));
		for (String filename : files) {
			try {
				document = sxb.build(new File(directory + "/" + filename));
				final Element root = document.getRootElement();
				final String modName = root.getAttributeValue("id").trim();
				final String iconFile = root.getAttributeValue("icon").trim();
				final String description = root.getAttributeValue("description").trim();
				LOG.log(Level.INFO, "      id : " + modName);
				ModData response = new ModData();
				response.setFilename(directory + "/" + filename);
				response.setDirectory(directory);
				response.setName(modName);
				response.setDescription(description);
				response.setIconfile(directory + "/" + iconFile);
				return response;
			} catch (final Exception e) {
				LOG.log(Level.SEVERE, "Impossible to find file " + filename, e);
				return null;
			}
		}
		return null;
	}

	/**
	 * Load a given mod
	 * 
	 * @param mod
	 */
	public static void loadMod(ModData mod, AssetManager manager) {
		LOG.log(Level.INFO, "Registering directory for assets : " + mod.getDirectory());
		manager.registerLocator(mod.getDirectory(), FileLocator.class);

		LOG.log(Level.INFO, "Loading mod " + mod.getName());
		loadMod(mod.getFilename(), mod.getDirectory());
	}

	/**
	 * Load a mod, given by its filename
	 * 
	 * @param filename  full filename (including directory)
	 * @param directory directory of the file
	 */
	private static void loadMod(String filename, String directory) {
		LOG.log(Level.INFO, "   loading mod in file " + filename);
		Document document;
		final SAXBuilder sxb = new SAXBuilder();
		try {
			File file = new File(filename);
			document = sxb.build(file);
			final Element root = document.getRootElement();
			genericNode(root, directory);
		} catch (final Exception e) {
			LOG.log(Level.SEVERE, "Impossible to find file " + filename, e);
		}
	}

	/**
	 * dispatch le loading to a method, depending on the name of the node. If
	 * unknown, distpach the childrens
	 * 
	 * @param node
	 * @param directory
	 */
	private static void genericNode(Element node, String directory) {
		switch (node.getName()) {
		case "file": { // allow to load a external file
			String name = node.getAttributeValue("name");
			loadMod(directory + "/" + name, directory);
			break;
		}
		case "image": {
			loadImage(node, directory);
			break;
		}
		case "text": {
			loadText(node, directory);
			break;
		}
		case "terrain": {
			loadTerrain(node, directory);
			break;
		}
		case "terrainMaterial": {
			loadTerrainMaterial(node, directory);
			break;
		}
		case "mapColorMapper": {
			loadMapColorMapper(node, directory);
			break;
		}
		case "mapRepresentation": {
			loadMapRepresentation(node, directory);
			break;
		}
		default:
			List<Element> childs = node.getChildren();
			Iterator<Element> it = childs.iterator();
			while (it.hasNext()) {
				genericNode(it.next(), directory);
			}
		}
	}

	private static void loadImage(Element node, String directory) {
		String id = node.getAttributeValue("id");
		if (ImageRepository.datas.getData(id) == null) {
			String file = directory + '/' + node.getValue().trim();
			ImageData data = new ImageData(id, file);
			ImageRepository.datas.addData(data);
			LOG.log(Level.INFO, "   loading image " + id + " as " + data.getFilename());
		} else {
			LOG.log(Level.INFO, "   image " + id + " ignored as already existing");
		}
	}

	/**
	 * Load a property file
	 * 
	 * @param node      the XML node containing the information
	 * @param directory the directory of the current XML file
	 */
	private static void loadText(Element node, String directory) {
		LOG.log(Level.INFO, "   loading texts in " + directory + "/" + node.getValue());
		I18nMultiFile.add(directory, node.getValue().trim());
	}

	/**
	 * Load a terrain
	 * 
	 * @param node
	 * @param directory
	 */
	private static void loadTerrain(Element node, String directory) {
		String id = node.getAttributeValue("id");

		if (TerrainRepository.datas.getData(id) == null) {
			LOG.log(Level.INFO, "   loading terrain " + id);
			TerrainData terrain = new TerrainData(id);

			// get the Center UV coordinates
			List<Element> uvt = node.getChildren("uvcenter");
			Iterator<Element> ituvt = uvt.iterator();
			while (ituvt.hasNext()) {
				Element uvn = ituvt.next();
				Vector2f uv = VectorUtils.vector2fFromString(uvn.getValue());
				terrain.uvCenter.add(uv);
			}

			Element uvSize = node.getChild("uvsize");
			if (uvSize != null)
				terrain.setUVSize(Float.parseFloat(uvSize.getValue().trim()));

			// get the colors
			List<Element> uvc = node.getChildren("color");
			Iterator<Element> ituvc = uvc.iterator();
			while (ituvc.hasNext()) {
				Element uvn = ituvc.next();
				ColorRGBA color = ColorParser.parse(uvn.getValue().trim());
				if (color != null) {
					terrain.baseColors.add(color);
				}
			}
			// get the border colors
			List<Element> uvb = node.getChildren("borderColor");
			Iterator<Element> ituvb = uvb.iterator();
			while (ituvb.hasNext()) {
				Element uvn = ituvb.next();
				ColorRGBA color = ColorParser.parse(uvn.getValue().trim());
				if (color != null) {
					terrain.borderColor = color;
				}
			}

			TerrainRepository.datas.addData(terrain);
		}
	}

	private static void loadTerrainMaterial(Element node, String directory) {
		String filename = node.getValue().trim();
		try {
			TerrainRepository.setTerrainMaterial(filename);
		} catch (final Exception e) {
			LOG.log(Level.SEVERE, "Problem with Material file " + filename, e);
		}
		;
	};

	private static void loadMapColorMapper(Element node, String directory) {
		String className = null;
		String id = node.getAttributeValue("id").trim().toLowerCase();
		if (colorMapperRepository.repository.getData(id) == null) {

			if (node.getAttributeValue("class") != null) {
				className = node.getAttributeValue("class").trim();
				Class<?> clazz;
				try {
					clazz = Class.forName(className);
					Constructor<?> constructor = clazz.getConstructor(String.class);
					Object instance = constructor.newInstance(id);
					colorMapperRepository.repository.addData((AbstractCellColorExtractor) (instance));
				} catch (ClassNotFoundException e) {
					LOG.log(Level.SEVERE, "Impossible to find class " + className);
				} catch (NoSuchMethodException e) {
					LOG.log(Level.SEVERE, "Impossible to find class constructor" + className);
				} catch (SecurityException e) {
					LOG.log(Level.SEVERE, "Impossible to instanciate class constructor" + className + "due to security");
				} catch (InstantiationException e) {
					LOG.log(Level.SEVERE, "Impossible to instanciate class constructor" + className);
				} catch (IllegalAccessException e) {
					LOG.log(Level.SEVERE, "Impossible to instanciate class constructor" + className);
				} catch (IllegalArgumentException e) {
					LOG.log(Level.SEVERE, "Impossible to instanciate class constructor" + className);
				} catch (InvocationTargetException e) {
					LOG.log(Level.SEVERE, "Impossible to instanciate class constructor" + className);
				}
			}

			AbstractCellColorExtractor colorExt = colorMapperRepository.repository.getData(id);
			String icon = node.getAttributeValue("icon");
			if (icon != null) {
				colorExt.setIconName(icon.trim());
			}
			String text = node.getAttributeValue("text");
			if (text != null) {
				colorExt.setTextName(text.trim());
			}
			
			String tooltip = node.getAttributeValue("tooltip");
			if (text != null) {
				colorExt.setTooltipName(tooltip.trim());
			}
			List<Attribute> attributes = node.getAttributes();
			removeAttribute("id", attributes);
			removeAttribute("class", attributes);
			removeAttribute("icon", attributes);
			removeAttribute("text", attributes);
			removeAttribute("tooltip", attributes);

			Iterator<Attribute> it = attributes.iterator();
			while (it.hasNext()) {
				Attribute attribute = it.next();
				colorExt.addDataParameters(attribute.getName().toLowerCase(), attribute.getValue(), directory + "/");
			}
		}
	}

	private static void loadMapRepresentation(Element node, String directory) {
		String className = null;
		String id = node.getAttributeValue("id").trim().toLowerCase();
		// if not already present
		if (MapRepresentationRepository.repository.getData(id) == null) {
			MapRepresentation representation = new MapRepresentation(id);
			
			if (node.getAttributeValue("class") != null) {
				className = node.getAttributeValue("class").trim();
				representation.setClassName(className);
			}

			String icon = node.getAttributeValue("icon");
			if (icon != null) {
				representation.setIconName(icon.trim());
			}
			String text = node.getAttributeValue("text");
			if (text != null) {
				representation.setLabelName(text.trim());
			}			
			String tooltip = node.getAttributeValue("tooltip");
			if (tooltip != null) {
				representation.setTooltipName(tooltip.trim());
			}
			String material = node.getAttributeValue("material");
			if (material != null) {
				representation.setMaterialName(material.trim());
			}

			MapRepresentationRepository.repository.addData(representation);
			
			List<Attribute> attributes = node.getAttributes();
			removeAttribute("id", attributes);
			removeAttribute("class", attributes);
			removeAttribute("icon", attributes);
			removeAttribute("text", attributes);
			removeAttribute("tooltip", attributes);
			removeAttribute("material", attributes);

			Iterator<Attribute> it = attributes.iterator();
			while (it.hasNext()) {
				Attribute attribute = it.next();
				representation.addDataParameters(attribute.getName().toLowerCase(), attribute.getValue(), directory + "/");
			}
		}
	}
	
	
	public static void removeAttribute(String id, List<Attribute> attributes) {
		Iterator<Attribute> it = attributes.iterator();
		while (it.hasNext()) {
			if (id.equals(it.next().getName())) {
				it.remove();
			}
		}
	}

}
