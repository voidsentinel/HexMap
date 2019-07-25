/**
 * 
 */
package org.voidsentinel.hexmap.view.ihm;

import org.voidsentinel.hexmap.view.ihm.IHMEventController.IHMAction;

import com.simsilica.lemur.Panel;

/**
 * This is an interface for a listener that get message about a IHM event in
 * another part of the IHM
 * 
 * @author VoidSentinel
 *
 */
public interface IIHMEventListener {

	/**
	 * indicate that a even occured on another IHM object (for exemple another menu
	 * was opened). 
	 * 
	 * @param source the source of the event
	 */
	public void signalAction(Panel source,  IHMAction action);

}
