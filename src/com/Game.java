package com;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class Game {
	String sentence = "Can you type this extremely long sentence in only just ten seconds";
	String typed = "";
	double time = 0;
	double xShake = 0;
	double yShake = 0;
	boolean shaking = false;
	double shakeTime = 0;
	boolean start = false;
	int currentLine = 0;
	String[] pieces = new String[5];
	String[] pieces2 = new String[5];
	int highestInt = 1;
	double bloatTime = 0;
	float bloat = 35;
	boolean win, lose;
	double finishTime = 0;
	double rotate = 0;
	double scale = 0;
	double winTime;
	double[] bestTimes = new double[3];
	
	public Game(double[] best) {
		pieces[0] = "Can you type";
		pieces[1] = "this extremely";
		pieces[2] = "long sentence";
		pieces[3] = "in only just";
		pieces[4] = "ten seconds";
		for (int i = 0; i < pieces2.length; i++) {
			pieces2[i] = "";
		}
		bestTimes = best;
	}
	
	public void update(double delta) {
		if (start && !lose && !win) {
			time += delta;
			if (time > highestInt) {
				bloatTime = 0.2;
				highestInt++;
			}
			if (time >= 10) {
				lose();
			}
			if (shakeTime > 0) {
				shakeTime -= delta;
				xShake = Math.random()-.5;
				yShake = Math.random()-.5;
				xShake *= 3;
				yShake *= 3;
			}
			if (bloatTime > 0) {
				bloat = (float)(bloatTime/0.2*15)+35;
				bloatTime -= delta;
			}
		}
		else if (win || lose) {
			finishTime+=delta;
			if (rotate < .5) {
				rotate += delta;
				scale += delta * 4;
			}
		}
	}
	
	DecimalFormat format = new DecimalFormat("0.00");
	Font f1 = new Font("Rockwell Extra Bold", Font.BOLD, 50);
	Font f2 = new Font("Rockwell Extra Bold", Font.BOLD, 35);
	public void render(Graphics2D g) {	
		if (!win && !lose) {
			int yPos = 150;
			g.setFont(f1);
			g.setColor(Color.getColor("", 0x006605));
			g.drawString(pieces[0], 70+(int)xShake, yPos + 0 * 50+(int)yShake);
			g.drawString(pieces[1], 50+(int)xShake, yPos + 1 * 50+(int)yShake);
			g.drawString(pieces[2], 52+(int)xShake, yPos + 2 * 50+(int)yShake);
			g.drawString(pieces[3], 85+(int)xShake, yPos + 3 * 50+(int)yShake);
			g.drawString(pieces[4], 73+(int)xShake, yPos + 4 * 50+(int)yShake);
			
			g.setColor(Color.getColor("", 0x2764ff));
			g.drawString(pieces2[0], 70 + (int)xShake, yPos + 0 * 50+(int)yShake);
			g.drawString(pieces2[1], 50 + (int)xShake, yPos + 1 * 50+(int)yShake);
			g.drawString(pieces2[2], 52 + (int)xShake, yPos + 2 * 50+(int)yShake);
			g.drawString(pieces2[3], 85 + (int)xShake, yPos + 3 * 50+(int)yShake);
			g.drawString(pieces2[4], 73 + (int)xShake, yPos + 4 * 50+(int)yShake);
			
			g.setFont(f2.deriveFont(bloat));
			g.setColor(Color.red);
			float x, y;
			x = 230 + (1-(bloat/35))*50;
			y = 70 - (1-(bloat/35))*15;
			g.drawString(format.format(time), x, y);
			
			g.setColor(Color.blue);
			Point p = getLineX();
			g.drawLine(p.x, yPos+currentLine*50, p.y, yPos+currentLine*50);
		} else {
			Rectangle2D rec = null;
			if (win) {				
				rec = f1.getStringBounds("YES", new FontRenderContext(null, false, false));
			} else if (lose) {				
				rec = f1.getStringBounds("NO", new FontRenderContext(null, false, false));
			}
			BufferedImage image = new BufferedImage((int)rec.getWidth(), (int)rec.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics gg = image.getGraphics();
			gg.setFont(f1);
			float a = (float)scale/2.0f;
			if (a > 1) a = 1;
			gg.setColor(new Color(1, 0, 0, a));
			if (win)
				gg.drawString("YES", 0, 58);
			if (lose) {
				gg.drawString("NO", 0, 58);
			}
			AffineTransform at = new AffineTransform();
			at.translate(300, 190);
			at.rotate(-rotate, 40, 40);
			at.scale(scale, scale);
			at.translate(-image.getWidth()/2, -image.getHeight()/2);
			g.drawImage(image, at, null);
			g.setFont(new Font("Helvetica", Font.BOLD, 30));
			g.setColor(Color.red);
			g.drawString("Try Again (Enter)", 180, 420);
			if (win) {
				g.drawString("Time: " + format.format(winTime) + "s", 80, 50);
			}
			g.setFont(new Font("Helvetica", Font.BOLD, 12));
			g.drawString("Best Times:", 400, 30);
			g.drawString("(1) " + format.format(bestTimes[0]), 420, 60);
			g.drawString("(2) " + format.format(bestTimes[1]), 420, 80);
			g.drawString("(3) " + format.format(bestTimes[2]), 420, 100);
		}
	}
	
	public Point getLineX() {
		Point ret = new Point(0, 0);
		int offs = 0;
		switch (currentLine) {
		case 0: offs = 70; break;
		case 1: offs = 50; break;
		case 2: offs = 52; break;
		case 3: offs = 85; break;
		case 4: offs = 73; break;
		}
		TextLayout layout;
		TextLayout layout2;
		if (pieces2[currentLine].length()>0 && typed.length() <= sentence.length()) {
			try {
				layout = new TextLayout("" + sentence.charAt(typed.length()), f1, new FontRenderContext(null, false, false));
				layout2 = new TextLayout(pieces2[currentLine], f1, new FontRenderContext(null, false, false));
				ret.x = offs + (int)(layout2.getAdvance());
				ret.y = ret.x + (int)layout.getAdvance();
			} catch (Exception e) {
				//Do nothing
			}
		} else {
			ret.x = offs;
			ret.y = ret.x + 35;
		}
		return ret;
	}
	
	public void typed(char c) {
		if (!start) {
			start = true;
		} 
		if (!win && !lose) {
			if (c == 8) {
				if (typed.length() > 0) {
					typed = typed.substring(0, typed.length()-1);
				}
				if (pieces2[currentLine].length() > 0) {
					pieces2[currentLine] = pieces2[currentLine].substring(0, pieces2[currentLine].length()-1);
				} else {
					if (currentLine > 0) {
						currentLine--;
						pieces2[currentLine] = pieces2[currentLine].substring(0, pieces2[currentLine].length()-1);
					}
				}
			} else {
				if (c != sentence.charAt(typed.length())) {
					shake();
				}
				typed = typed + c;
				pieces2[currentLine] = pieces2[currentLine] + c;
				checkLine();
			}
		} else {
			if (c == 10) {
				ShortGame.newGame();
			}
		}
	}
	
	public void shake() {
		shakeTime = 0.1;
	}
	
	public void checkLine() {
		if (pieces2[currentLine].length() == pieces[currentLine].length()+1 || (currentLine==4 && pieces2[currentLine].length()==pieces[currentLine].length())) {
			if (currentLine == 4) {
				if (typed.equals(sentence))
				win();
			} else {
				currentLine++;
			}
		}
	}
	
	public void win() {
		lose = false;
		win = true;
		winTime = time;
		double realWin = winTime;
		for (int i = 0; i < bestTimes.length; i++) {
			if (winTime < bestTimes[i]) {
				double temp = bestTimes[i];
				bestTimes[i] = winTime;
				winTime = temp;
			}
		}
		winTime = realWin;
	}
	public void lose() {
		win = false;
		lose = true;
	}
}
