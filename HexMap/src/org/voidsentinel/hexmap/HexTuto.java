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
import org.voidsentinel.hexmap.model.mapgenerator.CaptitalimeGenerator;
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
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.style.BaseStyles;

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
		
		
		GuiGlobals.initialize(this);
		Alea.setSeed(654);
		// Load the 'glass' style
		BaseStyles.loadGlassStyle();
		GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

		// load look & Feel
		ModList mods = ModLoader.getModsFromDirectories("assets/mod");
		ModData std = mods.get("standard");
		ModLoader.loadMod(std, this.assetManager);

		// generate the map
		MapGenerator generator = new CaptitalimeGenerator();
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

		// Create a simple container for our elements
		Container myWindow = new Container();
		guiNode.attachChild(myWindow);
		myWindow.setLocalTranslation(0, 800, 0);

		Iterator<Map.Entry<String, AbstractCellColorExtractor>> it = colorMapperRepository.repository.datas.entrySet()
		      .iterator();
		while (it.hasNext()) {
			Map.Entry<String, AbstractCellColorExtractor> colorizer = it.next();
			Button button = myWindow.addChild(new Button(colorizer.getKey()));
			String iconName = colorizer.getValue().getIconName();
			if (iconName != null) {
				ImageData image = ImageRepository.datas.getData(iconName);
				if (image != null) {
					String fileName = image.getFilename();
					if (fileName != null) {
						IconComponent icon = new IconComponent(fileName);
						icon.setIconSize(new Vector2f(32,32));
						button.setIcon(icon);
						button.setText(iconName);
						button.setTextVAlignment(VAlignment.Center);
						button.setTextHAlignment(HAlignment.Right);
					}
				}
			}
			button.addClickCommands(new Command<Button>() {
				@Override
				public void execute(Button source) {
					mapNode.setColorExtractor(colorMapperRepository.repository.getData(colorizer.getKey()));
				}
			});
		}

		Button hexButton = myWindow.addChild(new Button("Hexagon"));
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

		Button triButton = myWindow.addChild(new Button("Triangle"));
		triButton.addClickCommands(new Command<Button>() {
			@Override
			public void execute(Button source) {
				try {
					mapNode.setMeshGeneration("org.voidsentinel.hexmap.view.HexGridChunkTriangle");
				} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				      | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		Button tri2Button = myWindow.addChild(new Button("Triangle 2"));
		tri2Button.addClickCommands(new Command<Button>() {
			@Override
			public void execute(Button source) {
				try {
					mapNode.setMeshGeneration("org.voidsentinel.hexmap.view.HexGridChunkTriangle2");
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
