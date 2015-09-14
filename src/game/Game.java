package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import game.state.GameStateManager;
import game.state.GameStateManager.GameState;

/**
 * Game is designed to be the base class for the game. It is responsible for
 * setting up the UI, updating and rendering the game states and running the
 * gameloop.
 */

@SuppressWarnings("serial")
public class Game extends Canvas implements Runnable {
	// Game objects
	private GameStateManager gsm;
	private BufferedImage image;

	// Gameloop
	private Thread thread;
	private boolean running;

	// Canvas dimensions
	public static final int WIDTH = 320;
	public static final int HEIGHT = 180;
	public static final int SCALE = 4;

	/**
	 * Constructs a new Game object by initializing game objects, setting canvas
	 * dimensions and creating the JFrame.
	 */
	public Game() {
		// Game objects
		gsm = new GameStateManager(GameState.MENU);
		image = new BufferedImage(Game.WIDTH, Game.HEIGHT, BufferedImage.TYPE_INT_ARGB);

		// Canvas dimensions
		setMinimumSize(new Dimension(Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE));
		setMaximumSize(new Dimension(Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE));
		setPreferredSize(new Dimension(Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE));

		// Window to hold the canvas
		JFrame frame = new JFrame("Game Title");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	/**
	 * Updates the current frame for the current state.
	 */
	public void update() {
		// Update current state
		gsm.update();
	}

	/**
	 * Renders the current frame to the screen by creating the buffer strategy,
	 * graphics, rendering the current game state and drawing everything to the
	 * screen.
	 */
	public void render() {
		// Triple buffering
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		// Create graphics
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2 = (Graphics2D) image.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Clear screen
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

		// Render current state
		gsm.render(g2);

		// Draw to screen
		g.drawImage(image, 0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE, null);
		g.dispose();
		bs.show();
	}

	/**
	 * Controls the gameloop for the game by limiting updating and rendering to
	 * 60fps.
	 */
	public void run() {
		// Timing variables
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		double ns = 1000000000D / 60D;
		double delta = 0;
		int fps = 0;
		
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			// Update and render the frame
			if (delta >= 1) {
				update();
				render();
				delta--;
				fps++;
			}

			// [Debug] Show FPS
			if (System.currentTimeMillis() - timer > 1000) {
				System.out.println("FPS: " + fps);
				timer += 1000;
				fps = 0;
			}
		}
	}

	/**
	 * Starts the gameloop by initializing and starting the game thread.
	 */
	private synchronized void start() {
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	/*
	 * Main entry point for the game.
	 */
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
}
