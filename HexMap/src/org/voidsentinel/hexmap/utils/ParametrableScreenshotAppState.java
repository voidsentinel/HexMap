/**
 * 
 */
package org.voidsentinel.hexmap.utils;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;

/**
 * @author guipatry
 *
 */
public class ParametrableScreenshotAppState extends ScreenshotAppState {

	private int linkedKey = KeyInput.KEY_SYSRQ;

	
	

	public ParametrableScreenshotAppState() {
		super();
		// TODO Auto-generated constructor stub
	}


	public ParametrableScreenshotAppState(String filePath) {
		super(filePath);
	}

	public ParametrableScreenshotAppState(String filePath, long shotIndex) {
		super(filePath, shotIndex);
		// TODO Auto-generated constructor stub
	}



	public ParametrableScreenshotAppState(String filePath, String fileName, long shotIndex) {
		super(filePath, fileName, shotIndex);
		// TODO Auto-generated constructor stub
	}



	public ParametrableScreenshotAppState(String filePath, String fileName) {
		super(filePath, fileName);
		// TODO Auto-generated constructor stub
	}



	public void changeLinkedKey(AppStateManager stateManager, Application app, int key) {
		InputManager inputManager = app.getInputManager();
		if (linkedKey != 0) {
			inputManager.deleteMapping("ScreenShot");
		}
		linkedKey = key;
		inputManager.addMapping("ScreenShot", new KeyTrigger(linkedKey));
	}

}
