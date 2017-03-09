/*
 * Description: This class handles the inputs provided by the user playing the game. The inputs are keystrokes.
 * The class implements the key storkes onto the game instance.
 */
package com;

// Importing KeyEvent class to register key strokes and create events based on key press, release etc.
// Importing KeyListener class to allow InputHandler to process the created Key events through an interface.

import java.awt.event.KeyEvent;		
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener{
	Game game;					// An instance of the game is created to implement the keystrokes onto
	
	/*
	 * Method Description: Method for setting locally created game instance to parameterized game instance
	 * @param game parameter recieved for the game instance from the method call
	 */
	public InputHandler(Game game) {
		this.game = game;		// set the locally made game instance to the recieved parameter
	}
	
	/*
	 * Method Description: Method called once the user has pressed a key and the listening begins
	 * @param arg0 event created with the key press
	 */
	
	@Override				
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * Method Description: Method called once the user has releases a key and the listening ends
	 * @param arg0 event for key release
	 */
	
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	/*
	 * Method Description: Method called when a key has been typed by the user. Event is created to register the key stroke
	 * made by the user. Method implements the event onto the game instance 
	 * @param e event created to register the key stroke
	 */
	
	@Override
	public void keyTyped(KeyEvent e) {
		game.typed(e.getKeyChar());		// implements the keystroke onto the game instance 
	}
	
	/*
	 * Method Description: Method for creating a new instance of the game for the input handler for a new game
	 * @param game2 is the new parameterized instance of the game recieved from the method call.
	 */
	
	public void changeGame(Game game2) {
		this.game = game2;		// changes the active game instance to the newly recieved instance to start new game
	}

}
