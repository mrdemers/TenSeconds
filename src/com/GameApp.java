/*
 * Method Description: Class for allowing the game to be imbeded on a web page or to be viewed on java applet viewer
*/
	
package com;

import java.applet.Applet; 		// import the java Applet class that allows the program to be imbedded onto a web page
import java.awt.BorderLayout;		// import the java BorderLayout class for layout managent of the Applet window

public class GameApp extends Applet{
	private static final long serialVersionUID = 1L;	// serialVersionUID defined for valid program serialization/deserialization
	ShortGame game = new ShortGame();			// creates a new instance of the game 
	
	/*
	* Method Description: Method for intializing the 
	*/
	public void init() {
		setLayout(new BorderLayout());			// allows to implement a custom layout to the window
		add(game, BorderLayout.CENTER);			// adds the game instance to the center space of the applet window
	}
	
	
	/*
	* Method Description: Method for applet to start the execution 
	*/
	public void start() {			
		game.start();
	}
	
	/*
	* Method Description: Method for applet to stop the execution
	*/
	public void stop() {
		game.stop();
	}
}
