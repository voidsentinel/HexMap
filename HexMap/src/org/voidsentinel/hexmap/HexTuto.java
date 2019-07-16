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
import org.voidsentinel.hexmap.view.ihm.ImageData;
import org.voidsentinel.hexmap.view.ihm.ImageRepository;
import org.voidsentinel.hexmap.view.ihm.StepCameraControl;
import org.voidsentinel.hexmap.view.mapColor.AbstractCellColorExtractor;
import org.voidsentinel.hexmap.view.mapColor.colorMapperRepository;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Button.ButtonAction;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.RollupPanel;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.style.BaseStyles;
import com.simsilica.lemur.style.ElementId;

/**
 * @author guipatry
 *
 */
public class HexTuto extends SimpleApplication {

	private static HexTuto instance;

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
		//
//		BaseStyles.loadStyleResources("/themes/medieval/medieval.groovy");
//		GuiGlobals.getInstance().getStyles().setDefaultStyle("medieval");
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
		HexGrid mapNode = new HexGrid(map, this.getRootNode());

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

     // 
		Container tooltip = new Container();
		guiNode.attachChild(tooltip);
		TextField hooverField = new TextField("");
		tooltip.attachChild(hooverField);
		tooltip.setLocalTranslation(0, settings.getHeight(), 0);
		
		// Create a simple container for view elements
		RollupPanel roll = new RollupPanel("View", new ElementId("viewPanel"), null);
		guiNode.attachChild(roll);
		roll.setLocalTranslation(0, this.settings.getHeight()-25, 0);
		// set the initial icon
		Button bt = roll.getTitleElement();	
		AbstractCellColorExtractor extract = colorMapperRepository.repository.getDefaultMapper();
		ImageData image = ImageRepository.datas.getData(extract.getIconName());
		String fileName = image.getFilename();
		IconComponent icon = new IconComponent(fileName);
		icon.setIconSize(new Vector2f(32, 32));
		bt.setIcon(icon);
		bt.setText("");
		roll.setOpen(false);
      // set the possibles actions
		Container panel = new Container();
		roll.setContents(panel);
		Iterator<Map.Entry<String, AbstractCellColorExtractor>> it = colorMapperRepository.repository.datas.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<String, AbstractCellColorExtractor> colorizer = it.next();
			Button button = panel.addChild(new Button(colorizer.getKey()));
			tooltip.setBackground(button.getBackground().clone());
			hooverField.setBackground(button.getBackground().clone());
			String iconName = colorizer.getValue().getIconName();
			if (iconName != null) {
				image = ImageRepository.datas.getData(iconName);
				if (image != null) {
					fileName = image.getFilename();
					if (fileName != null) {
						icon = new IconComponent(fileName);
						icon.setIconSize(new Vector2f(32, 32));
						button.setIcon(icon);
						button.setText("");
						// button.setText(iconName);
						// button.setTextVAlignment(VAlignment.Center);
						// button.setTextHAlignment(HAlignment.Right);
					}
				}
			}
			button.addClickCommands(new Command<Button>() {
				@Override
				public void execute(Button source) {
					Button bt = roll.getTitleElement();
					ImageData image = ImageRepository.datas.getData(iconName);
					String fileName = image.getFilename();
					IconComponent icon = new IconComponent(fileName);
					icon.setIconSize(new Vector2f(32, 32));
					bt.setIcon(icon);
					bt.setText("");
					roll.setOpen(false);
					mapNode.setColorExtractor(colorMapperRepository.repository.getData(colorizer.getKey()));
				}
			});
			button.addCommands(Button.ButtonAction.HighlightOn, new Command<Button>() {
				@Override
				public void execute(Button source) {
					// set the tooltip using the name
					hooverField.setText(colorizer.getValue().getTextName());
				}
			});
			button.addCommands(Button.ButtonAction.HighlightOff, new Command<Button>() {
				@Override
				public void execute(Button source) {
					// set the tooltip using the name
					hooverField.setText("");
				}
			});
		}

		Button hexButton = panel.addChild(new Button("Slopped Hex"));
		hexButton.addClickCommands(new Command<Button>() {
			@Override
			public void execute(Button source) {
				try {
					mapNode.setMeshGeneration("org.voidsentinel.hexmap.view.HexGridChunkSlopped");
				} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				      | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		Button triButton = panel.addChild(new Button("Flat Hex"));
		triButton.addClickCommands(new Command<Button>() {
			@Override
			public void execute(Button source) {
				try {
					mapNode.setMeshGeneration("org.voidsentinel.hexmap.view.HexGridChunkFlat");
				} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				      | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

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

}
