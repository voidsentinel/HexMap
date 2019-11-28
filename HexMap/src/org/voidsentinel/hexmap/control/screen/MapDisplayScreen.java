/**
 * Project Mapper 
 * An Hexagonal Map
 */
package org.voidsentinel.hexmap.control.screen;

import java.util.Iterator;
import java.util.Map;

import org.voidsentinel.hexmap.HexTuto;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.mapgenerator.CapitalismGenerator;
import org.voidsentinel.hexmap.model.mapgenerator.MapGenerator;
import org.voidsentinel.hexmap.utils.ColorParser;
import org.voidsentinel.hexmap.utils.I18nMultiFile;
import org.voidsentinel.hexmap.view.HexGrid;
import org.voidsentinel.hexmap.view.HexMetrics;
import org.voidsentinel.hexmap.view.ihm.ImageData;
import org.voidsentinel.hexmap.view.ihm.ImageRepository;
import org.voidsentinel.hexmap.view.ihm.MenuBar;
import org.voidsentinel.hexmap.view.ihm.MenuButton;
import org.voidsentinel.hexmap.view.ihm.StepCameraControl;
import org.voidsentinel.hexmap.view.mapColor.AbstractCellColorExtractor;
import org.voidsentinel.hexmap.view.mapColor.ColorMapperRepository;
import org.voidsentinel.hexmap.view.representation.MapRepresentation;
import org.voidsentinel.hexmap.view.representation.MapRepresentationRepository;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.style.ElementId;

//import digitalgorgon.mapper.view.ihm.ISelectorCallBack;

/**
 * MapScreen : This is the screen that display the map and allows for
 * manipulation
 * 
 * @author Guillaume
 */
public class MapDisplayScreen extends GameState {

	private StepCameraControl	cameraControl	= null;
	private MapGenerator			generator		= new CapitalismGenerator();
	private HexMap					map				= new HexMap(800, 600);
	private HexGrid				mapNode			= null;

//	private AbstractPanelState[] modifierStates = null;

	/**
	 * @param application
	 * @param id
	 */
	public MapDisplayScreen(final HexTuto application, final String id) {
		super(application, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.digitalgorgon.game.hexmap.control.screens.GameState#initialize(com
	 * .jme3.app.state.AppStateManager, com.jme3.app.Application)
	 */
	@Override
	public void initialize(final AppStateManager stateManager, final Application app) {
		LOG.info("...Initializing State '" + this.id + "'");
		if (rootNode == null) {
			rootNode = new Node(this.id + ".root");
			generateDisplay();
		}

		if (guiNode == null) {
			guiNode = new Node(this.id + ".guiNode");
			generateIHM();
		}

		super.initialize(stateManager, app);
	}

	public void cleanup() {
		LOG.info("...Cleaning State '" + this.id + "'");
		cameraControl.removeControlMapping();		
		super.cleanup();
//		for (int i = 0; i < modifierStates.length; i++) {
//			application.getStateManager().detach(modifierStates[i]);
//		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.app.state.AbstractAppState#update(float)
	 */
	@Override
	public void update(float tpf) {
		super.update(tpf);
	}

	/**
	 * Generate the representation of the map.
	 * (use the common data bag to get the generator)
	 */
	private void generateDisplay() {
		// generate the map
		generator.generate(map);

		// generate the representation
		rootNode = new Node(id + ".map");
		mapNode = new HexGrid(map, rootNode);
		mapNode.generate();
		rootNode = mapNode.getNode();

		// the camera
		Vector3f center = HexMetrics.getCellCenter(map.getCenterCell());
		cameraControl = new StepCameraControl(HexTuto.getInstance(), mapNode, center, HexMetrics.CELL_UNIT_NORMAL);
		rootNode.addControl(cameraControl);
		cameraControl.addControlMapping();

	}

	/**
	 * Generate the IHM with a toolyip bar and a menubar.
	 */
	private void generateIHM() {
		AppSettings settings = HexTuto.getInstance().getSettings();
		Container panel = new Container();
		guiNode.attachChild(panel);

		panel.setBackground(new QuadBackgroundComponent(ColorParser.parse("rgb(211, 191, 143, 211)")));
		panel.setPreferredSize(new Vector3f(settings.getWidth(), 25f, 0f));
		panel.setLocalTranslation(0, settings.getHeight(), 0);

		TextField hooverField = new TextField("", new ElementId("tooltipElement"));
		hooverField.setColor(ColorRGBA.White);
		hooverField.setPreferredSize(new Vector3f(settings.getWidth(), 25f, 0f));
		hooverField.setTextVAlignment(VAlignment.Center);
		panel.addChild(hooverField);

		MenuBar menu = new MenuBar(new ElementId("menuBar"));
		menu.setPreferredSize(new Vector3f(settings.getWidth(), 32f, 0f));
		menu.setLocalTranslation(0, settings.getHeight() - hooverField.getPreferredSize().y, -0.10f);
		guiNode.attachChild(menu);

		addMapRepresentationRoll(menu, hooverField);
		addSettingMenu(menu, hooverField);
	}

	/**
	 * 
	 * @param menu
	 * @param hooverField
	 */
	private void addMapRepresentationRoll(MenuBar menu, TextField hooverField) {
		// defaul value
		AbstractCellColorExtractor extract = ColorMapperRepository.repository.getDefaultMapper();
		// Create a simple container for view elements
		MenuButton ddb = new MenuButton("", (IconComponent) null, new ElementId("visual"), true, true);
		menu.addButton(ddb);
		ddb.setHooverField(hooverField);
		ddb.setToolTip("Visualisation");

		int count = -1;
		Iterator<Map.Entry<String, AbstractCellColorExtractor>> it = ColorMapperRepository.repository.datas.entrySet()
		      .iterator();
		while (it.hasNext()) {
			Map.Entry<String, AbstractCellColorExtractor> colorizer = it.next();
			count++;
			IconComponent icon = null;
			String iconName = colorizer.getValue().getIconName();
			if (iconName != null) {
				ImageData image = ImageRepository.datas.getData(iconName);
				if (image != null) {
					String fileName = image.getFilename();
					if (fileName != null) {
						icon = new IconComponent(fileName);
						icon.setIconSize(new Vector2f(32, 32));
					}
				}
			}
			String text = I18nMultiFile.getText(colorizer.getValue().getTextName());
			String tooltip = I18nMultiFile.getText(colorizer.getValue().getTooltipName());
			ddb.addButton(text, icon, tooltip,
			      new VisualCommand(ColorMapperRepository.repository.getData(colorizer.getKey())));
			if (extract == colorizer.getValue()) {
				ddb.setSelected(count);
			}
		}
	}

	private void addSettingMenu(MenuBar menu, TextField hooverField) {

		// the System Menu
		ImageData image = ImageRepository.datas.getData("settingsIcon");
		String fileName = image.getFilename();
		IconComponent icon = new IconComponent(fileName);
		MenuButton ddb = new MenuButton("", icon, menu.getElementId().child("settingMenu"), false, true);
		ddb.setHooverField(hooverField);
		menu.addButton(ddb);
		ddb.setToolTip(I18nMultiFile.getText("ihm.system.tooltip"));

		// the Map representation subMenu
		image = ImageRepository.datas.getData("mapRepresentationIcon");
		fileName = image.getFilename();
		icon = new IconComponent(fileName);
		MenuButton ddbSettings = new MenuButton("", icon, ddb.getElementId().child("graphical"), false, false);
		ddbSettings.setHooverField(hooverField);
		ddb.addButton(ddbSettings, I18nMultiFile.getText("ihm.setting.tooltip"));

		// The content of the Map represenatation menu
		Iterator<String> it = MapRepresentationRepository.repository.datas.keySet().iterator();
		while (it.hasNext()) {
			MapRepresentation mr = MapRepresentationRepository.repository.getData(it.next());
			ddbSettings.addButton(I18nMultiFile.getText(mr.getLabelName()), null,
			      I18nMultiFile.getText(mr.getTooltipName()), new GeometryCommand(mr.id));
		}

		// The QUIT button
		image = ImageRepository.datas.getData("exitIcon");
		fileName = image.getFilename();
		icon = new IconComponent(fileName);
		ddb.addButton("", icon, I18nMultiFile.getText("ihm.quit.tooltip"), new ExitCommand());

		image = ImageRepository.datas.getData("reloadIcon");
		fileName = image.getFilename();
		icon = new IconComponent(fileName);
		ddb.addButton("", icon, I18nMultiFile.getText("ihm.reload.tooltip"), new ReloadCommand());

	}

	/**
	 * Class used for a change the color extractor mode & perform the regeneration
	 * of the representation color
	 */
	protected class VisualCommand implements Command<Button> {
		AbstractCellColorExtractor extractor = null;

		public VisualCommand(AbstractCellColorExtractor initial) {
			this.extractor = initial;
		}

		@Override
		public void execute(Button source) {
			if (extractor != null) {
				mapNode.setColorExtractor(extractor);
				mapNode.generateColor();
			}
		}
	}

	/**
	 * Class used for a change the represenattion mode & perform the generation of
	 * the representation
	 */
	protected class GeometryCommand implements Command<Button> {
		String geometry = null;

		public GeometryCommand(String initial) {
			this.geometry = initial;
		}

		@Override
		public void execute(Button source) {
			if (geometry != null) {
				mapNode.setMeshGeneration(geometry);
				mapNode.generate();
			}
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

	/**
	 * Class used for a Button that do nothing
	 * 
	 * @author guipatry
	 */
	protected class EmptyCommand implements Command<Button> {
		@Override
		public void execute(Button source) {
		}
	}

	/**
	 * Class for a Button that is used to regenerate a map & map representation
	 * 
	 * @author guipatry
	 *
	 */
	protected class ReloadCommand implements Command<Button> {
		@Override
		public void execute(Button source) {

			rootNode.detachAllChildren();
			cameraControl.removeControlMapping();
			rootNode.removeControl(cameraControl);

			// generate a new map
			generator.generate(map);

			// get the current ColorExtractor & representation
			AbstractCellColorExtractor extractor = mapNode.getColorExtractor();
			MapRepresentation meshGen = mapNode.getMeshGeneration();
			// create the grid
			mapNode = new HexGrid(map, HexTuto.getInstance().getRootNode());
			// replace the current ColorExtractor & representation
			mapNode.setColorExtractor(extractor);
			mapNode.setMeshGeneration(meshGen);
			// and generate the representation
			mapNode.generate();

			rootNode = mapNode.getNode();
			cameraControl.setMapDisplay(mapNode);
			rootNode.addControl(cameraControl);
			cameraControl.addControlMapping();

		}
	}

}
