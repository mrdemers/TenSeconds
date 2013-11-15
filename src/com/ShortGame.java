package com;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ShortGame extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	public static final String title = "Can you type this";
	public Thread thread;
	public boolean running = false;
	public InputHandler handler;
	public Game game;
	public VolatileImage image;
	public static double[] bestTimes = new double[3];
	public static boolean makeNewGame = false;
	
	public ShortGame() {
		bestTimes[0] = 7.0;
		bestTimes[1] = 8.5;
		bestTimes[2] = 10.0;
		game = new Game(bestTimes);
		handler = new InputHandler(game);
		this.addKeyListener(handler);
	}
	
	public void run() {
		last = System.currentTimeMillis();
		while (running) {
			update();
			render();
		}
	}
	
	public static void newGame() {
		makeNewGame = true;
	}
	
	long last;
	public void update() {
		long now = System.currentTimeMillis();
		long passed = now - last;
		last = now;
		double delta = passed/1000.0;
		game.update(delta);
		if (makeNewGame) {
			bestTimes = game.bestTimes;
			game = new Game(bestTimes);
			handler.changeGame(game);
			makeNewGame = false;
		}
	}
	
	public void render() {
		if (image == null) {
			image = createVolatileImage(this.getWidth(), this.getHeight());
		}
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(Color.getColor("", 0xffbb59));
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		game.render(g2d);
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, null);
		bs.show();
		g.dispose();
	}
	
	public void start() {
		if (running) return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop() {
		if (!running) return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ShortGame g = new ShortGame();
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setSize(600, 500);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(g);
		frame.add(panel);
		frame.setVisible(true);
		g.start();
	}
}
