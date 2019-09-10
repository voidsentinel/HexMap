/**
 * 
 */
package org.voidsentinel.hexmap;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;

import org.voidsentinel.hexmap.mod.ModData;
import org.voidsentinel.hexmap.mod.ModList;
import org.voidsentinel.hexmap.mod.ModLoader;
import org.voidsentinel.hexmap.model.HexMap;
import org.voidsentinel.hexmap.model.mapgenerator.CapitalismGenerator;
import org.voidsentinel.hexmap.model.mapgenerator.MapGenerator;
import org.voidsentinel.hexmap.utils.Alea;
import org.voidsentinel.hexmap.utils.ColorParser;
import org.voidsentinel.hexmap.utils.I18nMultiFile;
import org.voidsentinel.hexmap.utils.TerrainImage;
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

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.style.BaseStyles;
import com.simsilica.lemur.style.ElementId;

/**
 * @author guipatry
 *
 */
public class HexTuto extends SimpleApplication {

	private static HexTuto	instance;

	private HexGrid			mapNode	= null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.app.SimpleApplication#simpleInitApp()
	 */
	@Override
	public void simpleInitApp() {
		assetManager.registerLocator(".", FileLocator.class);
		assetManager.registerLocator("./assets/", FileLocator.class);

		Alea.setSeed(654);
		GuiGlobals.initialize(this);
		// Load the 'glass' style
		BaseStyles.loadGlassStyle();
		GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

		// load look & Feel
		ModList mods = ModLoader.getModsFromDirectories("assets/mod");
		ModData std = mods.get("standard");
		ModLoader.loadMod(std, this.assetManager);

		// generate the map
		MapGenerator generator = new CapitalismGenerator();
		HexMap map = new HexMap(128, 64);
		generator.generate(map);
		TerrainImage.generateImage(map, false);

		// generate the representation
		mapNode = new HexGrid(map, this.getRootNode());

		Vector3f center = HexMetrics.getCellCenter(map.getCenterCell());
		Vector3f top = center.add(-0f, -15f, -15f);
		this.getCamera().setLocation(top);
		this.getCamera().lookAt(center, HexMetrics.CELL_UNIT_NORMAL);
		this.getCamera().update();

		StepCameraControl cameraControl = new StepCameraControl(this, mapNode, top, center, HexMetrics.CELL_UNIT_NORMAL);
		rootNode.addControl(cameraControl);
		cameraControl.addControlMapping();

		getFlyByCamera().setEnabled(false);
		mouseInput.setCursorVisible(true);

		
		Container panel = new Container();
		guiNode.attachChild(panel);

		panel.setBackground(new QuadBackgroundComponent(ColorParser.parse("rgb(211, 191, 143, 211)")));// new ColorRGBA(0.36f, 0.54f, 0.66f, 0.75f)
		//panel.setBorder(new QuadBackgroundComponent(ColorParser.parse("rgb(0, 0, 0, 211)")));// new ColorRGBA(0.36f, 0.54f, 0.66f, 0.75f)
		panel.setPreferredSize(new Vector3f(settings.getWidth(),  25f, 0f));
		panel.setLocalTranslation(0, settings.getHeight(), 0);
		
		TextField hooverField = new TextField("", new ElementId("tooltipElement"));
		hooverField.setColor(ColorRGBA.White);
		hooverField.setPreferredSize(new Vector3f(settings.getWidth(),  25f, 0f));
		hooverField.setTextVAlignment(VAlignment.Center);
      panel.addChild(hooverField);		
		
      MenuBar menu = new MenuBar(new ElementId("menuBar"));
	   menu.setPreferredSize(new Vector3f(settings.getWidth(),  32f, 0f));
		menu.setLocalTranslation(0, this.settings.getHeight() - hooverField.getPreferredSize().y, -0.10f);
		guiNode.attachChild(menu);


		addMapRepresentationRoll(menu, hooverField);
		
		addExitButton(menu, hooverField, "Quit game");

		

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		instance = new HexTuto();
		instance.start();
	}

	public static HexTuto getInstance() {
		return instance;
	}

	private void addMapRepresentationRoll(MenuBar menu, TextField hooverField) {
		// defaul value
		AbstractCellColorExtractor extract = ColorMapperRepository.repository.getDefaultMapper();
		// Create a simple container for view elements
		MenuButton ddb = new MenuButton("", (IconComponent)null, new ElementId("visual"), true, true);
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

	private void addExitButton(MenuBar menu,TextField hooverField, String toolipText) {
		ImageData image = ImageRepository.datas.getData("settingsIcon");
		String fileName = image.getFilename();
		IconComponent icon = new IconComponent(fileName);
		
		MenuButton ddb = new MenuButton("", icon, menu.getElementId().child("settingMenu"), false, true);
      ddb.setHooverField(hooverField);
		menu.addButton(ddb);
		ddb.setToolTip("System Menu");
			   
		image = ImageRepository.datas.getData("mapRepresentationIcon");
		fileName = image.getFilename();
		icon = new IconComponent(fileName);
		
		MenuButton ddbSettings= new MenuButton("", icon, ddb.getElementId().child("graphical"), false, false);
		ddbSettings.setHooverField(hooverField);
		ddb.addButton(ddbSettings, I18nMultiFile.getText("ihm.setting.tooltip"));
		
		Iterator<String> it = MapRepresentationRepository.repository.datas.keySet().iterator();
		while(it.hasNext()) {
			MapRepresentation mr = MapRepresentationRepository.repository.getData(it.next());
			ddbSettings.addButton(
                     I18nMultiFile.getText(mr.getLabelName()),
                     null,
                     I18nMultiFile.getText(mr.getTooltipName()), 
                     new GeometryCommand(mr.id));
		}
      
		image = ImageRepository.datas.getData("exitIcon");
		fileName = image.getFilename();
		icon = new IconComponent(fileName);
	   ddb.addButton("",   icon, "Quit Game", new ExitCommand());

	}

	
	protected class VisualCommand implements Command<Button> {
		AbstractCellColorExtractor extractor = null;

		public VisualCommand(AbstractCellColorExtractor initial) {
			this.extractor = initial;
		}

		@Override
		public void execute(Button source) {
			if (extractor != null) {
				mapNode.setColorExtractor(extractor);
			}
		}
	}

		
	protected class GeometryCommand implements Command<Button> {
		String geometry = null;

		public GeometryCommand(String initial) {
			this.geometry = initial;
		}

		@Override
		public void execute(Button source) {
			if (geometry != null) {
				try {
					mapNode.setMeshGeneration(geometry);
				} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				      | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		}
	}

	
	protected class ExitCommand implements Command<Button> {
		@Override
		public void execute(Button source) {
			HexTuto.getInstance().stop();
		}
	}

	protected class EmptyCommand implements Command<Button> {
		@Override
		public void execute(Button source) {			
		}
	}
	

}