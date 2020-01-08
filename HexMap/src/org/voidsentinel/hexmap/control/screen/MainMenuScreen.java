/**
 * 
 */
package org.voidsentinel.hexmap.control.screen;

import org.voidsentinel.hexmap.HexTuto;
import org.voidsentinel.hexmap.repositories.ImageRepository;
import org.voidsentinel.hexmap.utils.I18nMultiFile;
import org.voidsentinel.hexmap.view.ihm.ThemeUtil;

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
import com.simsilica.lemur.component.BoxLayout;
import com.simsilica.lemur.component.InsetsComponent;
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
	public void attachScreen() {
		super.attachScreen();
		generateBackground();
	}

	@Override
	public void detachScreen() {
		super.detachScreen();
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
		menuContainer.setPreferredSize(new Vector3f(settings.getWidth() / 3f, settings.getHeight() - 80f, 0f));
		menuContainer.setLocalTranslation(40, settings.getHeight() - 80f, 0.1f);
		guiNode.attachChild(menuContainer);


		// random
		String buttonText = I18nMultiFile.getText("mainmenu.randommap.text");
		Button button = new Button(buttonText, backgroundPanel.getElementId().child(".randomMap"));
		ThemeUtil.setButtonTheme(button, true, false, false);
		button.setInsetsComponent(new InsetsComponent(20f, 20f, 2f, 2f));	
		button.addCommands(ButtonAction.Click, new ToScreenCommand("mapDisplay"));
		menuContainer.addChild(button);

		buttonText = I18nMultiFile.getText("mainmenu.newmap.text");
		button = new Button(buttonText, backgroundPanel.getElementId().child(".newmap"));
		ThemeUtil.setButtonTheme(button, true, false, false);
		button.setInsetsComponent(new InsetsComponent(10f, 20f, 2f, 2f));
		button.addCommands(ButtonAction.Click, new ToScreenCommand("mapGeneration"));
		menuContainer.addChild(button);

		buttonText = I18nMultiFile.getText("mainmenu.quit.text");
		button = new Button(buttonText, backgroundPanel.getElementId().child(".quit"));
		ThemeUtil.setButtonTheme(button, true, false, false);
		button.setInsetsComponent(new InsetsComponent(40f, 20f, 2f, 2f));
		button.addCommands(ButtonAction.Click, new ExitCommand());
		menuContainer.addChild(button);

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
