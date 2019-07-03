/**
 * 
 */
package org.voidsentinel.hexmap.model.mapgenerator.operations;

import org.voidsentinel.hexmap.model.HexMap;

/**
 * A Map Operation modify the map (other than the elevation)
 * 
 * @author voidSentinel
 */
public interface IMapOperation {

	public void filter(HexMap map);
}
