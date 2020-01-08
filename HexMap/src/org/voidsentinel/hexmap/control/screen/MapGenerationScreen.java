/**
 * 
 */
package org.voidsentinel.hexmap.control.screen;

import org.voidsentinel.hexmap.HexTuto;
import org.voidsentinel.hexmap.repositories.ImageRepository;
import org.voidsentinel.hexmap.view.ihm.MenuBar;
import org.voidsentinel.hexmap.view.ihm.ThemeUtil;
import org.voidsentinel.hexmap.view.ihm.VBoxLayout;

import com.jme3.system.AppSettings;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Button.ButtonAction;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.component.BorderLayout.Position;
import com.simsilica.lemur.component.InsetsComponent;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;
import com.simsilica.lemur.geom.TbtQuad;
import com.simsilica.lemur.style.ElementId;

/**
 * @author guipatry
 *
 */
public class MapGenerationScreen extends TitledScreen {

	public MapGenerationScreen(final HexTuto application, final String id) {
		super(application, id);
	}

	public MapGenerationScreen(final HexTuto application) {
		super(application);
	}

	@Override
	public void attachScreen() {
		super.attachScreen();
		generateGui();
	}

	private void generateGui() {

		AppSettings settings = HexTuto.getInstance().getSettings();
		Container screenContainer = (Container) guiNode.getChild(SCREENCONTAINER_ID);
		// background
		Texture backgroundTexture = assetManager
				.loadTexture(ImageRepository.datas.getData("buttonBackground").getFilename());
		Image img = backgroundTexture.getImage();
		TbtQuad q = new TbtQuad(img.getWidth(), img.getHeight());
		TbtQuadBackgroundComponent btTexture = new TbtQuadBackgroundComponent(q, backgroundTexture);
		screenContainer.setBackground(btTexture);

		// screen actions
		MenuBar menuBar = new MenuBar(new ElementId(this.id + ".menuBar"));
		screenContainer.addChild(menuBar, Position.South);

		Button button = new Button(null, menuBar.getElementId().child(".back"));
		ThemeUtil.setButtonIcon("backIcon", button);
		ThemeUtil.setButtonTheme(button);
		button.addCommands(ButtonAction.Click, new ToScreenCommand("mainMenu"));
		menuBar.addButton(button);

		button = new Button(null, menuBar.getElementId().child(".generate"));
		ThemeUtil.setButtonIcon("generateIcon", button);
		ThemeUtil.setButtonTheme(button);
		// button.addCommands(ButtonAction.Click, new ToScreenCommand("displayScreen"));
		menuBar.addButton(button);

		// general container
		Container general = new Container(screenContainer.getElementId().child("mapActionContainer"));
		general.setLayout(new VBoxLayout());
		screenContainer.addChild(general, Position.West);
		general.setInsetsComponent(new InsetsComponent(10f, 20f, 1f, 2f));
		
		// map initialisation
		
		
		Container mapStart = new Container(general.getElementId().child("initialisation"));
		general.addChild(mapStart);
		
		
		
		button = new Button("Initialize", menuBar.getElementId().child(".generate"));
		ThemeUtil.setButtonIcon("generateIcon", button);
		ThemeUtil.setButtonTheme(button);
		general.addChild(button);
		
		

	}



}
