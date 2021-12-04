/* --------------------------------------------------------------------------------
 * 								Gate
 * --------------------------------------------------------------------------------
 * 
 * Abstract class object represents a logic gate used in simulations. All gates
 * will inherit the corresponding attributes and methods associated with the main 
 * functions of a logic gate.
 * 
 * */

package modules;

public abstract class Gate extends Button {
	
	private boolean gateActive = false;		// Flag used to determine if this gate
											// is being used as a gate or button
	protected boolean isDetached = false;	// Flag used to determine if this gate
											// has been removed from circuit board

	/* Constructor
	 * Pre Condition: Receives location on screen, width and height.
	 * */
	public Gate(int x, int y, int width, int height) {
		super(x, y, width, height);
	}	// end Constructor
	
	/* Method: isGateActive
	 * Post Condition: Returns true if this gate is active (not a button).
	 * */
	public boolean isGateActive() {
		return gateActive;
	}	// end isGateActve
	
	/* Method: activateNodes
	 * Pre Condition: Activates the gate and gate nodes.
	 * */
	public void activateNodes() {
		gateActive = true;
	}	// end activateNodes
	
	/* Method: deactivateNodes
	 * Pre Condition: Deactivates the gate and gate nodes.
	 * */
	public void deactivateNodes() {
		gateActive = false;
	}	// end deactivateNodes
	
	/* Method: isDetached
	 * Post Condition: Returns true if this gate has been detached from
	 * 				   circuit board.
	 * */
	public boolean isDetached() {
		return isDetached;
	}	// end isDetached
	
	// Abstract methods
	public abstract void detach();
	public abstract Node getRefNode(int x, int y);
	public abstract boolean onGateHover(int x, int y);
	public abstract void update();
}	// end Gate class
