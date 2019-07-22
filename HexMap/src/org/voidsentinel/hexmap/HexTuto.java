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
import org.voidsentinel.hexmap.utils.TerrainImage;
import org.voidsentinel.hexmap.view.HexGrid;
import org.voidsentinel.hexmap.view.HexMetrics;
import org.voidsentinel.hexmap.view.ihm.DropDownButton;
import org.voidsentinel.hexmap.view.ihm.ImageData;
import org.voidsentinel.hexmap.view.ihm.ImageRepository;
import org.voidsentinel.hexmap.view.ihm.StepCameraControl;
import org.voidsentinel.hexmap.view.mapColor.AbstractCellColorExtractor;
import org.voidsentinel.hexmap.view.mapColor.colorMapperRepository;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.texture.Texture;
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
		HexMap map = new HexMap(256, 128);
		generator.generate(map);
		TerrainImage.generateImage(map, true);

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
		
		panel.setBackground(new QuadBackgroundComponent(new ColorRGBA(0.36f, 0.54f, 0.66f, 0.75f)));
		panel.setPreferredSize(new Vector3f(settings.getWidth(),  25f, 0f));
		panel.setLocalTranslation(0, settings.getHeight(), 0);
		
		TextField hooverField = new TextField("", new ElementId("tooltipElement"));
		hooverField.setColor(ColorRGBA.White);
		hooverField.setPreferredSize(new Vector3f(settings.getWidth(),  25f, 0f));
		hooverField.setTextVAlignment(VAlignment.Center);
      panel.addChild(hooverField);		
		
		addMapRepresentationRoll(0, hooverField);
		addMapFeometryRoll(1, hooverField);
		addExitButton(hooverField, "Quit game");

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

	private void addMapRepresentationRoll(int position, TextField hooverField) {
		// defaul value
		AbstractCellColorExtractor extract = colorMapperRepository.repository.getDefaultMapper();
		ImageData image = ImageRepository.datas.getData(extract.getIconName());
		String fileName = image.getFilename();
		IconComponent icon = new IconComponent(fileName);

		// Create a simple container for view elements
		DropDownButton ddb = new DropDownButton("", icon, new ElementId("visual"));
		guiNode.attachChild(ddb);
		ddb.setLocalTranslation(position * (DropDownButton.ICONSIZE+2), this.settings.getHeight() - hooverField.getPreferredSize().y-1, 0);
		ddb.setHooverField(hooverField);

		int count = -1;
		Iterator<Map.Entry<String, AbstractCellColorExtractor>> it = colorMapperRepository.repository.datas.entrySet()
		      .iterator();
		while (it.hasNext()) {
			Map.Entry<String, AbstractCellColorExtractor> colorizer = it.next();
			count++;
			icon = null;
			String iconName = colorizer.getValue().getIconName();
			if (iconName != null) {
				image = ImageRepository.datas.getData(iconName);
				if (image != null) {
					fileName = image.getFilename();
					if (fileName != null) {
						icon = new IconComponent(fileName);
						icon.setIconSize(new Vector2f(32, 32));
					}
				}
			}
			ddb.addButton("", icon, colorizer.getValue().getTextName(),
			      new VisualCommand(colorMapperRepository.repository.getData(colorizer.getKey())));
			if (extract == colorizer.getValue()) {
				ddb.setSelected(count);
			}
		}
	}

	private void addMapFeometryRoll(int position, final TextField hooverField) {
		// defaul value
		ImageData image = ImageRepository.datas.getData("mapRepresentationIcon");
		String fileName = image.getFilename();
		IconComponent icon = new IconComponent(fileName);

		// Create a simple container for view elements
		DropDownButton ddb = new DropDownButton("", icon, new ElementId("geometry"));
		guiNode.attachChild(ddb);
		ddb.setLocalTranslation(position * (DropDownButton.ICONSIZE+2), this.settings.getHeight() - hooverField.getPreferredSize().y-1, 0);
      ddb.setHooverField(hooverField);
      
	   ddb.addButton("Medium",   null, "", new GeometryCommand("org.voidsentinel.hexmap.view.HexGridChunkSlopped"));
	   ddb.addButton("Low",      null, "", new GeometryCommand("org.voidsentinel.hexmap.view.HexGridChunkFlat"));
	   ddb.addButton("very Low", null, "", new GeometryCommand("org.voidsentinel.hexmap.view.HexGridChunkFlatSimple"));
	
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

	private Button createMenuButton(Container panel, String text, String id, TextField hooverField, String toolipText) {
		Button hexButton = createButton(id, text, hooverField, toolipText);
		panel.addChild(hexButton);
		return hexButton;
	}

	private Button createButton(String id, String text, final TextField hooverField, final String toolipText) {
		Button hexButton = new Button(text, new ElementId(id));
		hexButton.addCommands(Button.ButtonAction.HighlightOn, new Command<Button>() {
			@Override
			public void execute(Button source) {
				// set the tooltip using the name
				hooverField.setText(toolipText);
				source.setColor(ColorRGBA.Yellow);
			}
		});
		hexButton.addCommands(Button.ButtonAction.HighlightOff, new Command<Button>() {
			@Override
			public void execute(Button source) {
				// set the tooltip using the name
				hooverField.setText("");
			}
		});
		return hexButton;
	}

	private void addExitButton(TextField hooverField, String toolipText) {
		Button bt = createButton("exitButton", "", hooverField, toolipText);
		ImageData image = ImageRepository.datas.getData("exitIcon");
		String fileName = image.getFilename();
		IconComponent icon = new IconComponent(fileName);
		icon.setIconSize(new Vector2f(32, 32));
		bt.setIcon(icon);
		bt.setText("");
		bt.addClickCommands(new Command<Button>() {
			@Override
			public void execute(Button source) {
				HexTuto.getInstance().stop();
			}
		});

		guiNode.attachChild(bt);
		bt.setLocalTranslation(this.settings.getWidth() - 35, this.settings.getHeight() - 25, 0);

	}
}
