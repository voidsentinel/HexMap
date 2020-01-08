/**
 * 
 */
package org.voidsentinel.hexmap.control.screen;

import org.voidsentinel.hexmap.control.GameStateMap;

import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;

/**
 * A command to use the game's statemap to go to a given screen (State) This is
 * used as the commnd for a button
 * 
 * @author VoidSentinel
 *
 */
public class ToScreenCommand implements Command<Button> {

	private String name = "";

	public ToScreenCommand(String name) {
		this.name = name;
	}

	@Override
	public void execute(Button source) {
		GameStateMap.getInstance().moveToState(name);
	}

}
