/* --------------------------------------------------------------------------------
 * 								NOTGate
 * --------------------------------------------------------------------------------
 * 
 * Class object represents a NOT gate, used in simulations. This gate behaves like
 * a gate, consisting of one input node and one output node. 
 * 
 * */

package modules;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class NOTGate extends Gate {
	
	private Rectangle2D area;		// Gate area
	private Polygon triangle;		// Gate triangle shape
	private Ellipse2D circle;		// Gate circle shape
	
	private Node n1;				// Input node
	private Node n2;				// Output node

	/* Constructor
	 * Pre Condition: Use default location and initiates shape objects
	 * 				  and nodes.
	 * */
	public NOTGate() {
		super(200, 200, 60, 30);
		setShapes();
	}	// end Constructor

	/* Constructor
	 * Pre Condition: Uses provided location and initiates shape objects
	 * 				  and nodes.
	 * */
	public NOTGate(int x, int y) {
		super(x, y, 60, 30);
		setShapes();
	}	// end NOTGate
	
	/* Method: setShapes
	 * Pre Condition: Initiates all shapes and nodes for this gate object.
	 * */
	private void setShapes() {
		// Initaite rectangle area
		area = new Rectangle2D.Double(x, y, 60, 30);
		// Initiate circle shape
		circle = new Ellipse2D.Double(x + 37, y + 10, 10, 10);
		// Initiate triangle shape
		triangle = new Polygon();
		triangle.addPoint(x + 22, y);
		triangle.addPoint(x + 37, y + 15);
		triangle.addPoint(x + 22, y + 30);
		// Initiate nodes
		n1 = new Node(x - 5, y + 7, Node.LEFT);
		n2 = new Node(x + 50, y + 7, Node.RIGHT);
	}
	
	/* Method: setPos
	 * Pre Condition: Receives x and y for the new gate position.
	 * */
	@Override
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
		setShapes();
	}	// end setPos
	
	/* Method: onHover
	 * Pre Condition: Receives x and y of current screen
	 * 				  location. Performs hovering processes.
	 * Post Condition: Returns true if the mouse is hovering over this
	 * 				   gate.
	 * */
	@Override
	public boolean onHover(int x, int y) {
		// If gate is activated
		if(isGateActive()) {
			boolean flag = false;
			// If location is hovering over the input node
			if(n1.onHover(x, y))
				flag = true;
			if(n2.onHover(x, y))
				flag = true;
			return flag;
		}
		// If this gate is not activated
		else if(!isGateActive()) {
			return area.contains(x, y);
		}
		return false;
	}	// end onHover
	
	/* Method: onGateHover
	 * Pre Condition: Receives location on screen, checks if this location
	 * 				  hovers over the triangle.
	 * Post Condition: Returns true if this location hovers over the triangle.
	 * */
	@Override
	public boolean onGateHover(int x, int y) {
		return triangle.contains(x, y);
	}	// end onGateHover

	@Override
	public void onClick(int x, int y) {}

	@Override
	public void click() {}
	
	@Override
	public boolean isPressed() { return false; }

	@Override
	public void acknowledge() {}
	
	/* Method: getRefNode
	 * Pre Condition: Receives screen location (x, y) and determines
	 * 				  if one of the 2 nodes contains this point.
	 * Post Condition: Returns the node that contains this point, otherwise
	 * 				   returns null.
	 * */
	@Override
	public Node getRefNode(int x, int y) {
		// If gate is active
		if(isGateActive()) {
			// If input node contains the location
			if(n1.onHover(x, y))
				return n1;
			// If output node contains the location
			if(n2.onHover(x, y))
				return n2;
		}
		return null;
	}	// end getRefNode
	
	/* Method: update
	 * Pre Condition: Attaches any node that has been detached during
	 * 				  the removal of a wire. Checks the value of the
	 * 				  two input nodes and determines the output value.
	 * */
	@Override
	public void update() {
		// If gate is still attached 
		if(!isDetached) {
			// If input node is detached
			if(n1.isDetached())
				n1.attach();
			// If output node is detached
			if(n2.isDetached())
				n2.attach();
		}
		
		// If input node is positive charged
		if(n1.getCharge()) {
			n2.setCharge(false);
		// If input node is negative charged
		} else {
			n2.setCharge(true);
		}
	}	// end update
	
	/* Method: draw
	 * Pre Condition: Receives graphics object used to draw all shapes
	 * 				  and the nodes related with this gate.
	 * */
	@Override
	public void draw(Graphics2D g) {
		// Draw NOT gate shapes
		g.setColor(new Color(204, 86, 2));
		g.fill(triangle);
		g.draw(circle);
		// Draw NOT gate node sticks
		g.drawLine(x + 47, y + 15, x + 52, y + 15);
		g.drawLine(x + 8, y + 15, x + 30, y + 15);
		// Draw nodes
		n1.draw(g);
		n2.draw(g);
	}	// end draw
	
	/* Method: detach
	 * Pre Condition: Sets this gate to be detached. Detaches all 
	 * 				  of this gate's nodes.
	 * */
	@Override
	public void detach() {
		isDetached = true;
		n1.detach();
		n2.detach();
	}	// end detach
}	// end NOTGate class
