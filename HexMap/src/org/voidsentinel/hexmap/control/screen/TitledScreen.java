/**
 * 
 */
package org.voidsentinel.hexmap.control.screen;

import org.voidsentinel.hexmap.HexTuto;
import org.voidsentinel.hexmap.repositories.FontRepository;
import org.voidsentinel.hexmap.repositories.ImageRepository;
import org.voidsentinel.hexmap.utils.I18nMultiFile;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.component.BorderLayout;
import com.simsilica.lemur.component.BorderLayout.Position;
import com.simsilica.lemur.style.ElementId;

/**
 * This subclass of GameState is a screen with a title.
 * <ul>
 * <li>the displayed title is under the property "id.title", where id is the id
 * of the screen (by default classname). Warning the id should be in lowercase,
 * even if defined with high and lowercase
 * <li>the font is defined by a "font" with id "title.font"
 * <li>the font size is defined by a "data" with id "title.fontsize"
 * <li>the font color is defined by a "data" with id "title.textcolor"
 * <li>the title background defined by a "image" with id "title.background"
 * </ul>
 * 
 * @see ImageRepository
 * @see FontRepository
 * @see DataRepository
 * @author guipatry
 *
 */
public class TitledScreen extends GameState {

	protected final String SCREENCONTAINER_ID;
	protected TextField title = null;

	public TitledScreen(final HexTuto application) {
		super(application);
		SCREENCONTAINER_ID = this.id + ".screen";
	}

	/**
	 * @param application
	 * @param id
	 */
	public TitledScreen(final HexTuto application, final String id) {
		super(application, id);
		SCREENCONTAINER_ID = this.id + ".screen";
	}

	/**
	 * Will initialize the screen and display it
	 */
	@Override
	public void attachScreen() {
		if (guiNode == null) {
			guiNode = new Node(this.id + ".guiNode");
		}
		generateIHM();
	}

	public void detachScreen() {
		
	}

	private void generateIHM() {
		// if the title does not exist...
		if (guiNode.getChild(SCREENCONTAINER_ID) == null) {
			AppSettings settings = HexTuto.getInstance().getSettings();
			// Title Container
			Container titleContainer = new Container(new ElementId(SCREENCONTAINER_ID));
			titleContainer.setName(SCREENCONTAINER_ID);
			titleContainer.setLayout(new BorderLayout());
			titleContainer.setBackground(null);
			guiNode.attachChild(titleContainer);
			titleContainer.setPreferredSize(new Vector3f(settings.getWidth(), settings.getHeight(), 0f));
			titleContainer.setLocalTranslation(0f, settings.getHeight(), 0.1f);
			// Title
			title = new TextField("", titleContainer.getElementId().child("text"));
			title.setName(title.getElementId().toString());
			title.setText(I18nMultiFile.getText(this.id + ".title"));
			title.setColor(ColorRGBA.Black);
			title.setFont(FontRepository.datas.getData("title.font").getFont());
			title.setFontSize(72f);
			title.setPreferredSize(new Vector3f(settings.getWidth(), 70f, 0f));
			title.setTextVAlignment(VAlignment.Center);
			title.setTextHAlignment(HAlignment.Center);

			titleContainer.addChild(title, Position.North);
		}
	}
}
