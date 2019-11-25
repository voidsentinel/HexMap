/**
 * 
 */
package org.voidsentinel.hexmap.view.ihm;

import com.simsilica.lemur.style.ElementId;

/**
 * @author Guillaume
 *
 */
public interface IToggleCallBack {

	/**
	 * 
	 * @param id id of the IHM element
	 * @param value current value
	 */
	public void toggleOccured(ElementId id, boolean value);
}
