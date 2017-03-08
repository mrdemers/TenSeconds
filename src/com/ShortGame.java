/*
 * Class Description: 
 */
package com;

import java.awt.BorderLayout;			// import the java BorderLayout class for layout managent of the game window
import java.awt.Canvas;				// import canvas to have a window to draw the game onto
import java.awt.Color;				// import the color library to add color to the gaming window
import java.awt.Graphics;			// graphics libraries to allow for the animation
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;		// import BufferStrategy to allow pre-rendering and image buffer 
import java.awt.image.VolatileImage;		// import ValatileImage as another graphics buffer

import javax.swing.JFrame;			//import JFrame and JPanel for create graphical window for the user to see the game
import javax.swing.JPanel;


public class ShortGame extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;	// serialVersionUID defined for valid program serialization/deserializatio
	public static final String title = "Can you type this";	// title of the game screen
	public Thread thread;					// create a new Thread instance for game updating and rendering
	public boolean running = false;				// variable to check if the game is running to continue updating
	public InputHandler handler;				
	public Game game;			
	public VolatileImage image;			
	public static double[] bestTimes = new double[3];	// array to hold the best times in which game was completed
	public static boolean makeNewGame = false;		// variable to to create a new game instance
	
	/*
	* Method Description: Constructor class to develop a new new instance of Short Game and initialize new Game and input handler
	* objects and begin key listening
	*/
	public ShortGame() {
		bestTimes[0] = 7.0;		// sets the 3 high scores for time
		bestTimes[1] = 8.5;
		bestTimes[2] = 10.0;
		game = new Game(bestTimes);		
		handler = new InputHandler(game); // sets the input handler to deal with inputs from the game intialized above
		this.addKeyListener(handler);
	}
	
	/*
	* Method Description: Method for executing the Thread instance created allowing the updating of score values and 
	* redering of the game screen
	*/
	public void run() {
		last = System.currentTimeMillis();		// to hold the system time for keeping track of redering updates
		while (running) {				// as long as the game session is running continues to render and 
			update();				// update the game status
			render();
		}
	}
	/*
	 * Method Description: Method to set a new game instance has begun
	*/
	public static void newGame() {
		makeNewGame = true;
	}
	
	long last;
	/*
	* Method Description: Method for updating game status and scores
	*/
	public void update() {
		long now = System.currentTimeMillis();
		long passed = now - last;			// calculates the elapsed time since the last time measurement
		last = now;					// sets the last time to be the most recently obtained time
		double delta = passed/1000.0;			
		game.update(delta);				// calls the update method for the game object to update the elapsed time
		if (makeNewGame) {				// resets the game values when a new game instance is created
			bestTimes = game.bestTimes;
			game = new Game(bestTimes);
			handler.changeGame(game);
			makeNewGame = false;
		}
	}
	/*
	* Method Description: Method for rendering the game images and game screen
	*/
	public void render() {
		// defines the image with the image dimensions retrieved from the game instance if there is no image
		if (image == null) {
			image = createVolatileImage(this.getWidth(), this.getHeight());
		}
		// defines BufferStrategy for buffering images before displaying them on screen
		BufferStrategy bs = getBufferStrategy();
		// creates a new buffer strategy when none exist for storign images
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		// block creates the graphics image and utilizes the buffer to store and show the image on the screen
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(Color.getColor("", 0xffbb59));			
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		game.render(g2d);				// the graphics is rendered before being displayed on the screen
		Graphics g = bs.getDrawGraphics();		
		g.drawImage(image, 0, 0, null);			// draws the image but it is not yet displayed
		bs.show();					
		g.dispose();					// disposes the game resources
	}
	/*
	* Method Description: Method for starting a new game thread
	*/
	public void start() {
		if (running) return;		// if a game instance is already running then new instance can't be started
		running = true;			// sets and starts new game instance if one is not already running
		thread = new Thread(this);
		thread.start();
	}
	/*
	* Method Description: Method for stopping a game instance
	*/
	public void stop() {
		if (!running) return;		// if a game instance is not running then it can't be stopped
		running = false;		// if an instance is running changes the status to indicate it is stopped and stops the 
		try {				// instance
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/*
	* Method Description: Main method for creating the game ShortGame object, creating the game frame and passing in arguments for
	* to set up the game environment
	*/
	public static void main(String[] args) {
		ShortGame g = new ShortGame();				
		JFrame frame = new JFrame(title);				// creates the main game frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		// game's frame properties are set (size, resizability)
		frame.setResizable(false);
		frame.setSize(600, 500);
		JPanel panel = new JPanel(new BorderLayout());			// Panel is created to which the game instance would be
		panel.add(g);							// added
		frame.add(panel);						
		frame.setVisible(true);						// the panel is added to the frame to be displayed
		g.start();
	}
}
