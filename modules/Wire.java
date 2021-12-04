/* --------------------------------------------------------------------------------
 * 								Wire
 * --------------------------------------------------------------------------------
 * 
 * Class object represents an wire, used to connect logic gates and components on 
 * the circuit board. Wires contain two nodes, and will possess the charge of the 
 * node that recently changed.
 * 
 * */

package modules;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Wire {
	// Nodes
	private Node endNode1;
	private Node endNode2;
	// Previous charge states of each node
	private boolean en1PrevCharge = false;
	private boolean en2PrevCharge = false;
	// Attached state
	private boolean isLooseWire = false;
	
	/* Constructor
	 * Pre Condition: Receives reference to the first node and the 
	 * 				  location of the second node.
	 * */
	public Wire(Node n1, int x2, int y2) {
		endNode1 = n1;
		endNode2 = new Node(x2, y2, 4);
		// Get current charge state of the first node
		en1PrevCharge = n1.getCharge();
		en2PrevCharge = n1.getCharge();
		endNode2.setCharge(endNode1.getCharge());
	}	// end Constructor

	/* Constructor
	 * Pre Condition: Receives the locations of both nodes.
	 * */
	public Wire(int x1, int y1, int x2, int y2) {
		endNode1 = new Node(x1, y1, 4);
		endNode2 = new Node(x2, y2, 4);
		en1PrevCharge = endNode1.getCharge();
		en2PrevCharge = endNode2.getCharge();
	}	// end Constructor
	
	/* Method: moveEndPoint
	 * Pre Condition: Receives new location on screen for the second 
	 * 				  node. Updates the location of the node.
	 * */
	public void moveEndPoint(int x, int y) {
		endNode2 = new Node(x - 8, y - 8, 4);
		endNode2.setCharge(en2PrevCharge);
	}	// end moveEndPoint
	
	/* Method: moveEndPoint
	 * Pre Condition: Receives reference of node. Sets the second
	 * 				  to equal the referenced node.
	 * */
	public void moveEndPoint(Node n) {
		endNode2 = n;
		endNode2.setCharge(en2PrevCharge);
	}	// end moveEndPoint
	
	/* Method: getRefNode
	 * Pre Condition: Receives location from the screen. Checks if the
	 * 				  location is contained in any of the nodes.
	 * Post Condition: Returns reference to the node containing this 
	 * 				   location, otherwise return null.
	 * */
	public Node getRefNode(int x, int y) {
		// If node 1 contains location
		if(endNode1.onHover(x, y))
			return endNode1;
		// If node 2 contains location
		else if(endNode2.onHover(x, y))
			return endNode2;
		else return null;
	}	// end getRefNode
	
	/* Method: detach
	 * Pre Condition: Receives location from the screen, checks if any
	 * 				  node contains this point. If yes, detach that node
	 * 				  and this wire.
	 * */
	public void detach(int x, int y) {
		// If node 1 contains location
		if(endNode1.onHover(x, y)) {
			endNode1.detach();
			isLooseWire = true;
		// If node 2 contains location
		} else if(endNode2.onHover(x, y)) {
			endNode2.detach();
			isLooseWire = true;
		}
	}	// end detach
	
	/* Method: isLoose
	 * Post Condition: If this wire is detached, return true.
	 * */
	public boolean isLoose() {
		return isLooseWire;
	}	// end isLoose
	
	/* Method: update
	 * Pre Condition: Check the state of each node and whether a charge
	 * 				  of either node has changed.
	 * 				  If a node has changed charge, change the charge of
	 * 				  the wire and the other node.
	 * */
	public void update() {
		looseWireCheck();
		if(!isLooseWire) {
			int value = endNode1.getCharge() ? 1 : 0;
			value += endNode2.getCharge() ? 1 : 0;
			value += en1PrevCharge ? 1 : 0;
			value += en2PrevCharge ? 1 : 0;
			// If there is a change in charge
			if(value != 4 || value != 0) {
				// If node 1 changed
				if(endNode1.getCharge() != en1PrevCharge) {
					endNode2.setCharge(endNode1.getCharge());
					en1PrevCharge = endNode1.getCharge();
					en2PrevCharge = endNode1.getCharge();
				// If node 2 changed
				} else if(endNode2.getCharge() != en2PrevCharge) {
					endNode1.setCharge(endNode2.getCharge());
					en1PrevCharge = endNode2.getCharge();
					en2PrevCharge = endNode2.getCharge();
				}
			}
		}
	}	// end update
	
	/* Method: looseWireCheck
	 * Pre Condition: Checks if either node is detached, updates the
	 * 				  wire's attached state if either are detached.
	 * */
	private void looseWireCheck() {
		// If node 1 or node 2 are detached
		if(endNode1.isDetached() || endNode2.isDetached())
			isLooseWire = true;
	}	// end looseWireCheck
	
	/* Method: onHover
	 * Pre Condition: Receives location from the screen. Checks if 
	 * 				  either node contain the location
	 * Post Condition: Returns true if node 1 or node 2 contain the
	 * 				   specified location.
	 * */
	public boolean onHover(int x, int y) {
		return endNode1.onHover(x, y) ? 
				true : endNode2.onHover(x, y) ? 
						true : false;
	}	// end onHover
	
	public void onClick(int x, int y) {}
	
	/* Method: draw
	 * Pre Condition: Receives graphics object used to draw all shapes
	 * 				  and the nodes related with this wire.
	 * */
	public void draw(Graphics2D g) {
		// Draw nodes
		endNode1.draw(g);
		endNode2.draw(g);
		// If wire is positive charged
		if(en1PrevCharge)
			g.setColor(new Color(204, 86, 2));
		// If wire is negative charged
		else 
			g.setColor(new Color(10, 10, 5));
		
		// Draw wire line
		g.setStroke(new BasicStroke(3f));
		g.drawLine(endNode1.getPos()[0] + 8, endNode1.getPos()[1] + 8, endNode2.getPos()[0] + 8, endNode2.getPos()[1] + 8);
		g.setStroke(new BasicStroke(1f));
	}	// end draw
}	// end Wire class
