/* --------------------------------------------------------------------------------
 * 							LOGIC GATES SIMULATOR
 * --------------------------------------------------------------------------------
 * 
 * Commenced: 		23/11/2021
 * Last Update: 	3/12/2021
 * 
 * An application designed to simulate the behavior of logic gates connected in a 
 * circuit. This will consist of the standard logic gates:
 * 		- AND
 * 		- OR
 * 		- NOT
 *
 * With the addition of other common circuit components:
 * 		- Power Source Button
 * 
 * */

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JFrame;

public class LogicGates extends JFrame {
	private static final long serialVersionUID = 925972459479404212L;
	
	public static final int FPS = 80;		// Default frame rate of the simulation
	public static final int NUM_FPS = 10;	// No. FPS samples
	
	/* Constructor
	 * Pre Condition: Constructor receives long parameter of the period value 
	 * 				  between each frame render. Initiates new panel object for the 
	 * 			      frame.
	 * */
	private LogicGates(long period) {
		super("Logic Gate Simulation");
		// Retrieve frame container and set border layout
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		// Create new frame panel 
		LogicGatesPanel panel = new LogicGatesPanel(period);
		c.add(panel, "Center");
		
		// Set JFrame traits
		setUndecorated(true);				// Hide frame decorations
		setIgnoreRepaint(true);				// Ignore repaint messages
		pack();
		setResizable(false);				// Frame size fixed
		setVisible(true);					// Display the frame
		
	}	// end Constructor

	/* Main Method
	 * Pre Condition: Receives input parameters from the console. Creates new frame
	 * 				  object.
	 * */
	public static void main(String[] args) {
		// FPS for simulation render speed
		int fps = FPS;
		long period = (long) 1000.0 / fps;
		
		// Initiate new Logic Gate frame
		new LogicGates(period * 1000000L);
	}	// end Main
	
}	// end LogicGates class
