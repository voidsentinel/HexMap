/**
 * 
 */
package org.voidsentinel.hexmap.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.voidsentinel.hexmap.mod.ModLoader;

/**
 * @author guipatry
 *
 */
public class I18nMultiFile {
	private static final Logger			LOG		= Logger.getLogger(I18nMultiFile.class.toString());

	private static List<ResourceBundle>	bundles	= new ArrayList<ResourceBundle>();

	public static void add(ResourceBundle bundle) {
		bundles.add(bundle);
	}

	public static void add(String directory, String filename) {
		File file = new File(directory);
		try {
			URL[] urls = { file.toURI().toURL() };
			ClassLoader loader = new URLClassLoader(urls);
			ResourceBundle rb = ResourceBundle.getBundle(filename, Locale.getDefault(), loader);
			add(rb);
		} catch (MalformedURLException e) {// TODO Auto-generated catch block
			LOG.severe("Impossible to find file " + filename + " into " + directory);
		}

	}

	/**
	 * Get the text for the given key. Bundle are searched in the order they were
	 * added
	 * 
	 * @param key
	 * @return
	 */
	public static String getText(String key) {
		for (Iterator<ResourceBundle> iterator = bundles.iterator(); iterator.hasNext();) {
			ResourceBundle resourceBundle = (ResourceBundle) iterator.next();
			LOG.info("> Searching " + key + " in " + resourceBundle.getBaseBundleName());
			if (resourceBundle.containsKey(key)) {
				return resourceBundle.getString(key);
			}
		}
		LOG.info("> Failure Searching " + key);
		return key;
	}

}
