/*
 * Class Description: Class is for defining the main Game object, its logic, the rules of the game, and ensure if the user
 * was able to satisfy the win or loss conditions
 */
package com;

import java.awt.Color;				// import the color library to add color to the gaming window
import java.awt.Font;				// import the font library to set the fonts for the game output
import java.awt.Graphics;			// graphics libraries to allow for the animation
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;		// import geometry libraries for properly drawing images on canvas
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;		
import java.text.DecimalFormat;

public class Game {
	String sentence = "Can you type this extremely long sentence in only just ten seconds";		// sentence to be typed by user
	String typed = "";				// variable to hold the user's typed input
	double time = 0;				// time taken by the user
	double xShake = 0;				// x-axis variable to shake the window when user types in the wrong character
	double yShake = 0;				// y-axis variable to shake the window when user types in the wrong character
	boolean shaking = false;			// variable to indicate screen shake
	double shakeTime = 0;				// variable to indicate how long the screen has been shaking for
	boolean start = false;
	int currentLine = 0;				// indicates which line of the sentence the user is on
	String[] pieces = new String[5];		// variable to hold the original pieces of the sentence
	String[] pieces2 = new String[5];		// valriable to hold the user's input lines
	int highestInt = 1;				
	double bloatTime = 0;				// variable to indicate how long the timer will hold the bloated effect
	float bloat = 35;
	boolean win, lose;				// variables to indicate the game result
	double finishTime = 0;				// variable to indicate the time taken by the user to complete the objective
	double rotate = 0;				// variables for the rotaion and scaling of the result otput screen
	double scale = 0;
	double winTime;					// time take by the user to win the game
	double[] bestTimes = new double[3];		// variable to hold the best times within which the objective was achieved
	
	/*
	 * Method Description: Constructor Method defines the different pieces of the sentence that must be typed by the user
	 * and intializes the empty strings which would be filled with the user input
	 * @param best recives as input the previously scored best times from the computer
	*/
	public Game(double[] best) {
		pieces[0] = "Can you type";			// each piece is intialized to its respective part ofthe sentence
		pieces[1] = "this extremely";
		pieces[2] = "long sentence";
		pieces[3] = "in only just";
		pieces[4] = "ten seconds";
		for (int i = 0; i < pieces2.length; i++) {	// loop to initialize the pieices of the sentence to be entered by user
			pieces2[i] = "";
		}
		bestTimes = best;		// sets best game completion times as recieved from from previous attempts
	}
	/*
	 * Method Description: Method for updating game logic. This includes elapsed time, player win/loss, screen shakes
	 * @param delta is the elapsed time since the last game update was made
	 */
	public void update(double delta) {
		if (start && !lose && !win) {			// when the game has started and in progress perform the following actions 
			time += delta;				// increase the elapsed time as recived from parameter
			
			if (time > highestInt) {		// when the time exceeds 1.00 for every second apply a bloat effect to the
				bloatTime = 0.2;		//timer
				highestInt++;
			}
			if (time >= 10) {			// when time exceeds 10 seconds call the lose method to indicate a loss
				lose();
			}
			if (shakeTime > 0) {			// when there is an indicated ShakeTime that is greater than 0
				shakeTime -= delta;		// subtract the shake time from the variable and implement the screen shake
				xShake = Math.random()-.5;
				yShake = Math.random()-.5;
				xShake *= 3;
				yShake *= 3;
			}
			if (bloatTime > 0) {				// when there is an indicated BloatTime that is greater than 0
				bloat = (float)(bloatTime/0.2*15)+35;	//implement the bloating of the countdown clock
				bloatTime -= delta;
			}
		}
		else if (win || lose) {					// if either a win or loss occurs added the latest elapsed time to
			finishTime+=delta;				// the final time and rotate in the ending sequence
			if (rotate < .5) {
				rotate += delta;
				scale += delta * 4;
			}
		}
	}
	
	DecimalFormat format = new DecimalFormat("0.00"); 			// declare variable for numeric formatting
	Font f1 = new Font("Rockwell Extra Bold", Font.BOLD, 50);		// declare fonts to be used for the output text
	Font f2 = new Font("Rockwell Extra Bold", Font.BOLD, 35);
	
	/*
	 * Method Description: Method for rendering the screens shakes, string outputs, user inputs and images to be displayed
	 *@param g Graphics object on which the redering will be performed
	 */
	public void render(Graphics2D g) {	
		if (!win && !lose) {					// when the game is in progress displays the sentence to be typed
									// and the input being typed by the user
			// following block sets the co-ordinate location for the sentence pieces to be displayed, their shake
			// values, font properties and draws them on the object
			int yPos = 150;								
			g.setFont(f1);
			g.setColor(Color.getColor("", 0x006605));
			g.drawString(pieces[0], 70+(int)xShake, yPos + 0 * 50+(int)yShake);
			g.drawString(pieces[1], 50+(int)xShake, yPos + 1 * 50+(int)yShake);
			g.drawString(pieces[2], 52+(int)xShake, yPos + 2 * 50+(int)yShake);
			g.drawString(pieces[3], 85+(int)xShake, yPos + 3 * 50+(int)yShake);
			g.drawString(pieces[4], 73+(int)xShake, yPos + 4 * 50+(int)yShake);
			
			//following block sets the co-ordinate location for the sentence pieces being entered in by the user and being
			// displayed on the screen, their shake values and font properties.
			g.setColor(Color.getColor("", 0x2764ff));
			g.drawString(pieces2[0], 70 + (int)xShake, yPos + 0 * 50+(int)yShake);
			g.drawString(pieces2[1], 50 + (int)xShake, yPos + 1 * 50+(int)yShake);
			g.drawString(pieces2[2], 52 + (int)xShake, yPos + 2 * 50+(int)yShake);
			g.drawString(pieces2[3], 85 + (int)xShake, yPos + 3 * 50+(int)yShake);
			g.drawString(pieces2[4], 73 + (int)xShake, yPos + 4 * 50+(int)yShake);
			
			g.setFont(f2.deriveFont(bloat));			// defines the bloating font for the timer
			g.setColor(Color.red);
			float x, y;					
			x = 230 + (1-(bloat/35))*50;		// sets the co-ordinate values for the bloated timer on the screen
			y = 70 - (1-(bloat/35))*15;
			g.drawString(format.format(time), x, y);
			
			g.setColor(Color.blue);				// set the fonts for the end game output corresponding to win or loss
			Point p = getLineX();
			g.drawLine(p.x, yPos+currentLine*50, p.y, yPos+currentLine*50); // draws the underline to follow the user input
		} else {
			Rectangle2D rec = null;
			if (win) {					// sets YES or NO to the for the rectangle value depending on if the user wins or looses the game
				rec = f1.getStringBounds("YES", new FontRenderContext(null, false, false));
			} else if (lose) {				
				rec = f1.getStringBounds("NO", new FontRenderContext(null, false, false));
			}
			BufferedImage image = new BufferedImage((int)rec.getWidth(), (int)rec.getHeight(), BufferedImage.TYPE_INT_ARGB); //sets up the image in a buffer before being displayed on the screen
			Graphics gg = image.getGraphics();		
			gg.setFont(f1);						//sets the font properties for the YES or NO display
			float a = (float)scale/2.0f;			
			if (a > 1) a = 1;
			gg.setColor(new Color(1, 0, 0, a));
			if (win)						// draws the YES or NO onto the object 
				gg.drawString("YES", 0, 58);
			if (lose) {
				gg.drawString("NO", 0, 58);
			}
			// The following block displays the final game result onto the screen along with the prompt to play again
			AffineTransform at = new AffineTransform();
			at.translate(300, 190);
			at.rotate(-rotate, 40, 40);					// sets rotations and translations of game result on screen
			at.scale(scale, scale);
			at.translate(-image.getWidth()/2, -image.getHeight()/2);
			g.drawImage(image, at, null);
			g.setFont(new Font("Helvetica", Font.BOLD, 30));		// sets font properties of the Try Again Prompt and draws it onto the object
			g.setColor(Color.red);
			g.drawString("Try Again (Enter)", 180, 420);
			if (win) {							// given that the user wins the win time is drawn onto the object
				g.drawString("Time: " + format.format(winTime) + "s", 80, 50);
			}
			// this block sets font properties for and draws onto the object the best game completion times provided by the system
			g.setFont(new Font("Helvetica", Font.BOLD, 12));		
			g.drawString("Best Times:", 400, 30);
			g.drawString("(1) " + format.format(bestTimes[0]), 420, 60);
			g.drawString("(2) " + format.format(bestTimes[1]), 420, 80);
			g.drawString("(3) " + format.format(bestTimes[2]), 420, 100);
		}
	}
	
	/*
	* Method Description: This method is for creating the cursor underline that follows th user input to indicate where the 
	* the user's position is.
	*/
	public Point getLineX() {
		Point ret = new Point(0, 0);
		int offs = 0;
		switch (currentLine) {			// sets the offsets depending on which line of the sentence it is.
		case 0: offs = 70; break;
		case 1: offs = 50; break;
		case 2: offs = 52; break;
		case 3: offs = 85; break;
		case 4: offs = 73; break;
		}
		TextLayout layout;
		TextLayout layout2;
		// given that the user has started typing on a line and not yet completed the line, the following if structure will continue
		// to render the typed letters
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
	/*
	 * Method Description: sets the logic of where the player is (ie. what line of the sentence) once the game has begun
	 * @param c variable to hold the lastest entered character by the user
	*/
	public void typed(char c) {
		if (!start) {				// if the game has not already started, then it starts the game with this being
			start = true;			// the first character input
		} 
		if (!win && !lose) {			// while the game is in progress
			if (c == 8) {			// when the backspace character is entered
				if (typed.length() > 0) {		// removes a character from the typed letters if there are any to begin with
					typed = typed.substring(0, typed.length()-1);
				}
				if (pieces2[currentLine].length() > 0) {  // removes characters from the typed onces 
					pieces2[currentLine] = pieces2[currentLine].substring(0, pieces2[currentLine].length()-1);
				} else {				// if not on the first line then allows to traverse back to previous lines
					if (currentLine > 0) {
						currentLine--;
						pieces2[currentLine] = pieces2[currentLine].substring(0, pieces2[currentLine].length()-1);
					}
				}
			} else {			
				if (c != sentence.charAt(typed.length())) {	// calls the screen shaker if a wrong character is typed
					shake();
				}
				typed = typed + c;
				pieces2[currentLine] = pieces2[currentLine] + c;
				checkLine();					// calls checkLine method to check if the correct inputs are being entered
			}
		} else {				// when a win or a loss has occured		
			if (c == 10) {			// if enter is pressed then proceed to create a new game
				ShortGame.newGame();
			}
		}
	}
	/*
	* Method Description: Method to set the shake time of the screen when a user enters the wrong characters
	*/
	public void shake() {				
		shakeTime = 0.1;		
	}
	/*
	 * Method Description: Method for cheking of the user has achieved the goal of the game and typed the sentence correctly
	*/
	public void checkLine() {
		//Conditional block to check that the user input matches the preset sentence to be ty[ed
		if (pieces2[currentLine].length() == pieces[currentLine].length()+1 || (currentLine==4 && pieces2[currentLine].length()==pieces[currentLine].length())) {
			if (currentLine == 4) {			// if the user is currently on the last line
				if (typed.equals(sentence))
				win();			// if the match occurs then game status is set to win
			} else {			// if not then the game moves onto the next line to be typed
				currentLine++;
			}
		}
	}
	/*
	* Method Description: Method to set the user's win status once the object has been acheived
	*/
	public void win() {
		lose = false;			// sets the appriate win and loose conditions and the time score
		win = true;
		winTime = time;
		double realWin = winTime;
		// following contional loops check if the user has set a new best time and if so then adds the user's time to the best times
		for (int i = 0; i < bestTimes.length; i++) {
			if (winTime < bestTimes[i]) {
				double temp = bestTimes[i];
				bestTimes[i] = winTime;
				winTime = temp;
			}
		}
		winTime = realWin;
	}
	/*
	* Method Description: Method to set the user's loss status to ture given that the time expired and the game objective was failed
	*/
	public void lose() {
		win = false;
		lose = true;
	}
}
