/* --------------------------------------------------------------------------------
 * 								Button
 * --------------------------------------------------------------------------------
 * 
 * Class object represents a button in the simulation. Logic gates are also
 * represented as buttons.
 * 
 * */

package modules;

import java.awt.Graphics2D;

public abstract class Button {
	
	protected int x = 0, y = 0;				// Location on screen
	protected int width = 0, height = 0;	// Button width and height
	protected boolean pressed;				// Pressed state
	
	/* Constructor
	 * Pre Condition: Receives screen location, width and height.
	 * */
	public Button(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}	// end Constructor
	
	/* Method: getX
	 * Post Condition: Returns buttons x location on screen.
	 * */
	public int getX() {
		return x;
	}	// end getX
	
	/* Method: getY
	 * Post Condition: Returns buttons y location on screen.
	 * */
	public int getY() {
		return y;
	}	// end getY
	
	/* Method: getWidth
	 * Post Condition: Returns button width.
	 * */
	public int getWidth() {
		return width;
	}	// end getWidth
	
	/* Method: getHeight
	 * Post Condition: Returns button height.
	 * */
	public int getHeight() {
		return height;
	}	// end getHeight
	
	// Abstract methods
	public abstract void setPos(int x, int y);
	
	public abstract boolean isPressed();
	public abstract void acknowledge();
	
	public abstract boolean onHover(int x, int y);
	public abstract void onClick(int x, int y);
	public abstract void click();
	
	public abstract void draw(Graphics2D g);
}	// end Button class
