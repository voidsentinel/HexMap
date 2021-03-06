/**
 * 
 */
package org.voidsentinel.hexmap.view.ihm;

import org.voidsentinel.hexmap.model.HexCell;
import org.voidsentinel.hexmap.view.HexGrid;
import org.voidsentinel.hexmap.view.HexMetrics;

import com.jme3.app.Application;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 * @author Xerces
 *
 */
public class StepCameraControl extends AbstractControl implements ActionListener {

	// private static final Logger LOG =
	// Logger.getLogger(StepCameraControl.class.toString());

	// ** Total time for a movement
	private static float	MVTTIME				= 20.0f;

	float						positionElapsed	= 0;
	boolean					positionChange		= false;
	Vector3f					startPosition		= null;
	Vector3f					stopPosition		= null;
	Vector3f					currentPosition	= null;
	Vector3f					normal				= null;

	float						targetElapsed		= 0;
	boolean					targetChange		= false;
	Vector3f					startTarget			= null;
	Vector3f					stopTarget			= null;
	Vector3f					currentTarget		= null;

	float						zoomStep				= 0.0f;
	boolean					farMaterial			= false;

	private Camera			camera				= null;
	private Application	application;
	private HexGrid		mapdisplay;

	boolean					topMode				= true;
	Vector3f					deltaCam				= new Vector3f();

	/**
	 * create a camera
	 * 
	 * @param application
	 * @param mapDisplay
	 * @param position
	 * @param target
	 * @param normal
	 */
	public StepCameraControl(final Application application, final HexGrid mapDisplay, final Vector3f target,
	      Vector3f normal) {

		this.application = application;
		this.camera = application.getCamera();
		this.mapdisplay = mapDisplay;
		this.normal = normal;

		this.currentTarget = target;
		this.startTarget = target;
		this.stopTarget = target;

		Vector3f top = target.add(-0f, -15f, -15f);
//		Vector3f top = target.add(-0f, -15f, 0.001f);
		this.currentPosition = top;
		this.startPosition = top;
		this.stopPosition = top;

		this.topMode = false;
		this.deltaCam = target.subtract(top);

		camera.setLocation(top);
		camera.lookAt(target, normal);
		camera.update();
	}

	public void setMapDisplay(final HexGrid mapDisplay) {
		this.mapdisplay = mapDisplay;
		updateCamera();
	};

	public void updateCamera() {
		camera.setLocation(stopPosition);
		camera.lookAt(stopTarget, normal);
		camera.update();
	}

	/**
	 * change the grid looked at. If the target cell is not in the new map, then
	 * 
	 * @param grid
	 */
	public void changeMap(HexGrid grid) {

	}

	public void addControlMapping() {
		final InputManager inputManager = application.getInputManager();
		// map one (or several) inputs to one named action
		inputManager.addMapping("Rotate+", new KeyTrigger(KeyInput.KEY_NUMPAD9));
		inputManager.addMapping("Rotate-", new KeyTrigger(KeyInput.KEY_NUMPAD7));
		inputManager.addMapping("RightClick", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

		inputManager.addMapping("ZoomOut", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
		inputManager.addMapping("ZoomIn", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));

		// add the camera as listener for those actions
		inputManager.addListener(this, "Rotate+", "Rotate-", "RightClick", "ZoomIn", "ZoomOut");

		// The only input we need for android, touch. The engine automatically
		// translates touch events into mouse events for Android, if you were wondering.
		// In fact, the engine can pretty much be used the same way as it were to be
		// used on desktop, except that resolutions are different.
		inputManager.addMapping("Rotate", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

		// Add the names to the touch listener.
		inputManager.addListener(touchMoveListener, new String[] { "Rotate" });
		inputManager.addListener(touchListener, new String[] { "Rotate" });

	}

	public void removeControlMapping() {
		final InputManager inputManager = application.getInputManager();
		inputManager.deleteMapping("ZoomOut");
		inputManager.deleteMapping("ZoomIn");
		inputManager.deleteMapping("Rotate+");
		inputManager.deleteMapping("Rotate-");
		inputManager.deleteMapping("RightClick");
		inputManager.deleteMapping("Rotate");

		application.getInputManager().removeListener(this);
		application.getInputManager().removeListener(touchMoveListener);
		application.getInputManager().removeListener(touchListener);
	}

	public void changeViewMode() {
		if (topMode) {
			stopPosition = stopTarget.subtract(deltaCam);
			topMode = !topMode;
		} else {
			stopPosition = stopTarget.add(-0.1f, stopPosition.getY(), 0.1f);
			topMode = !topMode;
		}
		positionChange = true;
		positionElapsed = 0.0f;
	}

	public void setPosition(final Vector3f target) {
		startPosition = currentPosition;
		stopPosition = target;
		positionChange = true;
		positionElapsed = 0.0f;
		if (!topMode)
			deltaCam = stopTarget.subtract(stopPosition);
	}

	public void rotatePosition(final float value) {
		final Vector3f delta = stopPosition.subtract(stopTarget);
		// rotate delta
		final Quaternion rotation = new Quaternion();
		rotation.fromAngleAxis((FastMath.TWO_PI / 12f) * value, normal);
		rotation.mult(delta, delta);
		// add delta to
		stopPosition = stopTarget.add(delta);
		positionChange = true;
		positionElapsed = 10.0f;
		if (!topMode)
			deltaCam = stopTarget.subtract(stopPosition);
	}

	public void setTarget(final Vector3f to) {
		if (stopTarget != to) {
			final Vector3f delta = to.subtract(stopTarget);
			stopTarget = to;
			stopPosition = stopPosition.add(delta);
			positionChange = true;
			positionElapsed = 0.0f;
			targetChange = true;
			targetElapsed = 0.0f;
			if (!topMode)
				deltaCam = stopTarget.subtract(stopPosition);
		}
	}

	@Override
	protected void controlUpdate(final float tpf) {
		if (camera != null) {
			// if the camera position is changing
			if (positionChange) {
				positionElapsed = positionElapsed + tpf;
				if (positionElapsed >= MVTTIME) {
					positionElapsed = MVTTIME;
					positionChange = false;
					startPosition = stopPosition;
					currentPosition = stopPosition;
				} else {
					final float interpolation = positionElapsed / MVTTIME;
					currentPosition.interpolateLocal(startPosition, stopPosition, interpolation);
				}
				camera.setLocation(currentPosition);
				camera.lookAt(currentTarget, normal);
			}
			// if the target is changing
			if (targetChange) {
				targetElapsed = targetElapsed + tpf;
				if (targetElapsed > MVTTIME) {
					targetElapsed = MVTTIME;
					targetChange = false;
					startTarget = stopTarget;
					currentTarget = stopTarget;
				} else {
					final float interpolation = targetElapsed / MVTTIME;
					currentTarget.interpolateLocal(startTarget, stopTarget, interpolation);
				}
				camera.setLocation(currentPosition);
				camera.lookAt(currentTarget, normal);
			}
		}

	}

	public void zoomIn() {
		final Vector3f orientation = stopPosition.subtract(stopTarget);
		stopPosition.subtractLocal(orientation.multLocal(0.1f));
		positionChange = true;
		positionElapsed = 0.0f;
		if (!topMode)
			deltaCam = stopTarget.subtract(stopPosition);

	}

	public void zoomOut() {
		final Vector3f orientation = stopPosition.subtract(stopTarget);
		stopPosition.addLocal(orientation.multLocal(0.1f));
		positionChange = true;
		positionElapsed = 0.0f;
		// change texture if to far.
		if (!topMode)
			deltaCam = stopTarget.subtract(stopPosition);

	}

	@Override
	public void onAction(final String name, final boolean keyPressed, final float tpf) {
		// LOG.info("Action :" + name);
		if (keyPressed) {
			if ("Rotate+".equals(name)) {
				rotatePosition(1f);
			}
			if ("Rotate-".equals(name)) {
				rotatePosition(-1f);
			}
			if ("ZoomIn".equals(name)) {
				zoomIn();
				;
			}
			if ("ZoomOut".equals(name)) {
				zoomOut();
			}

			if ("RightClick".equals(name)) {
				final Vector2f cursor = application.getInputManager().getCursorPosition();
				HexCell cell = mapdisplay.getCellPointed(cursor, camera);
				if (cell != null) {
					Vector3f target = HexMetrics.getCellCenter(cell);
					this.setTarget(target);
				}
			}
		}
	}

	@Override
	protected void controlRender(final RenderManager rm, final ViewPort vp) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the camera
	 */
	public Camera getCamera() {
		return camera;
	}

	// data for "swipe" listener
	private Vector2f			mousePreviousPosition	= null;
	private boolean			applyAnalog					= false;
	private ActionListener	touchListener				= new ActionListener() {
																		@Override
																		public void onAction(String name, boolean keyPressed, float tpf) {
																			final InputManager inputManager = application
																			      .getInputManager();
																			if (keyPressed) {
																				mousePreviousPosition = inputManager.getCursorPosition()
																				      .clone();
																				applyAnalog = true;
																			} else {
																				applyAnalog = false;

																			}
																		}

																	};
	private AnalogListener	touchMoveListener			= new AnalogListener() {
																		@Override
																		public void onAnalog(String name, float value, float tpf) {
																			final InputManager inputManager = application
																			      .getInputManager();
																			if (applyAnalog) {
																				Vector2f MousePosition = inputManager.getCursorPosition()
																				      .clone();

																				rotatePosition(-1f
																				      * (MousePosition.x - mousePreviousPosition.x) / 100f);

																				mousePreviousPosition = MousePosition;
																			}
																		}
																	};
}
