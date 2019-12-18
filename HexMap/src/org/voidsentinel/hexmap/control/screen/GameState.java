/**
 * Project JMEMapper
 */
package org.voidsentinel.hexmap.control.screen;

import java.util.logging.Logger;

import org.voidsentinel.hexmap.HexTuto;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

/**
 * This is an AppState that correspond to a page of the application.
 * Initialization/removal are managed
 * 
 * @author Guillaume
 */
public abstract class GameState extends AbstractAppState {

	protected static final Logger	LOG			= Logger.getLogger(GameState.class.toString());

	// State execution context
	protected final String			id;
	protected String					caller;
	protected HexTuto					application;
	protected AppStateManager		stateManager;
	protected boolean					displayed	= false;
	// State content
	protected AssetManager			assetManager;
	protected Node						rootNode;
	protected Node						guiNode;
	// camera informations
	protected Camera					saveCamera;

	/**
	 *
	 * @param application
	 */
	public GameState(final HexTuto application, final String id) {
		this.id = id;
		this.application = application;
		this.assetManager = application.getAssetManager();
		LOG.info("...Creating State '" + id + "'");
	}

	public GameState(final HexTuto application) {
		this.id = this.getClass().getSimpleName();
		this.application = application;
		this.assetManager = application.getAssetManager();
		LOG.info("...Creating State '" + id + "'");
	}

	/**
	 * called when the state is attached to the screen (ie the state become one of
	 * the active state). attach the rootNode, the GUI screen and set the camera as
	 * it was last call (if this is not the first call)
	 * <p>
	 * Child classes should initialize the protected rootNode and guiNode before
	 * calling super.initialize(...).
	 *
	 * @param stateManager
	 * @param app
	 */
	@Override
	public void initialize(final AppStateManager stateManager, final Application app) {
		this.stateManager = stateManager;

		super.initialize(stateManager, app);

		// place the graphic node
		if (rootNode != null) {
			application.getRootNode().attachChild(rootNode);
		}

		// place the gui node
		if (guiNode != null) {
			application.getGuiNode().attachChild(guiNode);
		}

		// set camera state
		if (saveCamera != null)
			application.getCamera().copyFrom(saveCamera);
		displayed = true;

	}

	/**
	 * called when the state is detached from the screen (ie the state is no more
	 * active)
	 */
	@Override
	public void cleanup() {
		super.cleanup();
		// spatial nodes
		if (rootNode != null) {
			application.getRootNode().detachChild(rootNode);
		}
		// Gui Node
		if (guiNode != null) {
			application.getGuiNode().detachChild(guiNode);
		}
		// camera
		if (saveCamera == null) {
			saveCamera = application.getCamera().clone();
		}
		saveCamera.copyFrom(application.getCamera());
		displayed = false;
	}

	public void reInitialize() {
		if (displayed) {
			cleanup();
		}
		if (rootNode != null) {
			rootNode.detachAllChildren();
			rootNode = null;
		}
		if (displayed) {
			this.initialize(stateManager, application);
		}
	}

	/**
	 * return the id of the game state, as defined in the constructor
	 *
	 * @return
	 */
	public final String getId() {
		return this.id;
	}

	/**
	 * return the id of the state that called this one
	 *
	 * @return
	 */
	public final String getCaller() {
		return caller;
	}

	/**
	 * set the id of the state that called this one
	 *
	 * @param caller
	 */
	public final void setCaller(final String caller) {
		this.caller = caller;
	}

	/**
	 * return the graphcal root of the state
	 *
	 * @return
	 */
	public final Node getRootNode() {
		return rootNode;
	}

}
