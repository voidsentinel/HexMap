/**
 * 
 */
package org.voidsentinel.hexmap.view.mapColor;

import com.jme3.math.ColorRGBA;

/**
 * @author guipatry
 *
 */
public class DirectionColorPair {
	private ColorRGBA inside;
	private ColorRGBA outside;
	
	public DirectionColorPair(ColorRGBA inside, ColorRGBA outside) {
		this.inside = inside;
		this.outside = outside;
	}


	/**
	 * @return the inside
	 */
	public ColorRGBA getInside() {
		return inside;
	}

	/**
	 * @param inside the inside to set
	 */
	public void setInside(ColorRGBA inside) {
		this.inside = inside;
	}

	/**
	 * @return the outside
	 */
	public ColorRGBA getOutside() {
		return outside;
	}

	/**
	 * @param outside the outside to set
	 */
	public void setOutside(ColorRGBA outside) {
		this.outside = outside;
	}

	
}
