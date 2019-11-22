/**
 * 
 */
package org.voidsentinel.hexmap.control.screen;

import org.voidsentinel.hexmap.HexTuto;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;

/**
 * @author guipatry
 *
 */
public class MapGenerationScreen extends GameState {

	public MapGenerationScreen(final HexTuto application, final String id) {
		super(application, id);
	}

	public MapGenerationScreen(final HexTuto application) {
		super(application);
	}

	@Override
	public void initialize(final AppStateManager stateManager, final Application app) {
		LOG.info("...Initializing State '" + this.id + "'");
		super.initialize(stateManager, app);
	}

	@Override
	public void cleanup() {
		LOG.info("...Cleaning State '" + this.id + "'");
		super.cleanup();
	}
	
	
	
	
}
