package org.voidsentinel.hexmap.control;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.voidsentinel.hexmap.HexTuto;
import org.voidsentinel.hexmap.control.screen.GameState;

public class GameStateMap {

	protected static final Logger		LOG		= Logger.getLogger(GameStateMap.class.toString());

	private static GameStateMap		instance;

	private Map<String, GameState>	states	= new HashMap<>();
	protected Map<String, Object>		datas		= new HashMap<>();

	private GameState						current	= null;
	private GameState						start		= null;
	private HexTuto						application;

	/**
	 * private constructor
	 * 
	 * @param application the application to use.
	 */
	private GameStateMap(final HexTuto application) {
		// @todo : use an interface for the application functionnalities
		this.application = application;
	}

	/**
	 * return a (the) singleton GameStateMap
	 * 
	 * @return the singleton instance
	 */
	public static GameStateMap getInstance() {
		if (instance == null) {
			instance = new GameStateMap(HexTuto.getInstance());
		}
		return instance;
	}


	public GameState getStartState() {
		return start;
	}

	/**
	 * indicate that we wish to move to another state. this new state will know
	 * which state called it.
	 * 
	 * @param statename the name of the state we wish to go to
	 */
	public void moveToState(final String statename) {
		GameState state = this.getState(statename.trim().toLowerCase());
		moveToState(state);
	}

	public void moveToState(final GameState state) {
		LOG.info("moving to " + state.getId());
		if (current != null) {
			state.setCaller(current.getId());
			application.getStateManager().detach(current);
		}
		current = state;
		application.getStateManager().attach(current);
	}

	/**
	 * return to the caller state, if not null
	 */
	public void moveBack() {
		if (current.getClass() != null) {
			GameState caller = states.get(current.getCaller());
			application.getStateManager().detach(current);
			current = caller;
			application.getStateManager().attach(current);
		}
	}

	/**
	 * add a GameState to the list of managed GameState
	 * 
	 * @param state the state to add. If a GameState already exist with the same Id,
	 *              it will be replaced
	 */
	public void addState(final GameState state) {
		states.put(state.getId().trim().toLowerCase(), state);
	}

	/**
	 * add a GameState to the list of managed GameState
	 * 
	 * @param state the state to add. If a GameState already exist with the same Id,
	 *              it will be replaced
	 */
	public void addState(final GameState state, boolean initial) {
		states.put(state.getId(), state);
		if (initial == true) {
			start = state;
		}
	}

	/**
	 * return the GameState with the given id
	 * 
	 * @param id the id of the wanted GameState
	 * @return the corresponding GameState
	 */
	private GameState getState(final String id) {
		return states.get(id.trim().toLowerCase());
	}

	/**
	 * set the start GameState as the current GameState
	 */
	public void moveToStart() {
		current = start;
		application.getStateManager().attach(current);
	}

	public void reInitialize() {
		Iterator<String> it = states.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			LOG.info("--reInitializing " + key);
			states.get(key).reInitialize();
		}

	}

	/**
	 * return the current State, as defined by the succession of move operation
	 * between states
	 * 
	 * @return the current state
	 */
	public GameState getCurrentState() {
		return current;
	}

	/**
	 * used to store data between states
	 * 
	 * @param name the name of the data
	 * @param data
	 */
	public void setData(final String name, final Object data) {
		datas.put(name, data);
	}

	/**
	 * get back data that was stored by a(nother) state
	 * 
	 * @param name the name of the data
	 * @return the object
	 */
	public Object getData(final String name) {
		return datas.get(name);
	}

}
