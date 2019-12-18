/**
 * 
 */
package org.voidsentinel.hexmap.control.screen;

import org.voidsentinel.hexmap.HexTuto;
import org.voidsentinel.hexmap.control.GameStateMap;
import org.voidsentinel.hexmap.repositories.FontRepository;
import org.voidsentinel.hexmap.repositories.ImageRepository;
import org.voidsentinel.hexmap.utils.ColorParser;
import org.voidsentinel.hexmap.utils.I18nMultiFile;
import org.voidsentinel.hexmap.view.HexGrid;
import org.voidsentinel.hexmap.view.mapColor.AbstractCellColorExtractor;
import org.voidsentinel.hexmap.view.representation.MapRepresentation;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
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
import com.simsilica.lemur.Insets3f;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.component.BoxLayout;
import com.simsilica.lemur.component.InsetsComponent;
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
		menuContainer.setPreferredSize(new Vector3f(settings.getWidth() / 3f, settings.getHeight() - 40f, 0f));
		menuContainer.setLocalTranslation(40, settings.getHeight() - 50f, 0.1f);
		guiNode.attachChild(menuContainer);
		// menuContainer.setBackground(new
		// QuadBackgroundComponent(ColorParser.parse("rgb(211, 191, 143, 211)")));

		BitmapFont buttonFont = FontRepository.datas.getData("button.font").getFont();
		float buttonFontSize = 25f;
		ColorRGBA buttonColor = ColorRGBA.Brown;
		ColorRGBA buttonColorFocus = ColorRGBA.Black;
		Texture buttonTexture = assetManager.loadTexture(ImageRepository.datas.getData("buttonBackground").getFilename());

		// random
		String buttonText = I18nMultiFile.getText("mainmenu.randommap.text");
		Button button = new Button(buttonText, backgroundPanel.getElementId().child(".randomMap"));
		button.setFontSize(buttonFontSize);
		button.setFont(buttonFont);
		button.setColor(buttonColor);
		button.setHighlightColor(buttonColorFocus);
		button.setTextVAlignment(VAlignment.Center);
		button.setInsetsComponent(new InsetsComponent(10f, 2f, 2f, 2f));
		TbtQuadBackgroundComponent btTexture2 = TbtQuadBackgroundComponent.create(buttonTexture, 1f, 5, 5, 40, 44, .1f,
				false);
		button.setBackground(btTexture2);
		button.addCommands(ButtonAction.Click, new ToScreenCommand("mapDisplay"));
		menuContainer.addChild(button);

		buttonText = I18nMultiFile.getText("mainmenu.quit.text");
		button = new Button(buttonText, backgroundPanel.getElementId().child(".quit"));
		button.setFontSize(buttonFontSize);
		button.setFont(buttonFont);
		button.setColor(buttonColor);
		button.setHighlightColor(buttonColorFocus);
		button.setTextVAlignment(VAlignment.Center);
		button.setInsetsComponent(new InsetsComponent(10f, 2f, 2f, 2f));
		btTexture2 = TbtQuadBackgroundComponent.create(buttonTexture, 1f, 5, 5, 40, 44, .1f, false);
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
