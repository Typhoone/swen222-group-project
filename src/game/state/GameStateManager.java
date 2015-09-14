package game.state;

import java.awt.Graphics2D;

/**
 * GameStateManager holds the current state for the game that should be actively
 * updated and rendered to the screen.
 */

public class GameStateManager {
	// Variables
	private GameState state;

	// Game objects
	private State menuState, level1State;

	/**
	 * Constructs a new GameStateManager which is
	 * 
	 * @param state
	 */
	public GameStateManager(GameState state) {
		menuState = new MenuState();
		level1State = new Level1State();
		this.state = state;
	}

	/**
	 * Updates the current state.
	 */
	public void update() {
		switch (state) {
		case MENU:
			menuState.update();
			break;
		case LEVEL1:
			level1State.update();
			break;
		}
	}

	/**
	 * Renders the current state to the screen.
	 * 
	 * @param g2
	 *            - graphics object to use for rendering
	 */
	public void render(Graphics2D g2) {
		switch (state) {
		case MENU:
			menuState.render(g2);
			break;
		case LEVEL1:
			level1State.render(g2);
			break;
		}
	}

	/**
	 * State is an enum used for specifying the current state to be updated and
	 * rendered.
	 */
	public enum GameState {
		MENU, LEVEL1
	}
}
