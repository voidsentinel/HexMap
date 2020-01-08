/**
 * 
 */
package org.voidsentinel.hexmap;

import org.voidsentinel.hexmap.control.GameStateMap;
import org.voidsentinel.hexmap.mod.ModData;
import org.voidsentinel.hexmap.mod.ModList;
import org.voidsentinel.hexmap.mod.ModLoader;
import org.voidsentinel.hexmap.utils.Alea;
import org.voidsentinel.hexmap.view.ParametrableScreenshotAppState;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.input.KeyInput;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;

import net.wcomohundro.jme3.loris.GuiContext;

/**
 * @author guipatry
 *
 */
public class HexTuto extends SimpleApplication {

	private static HexTuto	instance;

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

		// allow for screenshot
		ParametrableScreenshotAppState screenshot = new ParametrableScreenshotAppState("",
		      this.getClass().getSimpleName());
		screenshot.setFilePath("./screenshot/");
		screenshot.changeLinkedKey(this.getStateManager(), this, KeyInput.KEY_PRTSCR);
		this.stateManager.attach(screenshot);

		getFlyByCamera().setEnabled(false);
		mouseInput.setCursorVisible(true);
		this.setDisplayStatView(false);
		
		GameStateMap.getInstance().moveToStart();

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		instance = new HexTuto();
		instance.start();
	}

	public void stop() {
		// save current status

		// and stop
		super.stop();
	}

	public static HexTuto getInstance() {
		return instance;
	}

	public AppSettings getSettings() {
		return settings;
	}
	
}