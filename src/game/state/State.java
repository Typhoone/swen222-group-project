package game.state;

import java.awt.Graphics2D;

public interface State {
	/**
	 * Updates the state.
	 */
	public void update();

	/**
	 * Renders the state to the screen.
	 * 
	 * @param g2
	 *            - graphics object to use for rendering
	 */
	public void render(Graphics2D g2);
}
