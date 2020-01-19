/**
 * 
 */
package org.voidsentinel.hexmap.control.screen;

import org.voidsentinel.hexmap.HexTuto;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.HeightMapExecutor;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.treatment.DiamondSquare;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.treatment.FaultCirclesOperation;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.treatment.GrelfOperation;
import org.voidsentinel.hexmap.model.mapgenerator.heightmap.treatment.ResetOperation;
import org.voidsentinel.hexmap.repositories.ImageRepository;
import org.voidsentinel.hexmap.utils.I18nMultiFile;
import org.voidsentinel.hexmap.utils.TerrainImage;
import org.voidsentinel.hexmap.view.ihm.MenuBar;
import org.voidsentinel.hexmap.view.ihm.ThemeUtil;

import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Button.ButtonAction;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.component.BorderLayout.Position;
import com.simsilica.lemur.component.BoxLayout;
import com.simsilica.lemur.component.InsetsComponent;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;
import com.simsilica.lemur.geom.TbtQuad;
import com.simsilica.lemur.style.ElementId;

/**
 * @author guipatry
 *
 */
public class MapGenerationScreen extends TitledScreen {

	private float[][]				heightMap	= new float[192][256];
	private HeightMapExecutor	operation	= null;
	private int						filecount	= 0;

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
		general.setLayout(new BoxLayout(Axis.Y, FillMode.None));
		screenContainer.addChild(general, Position.West);
		general.setInsetsComponent(new InsetsComponent(10f, 20f, 1f, 2f));
		general.setPreferredSize(new Vector3f(settings.getWidth() / 4f, settings.getHeight(), 0f));

		// map initialisation

		Container mapStart = new Container(general.getElementId().child("initialisation"));
		general.addChild(mapStart);

		button = new Button("Reset", menuBar.getElementId().child(".reset"));
		// ThemeUtil.setButtonIcon("generateIcon", button);
		ThemeUtil.setButtonTheme(button);
		button.addClickCommands(new HeightMapCommand(new ResetOperation()));
		general.addChild(button);

		button = new Button("Circle Fault", menuBar.getElementId().child(".circleFault"));
		ThemeUtil.setButtonTheme(button);
		button.addClickCommands(new HeightMapCommand(new FaultCirclesOperation(4000)));
		general.addChild(button);

		button = new Button("Diamond-Square", menuBar.getElementId().child(".diamondSquare"));
		ThemeUtil.setButtonTheme(button);
		button.addClickCommands(new HeightMapCommand(new DiamondSquare()));
		general.addChild(button);

		button = new Button("Grelf", menuBar.getElementId().child(".grefl"));
		ThemeUtil.setButtonTheme(button);
		button.addClickCommands(new HeightMapCommand(new GrelfOperation()));
		general.addChild(button);

		button = new Button("Initialize", menuBar.getElementId().child(".generate"));
		ThemeUtil.setButtonIcon("generateIcon", button);
		ThemeUtil.setButtonTheme(button);
		general.setInsetsComponent(new InsetsComponent(40f, 20f, 1f, 2f));
		general.addChild(button);

	}

	protected class HeightMapCommand implements Command<Button> {

		private HeightMapExecutor executor = null;

		public HeightMapCommand(HeightMapExecutor executor) {
			this.executor = executor;
		}

		@Override
		public void execute(Button source) {
			executor.setHeightMap(heightMap);
			operation = executor;
			executor.run();
		}
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		// is an operation is currently running : display the avance
		if (operation != null) {
			float value = operation.getExecution();
			title.setText("Result :" + (value * 100f));
			if (value == 1f) {
				heightMap = operation.getHeightMap();
				operation = null;
				title.setText(I18nMultiFile.getText(this.id + ".title"));
				LOG.info("   Operation finished externally");
				filecount++;
				TerrainImage.generateImage(heightMap, 0.25f, "step" + filecount);
			}
		}
	}

}
