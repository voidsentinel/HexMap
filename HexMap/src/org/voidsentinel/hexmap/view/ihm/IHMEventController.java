/**
 * 
 */
package org.voidsentinel.hexmap.view.ihm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.simsilica.lemur.Panel;

/**
 * @author guipatry
 *
 */
public class IHMEventController {
	private static final Logger			LOG		= Logger.getLogger(IHMEventController.class.toString());

	public static enum IHMAction {
		MENU_OPEN, MENU_CLOSE, BUTTON_CLICK
	};

	private static List<IIHMEventListener> listener = new ArrayList<IIHMEventListener>();

	public static void addListener(IIHMEventListener obj) {
		listener.add(obj);
	}

	public static void removeListener(IIHMEventListener obj) {
		listener.remove(obj);
	}

	public static void signalEvent(Panel source, IHMAction action) {
		for (Iterator<IIHMEventListener> iterator = listener.iterator(); iterator.hasNext();) {
			IIHMEventListener iihmEventListener = (IIHMEventListener) iterator.next();
			iihmEventListener.signalAction(source, action);
		}
	}

}
