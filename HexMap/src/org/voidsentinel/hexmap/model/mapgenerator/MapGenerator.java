/**
 * Project Hexagon
 *
 * @copyright 2018
 * @author    VoidSentinel
 */
package org.voidsentinel.hexmap.model.mapgenerator;

import java.util.logging.Logger;

import org.voidsentinel.hexmap.model.HexMap;

/**
 * This is the abstract class for a map generator.
 */
public abstract class MapGenerator {

	protected static final Logger				LOG			= Logger.getLogger(MapGenerator.class.toString());

   /**
    * generate a map
    * @param map
    */
   public abstract void generate(HexMap map);

   
}
