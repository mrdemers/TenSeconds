package com;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener{
	Game game;
	
	public InputHandler(Game game) {
		this.game = game;
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		game.typed(e.getKeyChar());
	}
	public void changeGame(Game game2) {
		this.game = game2;
	}

}
