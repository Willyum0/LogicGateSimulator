/* --------------------------------------------------------------------------------
 * 								Node
 * --------------------------------------------------------------------------------
 * 
 * Class object represents a point in the circuit board that contains connectivity
 * for wires to connect to other wires or to a logic gate.
 * The node will possess the charge state of the wires and logic gates. This 
 * determines whether a "positive voltage" is traveling through a specific wire. 
 * 
 * */

package modules;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

public class Node {
	
	private final int ARC_RADIUS = 10;		// Radius of node area
	private boolean isHovering = false;		// Is hovering state
	private int dir;						// Direction node is facing
	// Direction constants
	public static final int NONE = -1;
	public static final int RIGHT = 0;
	public static final int UP = 1;
	public static final int LEFT = 2;
	public static final int DOWN = 3;
	public static final int FULL = 4;
	
	private Arc2D arc;						// Arc shape for nodes with directions
	private Ellipse2D area;					// Node area
	
	private boolean p_Charged = false;		// Charge state of the node
	private boolean isDetached = false;		// Detached from the board state 
											// (to be removed)
	/* Constructor
	 * Pre Condition: Receives location on screen to place the node. Initiates
	 * 				  shapes of of the node.
	 * */
	public Node(int x, int y) {
		init(x, y, NONE);
	}	// end Constructor

	/* Constructor
	 * Pre Condition: Receives location on screen to place the node and a facing 
	 * 				  direction. Initiates shapes of of the node.
	 * */
	public Node(int x, int y, int direction) {
		init(x, y, direction);
		dir = direction;
	}	// end Constructor
	
	/* Method: init
	 * Pre Condition: Receives location on screen and direction. Sets the arc
	 * 				  shape to face in the specified direction so the open side
	 * 				  of the arc points in said direction.
	 * */
	public void init(int x, int y, int direction) {
		// Initiate area
		area = new Ellipse2D.Double(x, y, 16, 16);
		switch(direction) {
		// If direction is right
		case 0:
			arc = new Arc2D.Double(x + 3, y + 3, ARC_RADIUS, ARC_RADIUS, 90, 180, Arc2D.OPEN);
			break;
		// If direction is up
		case 1:
			arc = new Arc2D.Double(x + 3, y + 3, ARC_RADIUS, ARC_RADIUS, 180, 180, Arc2D.OPEN);
			break;
		// If direction is left
		case 2:
			arc = new Arc2D.Double(x + 3, y + 3, ARC_RADIUS, ARC_RADIUS, 270, 180, Arc2D.OPEN);
			break;
		// If direction is down
		case 3:
			arc = new Arc2D.Double(x + 3, y + 3, ARC_RADIUS, ARC_RADIUS, 0, 180, Arc2D.OPEN);
			break;
		// If arc is drawn with no direction (full arc)
		case 4:
			arc = new Arc2D.Double(x + 3, y + 3, ARC_RADIUS, ARC_RADIUS, 0, 360, Arc2D.OPEN);
			break;
		default:
			arc = null;
		}
	}	// end init
	
	/* Method: setPos
	 * Pre Condition: Receives location on the screen. Sets the node at this point.
	 * */
	public void setPos(int x, int y) {
		init(x, y, dir);
	}	// end setPos
	
	/* Method: getPos
	 * Post Condition: Returns the location of this node on the screen.
	 * */
	public int[] getPos() {
		int[] pos = new int[2];
		pos[0] = (int) area.getX();
		pos[1] = (int) area.getY();
		return pos;
	}	// end getPos
	
	/* Method: setCharge
	 * Pre Condition: Sets the charge of this node.
	 * */
	public void setCharge(boolean c) {
		p_Charged = c;
	}	// end setCharge
	
	/* Method: getCharge
	 * Post Condition: Returns the charge value of this node.
	 * */
	public boolean getCharge() {
		return p_Charged;
	}	// end getCharge
	
	/* Method: attach
	 * Pre Condition: Attaches this node.
	 * */
	public void attach() {
		isDetached = false;
	}	// end attach
	
	/* Method: detach
	 * Pre Condition: Detaches this node.
	 * */
	public void detach() {
		isDetached = true;
	}	// end detach
	
	/* Method: isDetached
	 * Post Condition: Returns true if the node is detached
	 * */
	public boolean isDetached() {
		return isDetached;
	}	// end isDetached
	
	/* Method: onHover
	 * Pre Condition: Receives a location on the screen. Checks if the location
	 * 				  is in the node area.
	 * Post Condition: Returns true if the location is in the area.
	 * */
	public boolean onHover(int x, int y) {
		isHovering = area.contains(x, y);
		return isHovering;
	}	// end onHover

	/* Method: draw
	 * Pre Condition: Receives graphics object used to draw all shapes
	 * 				  related with the node.
	 * */
	public void draw(Graphics2D g) {
		// If node is being hovered over
		if(isHovering) {
			g.setColor(new Color(0, 200, 0, 100));
			g.fill(area);
		}
		// If an arc is to be drawn
		if(arc != null) {
			// If the charge of this node is positive
			if(p_Charged) {
				g.setColor(new Color(204, 86, 2));
			// If the charge of this node is negative
			} else {
				g.setColor(new Color(10, 10, 8));
			}
			g.setStroke(new BasicStroke(4f));
			g.draw(arc);
			g.setStroke(new BasicStroke(1f));
		}
	}	// end draw
}	// end Node class
