/* --------------------------------------------------------------------------------
 * 								DisplayBar
 * --------------------------------------------------------------------------------
 * 
 * Class object represents a side (display) bar, consisting of all the gates and 
 * components available to design circuits with.
 * 
 * */

package modules;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class DisplayBar {
	
	private final int BUTTON_MARGIN = 20;			// Distance between each button element
	
	private int x = 0, y = 0;						// Display bar location
	private int width = 0, height = 0;				// Display bar width and height
	private int heightIndex = 100;					// Starting location of the buttons list
	
	private ArrayList<Button> buttons;				// List of buttons
	private int refButton;							// Referenced button
	
	/* Constructor
	 * Pre Condition: Receives display bar's location on screen, width and height.
	 * 				  Initiates list of buttons.
	 * */
	public DisplayBar(int xLoc, int yLoc, int width, int height) {
		this.x = xLoc;
		this.y = yLoc;
		this.width = width;
		this.height = height;
		
		buttons = new ArrayList<>();
	}	// end Constructor
	
	/* Method: addButton
	 * Pre Condition: Adds new button to the list of buttons.
	 * */
	public void addButton(Button newButton) {
		// Set Button's position in the display bar
		newButton.setPos(x + 40, y + heightIndex);
		// Increment height index so next button will appear below this new one.
		heightIndex += newButton.getHeight() + BUTTON_MARGIN;
		// Add to list
		buttons.add(newButton);
	}	// end addButton
	
	/* Method: isHovering
	 * Pre Condition: Receives location on screen.
	 * Post Condition: If location is hovering over a button, returns true.
	 * */
	public boolean isHovering(int x, int y) {
		// For each button
		for(int i = 0; i < buttons.size(); i++) {
			// If the location is over a button
			if(buttons.get(i).onHover(x, y)) {
				refButton = i;		// Identify referenced button
				return true;
			}
		}
		refButton = -1;
		return false;
	}	// end isHovering
	
	/* Method: getReferencedGate
	 * Post Condition: Returns new instance of the referenced button from the list.
	 * */
	public Gate getReferencedGate() {
		// If button referenced is the AND gate
		if(buttons.get(refButton) instanceof ANDGate) {
			return new ANDGate();
		// If button referenced is the OR gate
		} else if(buttons.get(refButton) instanceof ORGate) {
			return new ORGate();
		// If button referenced is the Power source
		} else if(buttons.get(refButton) instanceof PSource) {
			return new PSource();
		// If button referenced is the NOT gate
		} else if(buttons.get(refButton) instanceof NOTGate) {
			return new NOTGate();
		}
		else return null;
	}	// end getReferencedGate

	/* Method: draw
	 * Pre Condition: Receives graphics object, draws all shapes in relation
	 * 				  to this display bar.
	 * */
	public void draw(Graphics2D g) {
		// Set background
		g.setColor(new Color(70, 70, 65));
		g.fillRect(x, y, width, height);
		// If buttons list is not empty
		if(!buttons.isEmpty()) {
			buttons.forEach((n) -> n.draw(g));
		}
	}	// end drawDisplayBar
}	// end DisplayBar class
