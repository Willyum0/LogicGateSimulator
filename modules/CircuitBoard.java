/* --------------------------------------------------------------------------------
 * 								CircuitBoard
 * --------------------------------------------------------------------------------
 * 
 * Class object CircuitBoard represents the simulation board, containing all gates
 * wires used to make a circuit. 
 * 
 * */

package modules;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class CircuitBoard {
	
	private Rectangle2D area;			// Circuit board area
	private ArrayList<Gate> gates;		// List of gates
	private ArrayList<Wire> wires;		// List of wires
	private Wire refWire;				// Reference wire
	private Node refNode;				// Reference node

	/* Constructor
	 * Pre Condition: Receives the screen location, width and height. initiates
	 * 				  all shapes associated with the circuit board
	 * */
	public CircuitBoard(int x, int y, int width, int height) {
		area = new Rectangle2D.Double(x, y, width, height);
		gates = new ArrayList<>();
		wires = new ArrayList<>();
		refWire = null;
		refNode = null;
	}	// end Constructor
	
	/* Method: update
	 * Pre Condition: Updates all gates and wires present on the circuit board.
	 * */
	public void update() {
		int index = 0;
		// For each Gate
		for(int i = 0; i < gates.size() - index; i++) {
			// If this gate is detached
			if(gates.get(i).isDetached()) {
				gates.remove(i);
				i--;
				index++;
			// Else, update gate
			} else {
				gates.get(i).update();
			}
		}
		index = 0;
		// For each wire
		for(int i = 0; i < wires.size() - index; i++) {
			// If this wire is detached
			if(wires.get(i).isLoose()) {
				wires.remove(i);
				i--;
				index++;
			// Else, update wire
			} else {
				wires.get(i).update();
			}
		}
	}	// end update
	
	/* Method: clear
	 * Pre Condition: Clears all gates and wires from the circuit board.
	 * */
	public void clear() {
		gates.clear();
		wires.clear();
		refWire = null;
		refNode = null;
	}	// end clear
	
	/* Method: draw
	 * Pre Condition: Draws all gates and wires on the circuit board.
	 * */
	public void draw(Graphics2D g) {
		// Set background
		g.setColor(new Color(40, 40, 35));
		g.fill(area);
		// If the gates list is not empty
		if(!gates.isEmpty()) {
			// For each gate
			for(int i = 0; i < gates.size(); i++) {
				gates.get(i).draw(g);
			}
		}
		// If the wires list is not empty
		if(!wires.isEmpty()) {
			// For each wire
			for(int i = 0; i < wires.size(); i++) {
				wires.get(i).draw(g);
			}
		}
		// If there is a reference wire
		if(refWire != null) {
			refWire.draw(g);
		}
	}	// end draw
	
	/* Method: addNewGate
	 * Pre Condition: Receives a gate, activates the gate and adds this 
	 * 				  to the gates list.
	 * */
	public void addNewGate(Gate newGate) {
		newGate.activateNodes();
		gates.add(newGate);
	}	// end addNewGate
	
	/* Method: onHover
	 * Pre Condition: Performs hover functions, depending if a wire is currently 
	 * 				  being set.
	 * Post Condition: Returns true if a gate or wire was being hovered over at 
	 * 			 	   the specific screen location.
	 * */
	public boolean onHover(int x, int y) {
		// If a wire is not being set
		if(refWire == null) {
			// Check if mouse is hovering over a gate on the circuit board
			for(int i = 0; i < gates.size(); i++) {
				// If mouse is hovering over a gate
				if(gates.get(i).onHover(x, y) || gates.get(i).onGateHover(x, y))
					return true;
			}
			// Check if hovering over a wire
			for(int i = 0; i < wires.size(); i++) {
				// If mouse is hovering over a wire
				if(wires.get(i).onHover(x, y)) {
					return true;
				}
			}
		}
		
		// If a wire is being set
		if(refWire != null) {
			Node n = null;
			boolean flag = false;
			// Check all gates to see if this wire's node matches the pos
			// of a gate node
			for(int i = 0; i < gates.size(); i++) {
				n = gates.get(i).getRefNode(x, y);
				// If there is a match
				if(n != null) {
					flag = true;
					break;
				}
			}
			// If no match with a gate node was made
			if(n == null) {
				// Check all wires to see if this wire's node matches the
				// pos of a wire node
				for(int i = 0; i < wires.size(); i++) {
					n = wires.get(i).getRefNode(x, y);
					// If there is a match
					if(n != null) {
						flag = true;
						break;
					}
				}
			}
			// If no match with a node was made
			if(n == null) {
				refWire.moveEndPoint(x, y);			// move wire node
				// If reference node is not null, make null
				if(refNode != null)
					refNode = null;
			} else {
				refWire.moveEndPoint(n);
				refNode = n;
			}
			return flag;
		}
		
		return false;
	}	// end onHover
	
	/* Method: onLeftClick
	 * Pre Condition: Performs appropriate functions when left click event
	 * 				  occurs over circuit board.
	 * */
	public void onLeftClick(int x, int y) {
		// If screen location is over circuit board
		if(area.contains(x, y)) {
			// If no wire is currently being sent
			if(refWire == null) {
				Node n = null;
				Gate g = null;
				// For all gates
				for(int i = 0; i < gates.size(); i++) {
					g = gates.get(i);
					n = g.getRefNode(x, y);
					// If hovering over gate node
					if(n != null) {
						// Run new wire
						int[] pos = n.getPos();
						refWire = new Wire(n, pos[0], pos[1]);
						break;
					// If hovering over gate but not node
					} else if(g.onHover(x, y)) {
						g.onClick(x, y);
					}
				}
				// If not hovering over gate node
				if(n == null) {
					// For all wires
					for(int i = 0; i < wires.size(); i++) {
						n = wires.get(i).getRefNode(x, y);
						// If hovering over wire node
						if(n != null) {
							// Run new wire
							int[] pos = n.getPos();
							refWire = new Wire(n, pos[0], pos[1]);
							break;
						}
					}
				}
			// If a wire is currently being set
			} else if(refWire != null) {
				// If there is a reference node being hovered over
				if(refNode != null) {
					refWire.moveEndPoint(refNode);
					refNode = null;
				}
				// Add new wire to the wires list
				wires.add(refWire);
				refWire = null;
			}
		}
	}	// end onLeftClick
	
	/* Method: onRightClick
	 * Pre Condition: Performs appropriate functions when right click event
	 * 				  occurs over circuit board.
	 * */
	public void onRightClick(int x, int y) {
		// If screen location is over circuit board
		if(area.contains(x, y)) {
			// If a wire is not being set
			if(refNode == null && refWire == null) {
				// For all wires
				for(int i = 0; i < wires.size(); i++) {
					// If screen location is over a wire
					if(wires.get(i).onHover(x, y)) {
						// detach this wire
						wires.get(i).detach(x, y);
						return;
					}
				}
			// If a wire is being set
			} else if(refWire != null) {
				refWire = null;
				return;
			}
			// For all gates
			for(int i = 0; i < gates.size(); i++) {
				// If screen location is over a gate
				if(gates.get(i).onGateHover(x, y)) {
					// detach this gate
					gates.get(i).detach();
					return;
				}
			}
		}
	}	// end onRightClick
	
	public void onMiddleClick(int x, int y) {}	// end onMiddleClick
}	// end CircuitBoard class

