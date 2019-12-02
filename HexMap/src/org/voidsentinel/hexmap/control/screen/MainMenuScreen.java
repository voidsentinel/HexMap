/**
 * 
 */
package org.voidsentinel.hexmap.control.screen;

import org.voidsentinel.hexmap.HexTuto;
import org.voidsentinel.hexmap.control.GameStateMap;
import org.voidsentinel.hexmap.repositories.ImageRepository;
import org.voidsentinel.hexmap.utils.ColorParser;
import org.voidsentinel.hexmap.view.HexGrid;
import org.voidsentinel.hexmap.view.mapColor.AbstractCellColorExtractor;
import org.voidsentinel.hexmap.view.representation.MapRepresentation;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Button.ButtonAction;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.component.BoxLayout;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;
import com.simsilica.lemur.geom.TbtQuad;
import com.simsilica.lemur.style.ElementId;

/**
 * @author guipatry
 *
 */
public class MainMenuScreen extends TitledScreen {

	public MainMenuScreen(final HexTuto application, final String id) {
		super(application, id);
	}

	public MainMenuScreen(final HexTuto application) {
		super(application);
	}

	@Override
	public void initialize(final AppStateManager stateManager, final Application app) {
		LOG.info("...Initializing State '" + this.id + "'");
		generateBackground();
		super.initialize(stateManager, app);
	}

	@Override
	public void cleanup() {
		LOG.info("...Cleaning State '" + this.id + "'");
		super.cleanup();
	}

	private void generateBackground() {
		if (guiNode == null) {
			guiNode = new Node(this.id + ".guiNode");
		}

		AppSettings settings = HexTuto.getInstance().getSettings();

		// background
		Container backgroundPanel = new Container(new ElementId(this.id + ".background"));
		guiNode.attachChild(backgroundPanel);
		Texture t = GuiGlobals.getInstance()
		      .loadTexture(ImageRepository.datas.getData("mainMenu.background").getFilename(), false, false);
		Image img = t.getImage();
		TbtQuad q = new TbtQuad(img.getWidth(), img.getHeight());

		TbtQuadBackgroundComponent btTexture = new TbtQuadBackgroundComponent(q, t);
		backgroundPanel.setBackground(btTexture);
		backgroundPanel.setPreferredSize(new Vector3f(settings.getWidth(), settings.getHeight(), 0f));
		backgroundPanel.setLocalTranslation(0, settings.getHeight(), 0);

		Container menuContainer = new Container(new ElementId(this.id + ".menu"));
		menuContainer.setLayout(new BoxLayout(Axis.Y, FillMode.None));
		menuContainer.setBackground(new QuadBackgroundComponent(ColorParser.parse("rgb(211, 191, 143, 211)")));
		menuContainer.setPreferredSize(new Vector3f(settings.getWidth() / 3f, settings.getHeight() - 40f, 0f));
		menuContainer.setLocalTranslation(0, settings.getHeight() - 40f, 0.1f);
		guiNode.attachChild(menuContainer);

		// random
		Button button = new Button("Random Map", backgroundPanel.getElementId().child(".randomMap"));
		button.setPreferredSize(new Vector3f(settings.getWidth() / 4f, 45f, 0f));
		button.setFontSize(25f);
		button.setTextVAlignment(VAlignment.Center);
		button.setColor(ColorRGBA.White);
		button.setLocalTranslation(25f, settings.getHeight() - 60f, 0.1f);
		TbtQuadBackgroundComponent btTexture2 = TbtQuadBackgroundComponent
		      .create(ImageRepository.datas.getData("buttonBackground").getFilename(), 1f, 5, 5, 40, 44, .1f, false);
		button.setBackground(btTexture2);
		button.addCommands(ButtonAction.Click, new ToScreenCommand("mapDisplay"));
		menuContainer.addChild(button);

		button = new Button("Quit", backgroundPanel.getElementId().child(".quit"));
		button.setPreferredSize(new Vector3f(settings.getWidth() / 4f, 45f, 0f));
		button.setFontSize(25f);
		button.setTextVAlignment(VAlignment.Center);
		button.setColor(ColorRGBA.White);
		button.setLocalTranslation(25f, settings.getHeight() - 60f, 0.1f);
		btTexture2 = TbtQuadBackgroundComponent.create(ImageRepository.datas.getData("buttonBackground").getFilename(),
		      1f, 5, 5, 40, 44, .1f, false);
		button.setBackground(btTexture2);
		button.addCommands(ButtonAction.Click, new ExitCommand());
		menuContainer.addChild(button);

	}

	/**
	 * Class for a Button that is used to regenerate a map & map representation
	 * 
	 * @author guipatry
	 *
	 */
	protected class ToScreenCommand implements Command<Button> {
		private String name = "";

		public ToScreenCommand(String name) {
			this.name = name;
		}

		@Override
		public void execute(Button source) {
			GameStateMap.getInstance().moveToState(name);
		}
	}

	/**
	 * Class used for a Button that leave the application
	 * 
	 * @author guipatry
	 */
	protected class ExitCommand implements Command<Button> {
		@Override
		public void execute(Button source) {
			HexTuto.getInstance().stop();
		}
	}

}
