/* --------------------------------------------------------------------------------
 * 								ResetButton
 * --------------------------------------------------------------------------------
 * 
 * Class object represents reset/clear button. This button has a pressed state 
 * that can be retrieved to determine if reset functions are to be executed.
 * 
 * */

package modules;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public class ResetButton extends Button {
	
	private Shape area;				// Area of the button
	private Polygon shape;			// Shape of the bin

	/* Constructor
	 * Pre Condition: Receives location on screen for the button to be
	 * 				  placed. Initiates shapes and pressed state of this
	 * 				  button.
	 * */
	public ResetButton(int x, int y) {
		super(x, y, 40, 40);
		pressed = false;
		setButton();
	}	// end Constructor
	
	/* Method: setButton
	 * Pre Condition: Initiates/sets the the location of the area and the
	 * 				  shape.
	 * */
	private void setButton() {
		// Initiates area
		area = new Ellipse2D.Double(x, y, 40, 40);
		// Initiates shape
		shape = new Polygon();
		shape.addPoint(x + 10, y + 10);
		shape.addPoint(x + width - 10, y + 10);
		shape.addPoint(x + width - 15, y + height - 10);
		shape.addPoint(x + 15, y + height - 10);
	}	// end setButton
	
	/* Method: isPressed
	 * Post Condition: Returns the pressed state of this button.
	 * */
	@Override
	public boolean isPressed() {
		return pressed;
	}	// end isPressed

	@Override
	public void setPos(int x, int y) {}
	
	/* Method: draw
	 * Pre Condition: Receives graphics object used to draw all shapes
	 * 				  related with this button.
	 * */
	@Override
	public void draw(Graphics2D g) {
		g.setColor(new Color(60, 60, 55));
		g.fillOval(x, y, width, height);
		g.setColor(new Color(204, 86, 2));
		g.setStroke(new BasicStroke(3f));
		g.draw(shape);
		g.setStroke(new BasicStroke(1f));
	}	// end draw

	/* Method: onHover
	 * Pre Condition: Receives location on the screen and determines if
	 * 				  this location is within the button area
	 * Post Condition: Returns true if this location is within the area.
	 * */
	@Override
	public boolean onHover(int x, int y) {
		return area.contains(x, y);
	}	// end onHover

	@Override
	public void onClick(int x, int y) {}

	/* Method: click
	 * Pre Condition: Sets the pressed state of this button to true.
	 * */
	@Override
	public void click() {
		pressed = true;
	}	// end click

	/* Method: acknowledge
	 * Pre Condition: Acknowledges button has been pressed, sets
	 * 				  pressed state of this button to false.
	 * */
	@Override
	public void acknowledge() {
		pressed = false;
	}	// end acknowledge
}	// end ResetButton class
