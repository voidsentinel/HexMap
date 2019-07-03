/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author    VoidSentinel
 */
package org.voidsentinel.hexmap.mod;

import java.util.LinkedHashMap;


/**
 * Set of Mods  
 */
public class ModList extends LinkedHashMap<String, ModData>{
		
	public void add(ModData data) {
		this.put(data.getName(), data);
	}
	
	public ModData remove(ModData data) {
		return this.remove(data.getName());
	}
	
}
