/* --------------------------------------------------------------------------------
 * 								PSource
 * --------------------------------------------------------------------------------
 * 
 * Class object represents a power source switch, used in simulations. This switch 
 * changes the charge of it's output node. 
 * 
 * */

package modules;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class PSource extends Gate {
	
	private static final int WIDTH = 30;	// Button area width
	private static final int HEIGHT = 30;	// Button area height
	
	private Rectangle2D area;				// Button area
	private Node node;						// Output node
	
	private boolean outputCharge = false;	// Output charge

	/* Constructor
	 * Pre Condition: Uses default location and initiates shape objects
	 * 				  and nodes. 
	 * */
	public PSource() {
		super(200, 200, WIDTH, HEIGHT);
		area = new Rectangle2D.Double(x, y, WIDTH, HEIGHT);
		node = new Node(x + 40, y + 7, Node.RIGHT);
	}	// end Constructor
	
	/* Constructor
	 * Pre Condition: Uses provided location and initiates shape objects
	 * 				  and nodes.
	 * */
	public PSource(int x, int y) {
		super(x, y, WIDTH, HEIGHT);
		area = new Rectangle2D.Double(x, y, WIDTH, HEIGHT);
		node = new Node(x + 40, y + 7, Node.RIGHT);
	}	// end Constructor
	
	/* Method: setPos
	 * Pre Condition: Receives new screen location.
	 * */
	@Override
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
		area.setRect(x, y, WIDTH, HEIGHT);
		node.setPos(x + 40, y + 7);
	}	// end setPos

	/* Method: onHover
	 * Pre Condition: Receives x and y of current screen
	 * 				  location. Performs hovering processes.
	 * Post Condition: Returns true if the mouse is hovering over this
	 * 				   power switch area or the node.
	 * */
	@Override
	public boolean onHover(int x, int y) {
		return area.contains(x, y) || node.onHover(x, y);
	}	// end onHover
	
	/* Method: onGateHover
	 * Pre Condition: Determines if the specified location is contained
	 * 				  in the area of the power switch.
	 * Post Condition: Returns true if this point exists in the area.
	 * */
	@Override
	public boolean onGateHover(int x, int y) {
		return area.contains(x, y);
	}	// end onGateHover

	/* Method: onClick
	 * Pre Condition: Changes the charge of the output node.
	 * */
	@Override
	public void onClick(int x, int y) {
		outputCharge = !outputCharge;
		node.setCharge(outputCharge);
	}	// end onClick

	@Override
	public void click() {}

	@Override
	public boolean isPressed() { return false; }

	@Override
	public void acknowledge() {}
	
	/* Method: getRefNode
	 * Pre Condition: Receives screen location (x, y) and determines
	 * 				  if the node contains this point.
	 * Post Condition: Returns the node if true, otherwise returns null.
	 * */
	@Override
	public Node getRefNode(int x, int y) {
		// If power switch is activated
		if(isGateActive()) {
			// If node is being hovered over
			if(node.onHover(x, y))
				return node;
		}
		return null;
	}	// end getRefNode
	
	/* Method: update
	 * Pre Condition: Attaches the node if it was detached during
	 * 				  the removal of a wire.
	 * */
	@Override
	public void update() {
		// If output node was detached and this power source is still
		// attached
		if(node.isDetached() && !isDetached)
			node.attach();
	}	// end update
	
	/* Method: draw
	 * Pre Condition: Receives graphics object used to draw all shapes
	 * 				  and the node related with this power switch
	 * */
	@Override
	public void draw(Graphics2D g) {
		// Draw rectangle
		g.setColor(new Color(204, 86, 2));
		g.setStroke(new BasicStroke(3f));
		g.draw(area);
		g.setStroke(new BasicStroke(1f));
		// Draw node
		node.draw(g);
		// Draw node stick
		g.drawLine(x + 30, y + 15, x + 42, y + 15);
	}	// end draw

	/* Method: detach
	 * Pre Condition: Sets this power switch to be detached. Detaches 
	 * 				  the output node.
	 * */
	@Override
	public void detach() {
		isDetached = true;
		node.detach();
	}	// end detach
}	// end PSource class
