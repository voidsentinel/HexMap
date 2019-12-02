/**
 * 
 */
package org.voidsentinel.hexmap.view.ihm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.voidsentinel.hexmap.repositories.ImageRepository;

import com.jme3.scene.Spatial;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;
import com.simsilica.lemur.style.ElementId;

/**
 * This is a horizontal menu.
 * All added button get the buttonSelectedBackground background
 * @author Xerces
 *
 */
public class MenuBar extends Container {
	private static final Logger			LOG		= Logger.getLogger(MenuBar.class.toString());

	public MenuBar(ElementId elementId) {
		super(elementId);
		MigLayout layout = new MigLayout(null);
		this.setLayout(layout);
	}

	public void addButton(Button button) {
		addButton(button, true);
	}

	public void addButton(Button button, boolean rightSide) {
		if (rightSide) {
			addChild(button, "growx");
		} else {
			addChild(button);
		}
		// replace the background
		TbtQuadBackgroundComponent btTexture = TbtQuadBackgroundComponent.create(
		      ImageRepository.datas.getData("buttonSelectedBackground").getFilename(), 1f, 5, 5, 40, 44, .1f, false);
		button.setBackground(btTexture);
	}

	public List<Button> getButtons(){
		List<Button>  response = new ArrayList<Button>();
		List<Spatial> list = this.getChildren();
		for (Iterator<Spatial> iterator = list.iterator(); iterator.hasNext();) {
			Spatial spatial = (Spatial) iterator.next();
			response.add((Button)spatial);
			
		}
		return response;
	}

}
