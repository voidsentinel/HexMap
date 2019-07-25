/**
 * 
 */
package org.voidsentinel.hexmap.view.ihm;

import com.jme3.math.Vector3f;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.component.BoxLayout;
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
		this.setLayout(new BoxLayout(Axis.X, FillMode.None));
	}

	public void addChild(Button button) {
		if (!leftToRight) {
			Vector3f currentPosition = this.getLocalTranslation();
			this.setLocalTranslation(currentPosition.x - button.getPreferredSize().x, currentPosition.y, currentPosition.z);
		}
		super.addChild(button);		
	}

}
