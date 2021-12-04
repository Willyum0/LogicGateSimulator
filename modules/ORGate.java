/* --------------------------------------------------------------------------------
 * 								ORGate
 * --------------------------------------------------------------------------------
 * 
 * Class object represents an OR gate, used in simulations. This gate behaves like
 * a gate, consisting of two input nodes and one output node. 
 * 
 * */

package modules;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;

public class ORGate extends Gate {

	private Rectangle2D area;	// Gate area
	private Polygon gate;		// Gate shape
	
	private Node out;			// Output node
	private Node in1;			// Input node 1
	private Node in2;			// Input node 2

	/* Constructor
	 * Pre Condition: Uses default location and initiates shape objects 
	 * 				  and nodes.
	 * */
	public ORGate() {
		super(200, 200, 90, 60);
		setShapes();
	}	// end Constructor
	
	/* Constructor
	 * Pre Condition: Uses provided location and initiates shape objects 
	 * 				  and nodes.
	 * */
	public ORGate(int x, int y) {
		super(x, y, 90, 60);
		setShapes();
	}	// end Constructor
	
	/* Method: setShapes
	 * Pre Condition: Initiates all shapes and nodes for this gate object.
	 * */
	private void setShapes() {
		// Initaite rectangle area
		area = new Rectangle2D.Double(x, y, 90, 60);
		// Initiate gate shape
		gate = new Polygon();
		gate.addPoint(x + 2 + 15, y + 2);
		gate.addPoint(x + 20 + 15, y + 2);
		gate.addPoint(x + 44 + 15, y + 10);
		gate.addPoint(x + 52 + 15, y + 20);
		gate.addPoint(x + 56 + 15, y + 29);
		gate.addPoint(x + 52 + 15, y + 38);
		gate.addPoint(x + 44 + 15, y + 48);
		gate.addPoint(x + 20 + 15, y + 58);
		gate.addPoint(x + 2 + 15, y + 58);
		gate.addPoint(x + 9 + 15, y + 40);
		gate.addPoint(x + 10 + 15, y + 29);
		gate.addPoint(x + 9 + 15, y + 18);
		// Initiate nodes
		out = new Node(x + 82, y + 22, Node.RIGHT);
		in1 = new Node(x - 10, y + 8, Node.LEFT);
		in2 = new Node(x - 10, y + 37, Node.LEFT);
	}	// end setShapes
	
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
		// If this gate is activated
		if(isGateActive()) {
			boolean flag = false;
			// If mouse is hovering over the out node
			if(out.onHover(x, y))
				flag = true;
			// If mouse is hovering over the input 1 node
			if(in1.onHover(x, y))
				flag = true;
			// If mouse is hovering over the input 2 node
			if(in2.onHover(x, y))
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
	 * Pre Condition: Determines if the specified location (x, y) is 
	 * 				  contained in the polygon shape.
	 * Post Condition: Returns true if this point exists in the polygon.
	 * */
	@Override
	public boolean onGateHover(int x, int y) {
		return gate.contains(x, y);
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
	 * 				  if one of the 3 nodes contains this point.
	 * Post Condition: Returns the node that contains this point, otherwise
	 * 				   returns null.
	 * */
	@Override
	public Node getRefNode(int x, int y) {
		// If gate is active
		if(isGateActive()) {
			// If output node contains the point
			if(out.onHover(x, y))
				return out;
			// If input 1 node contains the point
			if(in1.onHover(x, y))
				return in1;
			// If input 2 node contains the point
			if(in2.onHover(x, y))
				return in2;
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
			// If output node is detached
			if(out.isDetached())
				out.attach();
			// If input 1 node is detached
			if(in1.isDetached())
				in1.attach();
			// If input 2 Node is detached
			if(in2.isDetached())
				in2.attach();
		}
		
		// If input 1 node or input 2 node are positive chargeds
		if(in1.getCharge() || in2.getCharge()) {
			out.setCharge(true);
		} else {
			out.setCharge(false);
		}
	}	// end update
	
	/* Method: draw
	 * Pre Condition: Receives graphics object used to draw all shapes
	 * 				  and the nodes related with this gate.
	 * */
	@Override
	public void draw(Graphics2D g) {
		// Draw OR gate shape
		g.setColor(new Color(204, 86, 2));
		g.fill(gate);
		
		// Draw AND gate node sticks
		g.drawLine(x + 5, y + 16, x + 20, y + 16);
		g.drawLine(x + 5, y + 45, x + 20, y + 45);
		g.drawLine(x + 70, y + 29, x + 85, y + 29);
		
		// Draw nodes
		out.draw(g);
		in1.draw(g);
		in2.draw(g);
	}	// end draw

	/* Method: detach
	 * Pre Condition: Sets this gate to be detached. Detaches all 
	 * 				  of this gate's nodes.
	 * */
	@Override
	public void detach() {
		isDetached = true;
		out.detach();
		in1.detach();
		in2.detach();
	}	// end detach
}	// end ORGate class
