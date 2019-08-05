/**
 * 
 */
package org.voidsentinel.hexmap.view.ihm;

import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.style.ElementId;

/**
 * @author Xerces
 *
 */
public class MenuBar extends Container {

	private boolean leftToRight = true;

	public MenuBar(ElementId elementId) {
		this(elementId, true);
	}

	public MenuBar(ElementId elementId, boolean leftToRight) {
		super(elementId);
		this.leftToRight = leftToRight;
		MigLayout layout = new MigLayout(null);
		this.setLayout(layout);
	}

	public void addButton(Button button) {
		addChild(button, false);
	}

	public void addButton(Button button, boolean rightSide) {
		if (rightSide) {
			addChild(button, "growx");
		} else {
			addChild(button);
		}
	}

}
