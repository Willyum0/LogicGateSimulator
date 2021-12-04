/* --------------------------------------------------------------------------------
 * 								LogicGatesPanel
 * --------------------------------------------------------------------------------
 * 
 * Class object inherits the JPanel class, is used to create a panel for the
 * calling JFrame object. This panel will consist of all entities to exist in the 
 * frame, Including menu and simulation features.
 * 
 * */

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JPanel;

import modules.DisplayBar;
import modules.Gate;
import modules.NOTGate;
import modules.ANDGate;
import modules.ORGate;
import modules.PSource;
import modules.ResetButton;
import modules.Button;
import modules.CircuitBoard;
import modules.CloseButton;

public class LogicGatesPanel extends JPanel implements Runnable {
	private static final long serialVersionUID = -4575627373625583132L;
	
	private static final int NUM_DELAYS_PER_YIELD = 16;				// No. of delays before rendering thread
																	// yields;
	private static final int MAX_FRAME_SKIPS = 5;					// Max number of frame skips that can occur
																	// in a cycle
	private static final long MAX_STATS_INTERVAL = 1000000000L;		// The interval from which statistics are 
																	// recorded (nanosec)
	private long period;							// Stores a cycle's duration time
	private int panelWidth = 0, panelHeight = 0;		
	private Thread renderer;						// Rendering thread
	
	private volatile boolean running = false;		// Running flag
	private volatile boolean finishOff = false;		// Final process execution flag
	
	private long gameStartTime = 0L;				// Time the game starts
	private long prevStatsTime = 0L;				// Time of the last statistics recording
	private long framesSkipped = 0L;				// Number of frames skipped
	private long statsInterval = 0L;				// Incrememts the statistics duration
	private long frameCount = 0L;
	
	private long totalElapsedTime = 0L;				// Total duration of rendering thread
	private long totalFramesSkipped = 0L;			// Total amount of frames skipped
	private long statsCount = 0L;
	private double averageFPS = 0L, averageUPS = 0L;
	
	private double fpsStore[];						// Stores a number of recorded FPS for statistics
	private double upsStore[];						// Stores a number of recorded UPS for statistics
	private DecimalFormat df = new DecimalFormat("0.##");
	
	private Graphics2D dbg = null;					// Graphics object used for drawing
	private Image dbImage = null;					// Background image used during rendering
	private Font font;
	
	private ArrayList<DisplayBar> bars;				// List of display bars
	private ArrayList<Button> buttons;				// List of buttons
	private CloseButton cb;							// Close button
	private ResetButton rb;							// Reset button
	
	private CircuitBoard circuitBoard;				// Circuit board object
	
	private Gate clickedButton = null;				// Reference to clicked gate

	/* Constructor
	 * Pre Condition: Constructor receives long parameter of the period value
	 * 				  between each frame render. Initiates the panel parameters and
	 * 				  key listeners for the application.
	 * 
	 * */
	public LogicGatesPanel(long period) {
		// Set frame and period references
		this.period = period;
		
		// Retrieve screen width & height
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenDim = tk.getScreenSize();
		panelWidth = screenDim.width;
		panelHeight = screenDim.height;
		// Display frame size
		System.out.println("Screen Width / Height: " + 
				panelWidth + " / " + 
				panelHeight);
		
		// Set panel traits
		setBackground(Color.white);		// Background colour
		setPreferredSize(screenDim);	// Set preferred panel size
		setFocusable(true);				// Allow this panel to be focused
		requestFocus();					// Set this panel to receive events
		
		terminationEvent();				// Add termination event handling
		
		// Add key listeners
		addKeyListener( new KeyAdapter() {		// Key board listener
			public void keyPressed(KeyEvent e) {
				handleKeyEvents(e);
			}	// end keyPressed
		});
		
		addMouseListener( new MouseAdapter() {	// Mouse button listener
			public void mousePressed(MouseEvent e) {
				handleMouseClickEvents(e);
			}
		});
		
		addMouseMotionListener( new MouseMotionAdapter() {	// Mouse movement listener
			public void mouseMoved(MouseEvent e) {
				handleMouseMovedEvents(e);
			}
		});
		
		// Initiate FPS / UPS statistics storage
		fpsStore = new double[LogicGates.NUM_FPS];
		upsStore = new double[LogicGates.NUM_FPS];
		for(int i = 0; i < LogicGates.NUM_FPS; i++) {
			fpsStore[i] = 0.0;
			upsStore[i] = 0.0;
		}
		
		initDisplay();
		
	}	// end Constructor
	
	/* Method: initDisplay
	 * Pre Condition: Initiates display bars and buttons associated with the 
	 * 				  display of this application. This includes the circuit
	 * 				  board object.
	 * */
	private void initDisplay() {
		bars = new ArrayList<>();
		bars.add(new DisplayBar(1200, 0, (panelWidth - 1200), panelHeight));
		
		cb = new CloseButton(panelWidth - 80, 20);
		rb = new ResetButton(panelWidth - 125, 20);
		
		buttons = new ArrayList<>();
		buttons.add(cb);
		buttons.add(rb);
		
		bars.get(0).addButton(new ANDGate());
		bars.get(0).addButton(new ORGate());
		bars.get(0).addButton(new NOTGate());
		bars.get(0).addButton(new PSource());
		
		circuitBoard = new CircuitBoard(0, 0, 1200, panelHeight);
	}	// end initDisplay

	/* Method: handleKeyEvents
	 * Pre Condition: Receives key event and executes responding 
	 * 				  methods accordingly.
	 * */
	private void handleKeyEvents(KeyEvent e) {
		int kc = e.getKeyCode();
		// If escape key is pressed
		if((kc == KeyEvent.VK_ESCAPE) || (kc == KeyEvent.VK_END)) {
			running = false;
		}
	}	// end handleKeyEvents
	
	/* Method: handleMouseClickEvents
	 * Pre Condition: Receives mouse event and executes responding
	 * 				  methods accordingly.
	 * */
	private void handleMouseClickEvents(MouseEvent e) {
		// If left mouse button was clicked
		if(e.getButton() == MouseEvent.BUTTON1) {
			leftMouseButtonClicked(e);
		}
		// If middle mouse button was clicked
		if(e.getButton() == MouseEvent.BUTTON2) {
			middleMouseButtonClicked(e);
		}
		// If right mouse button was clicked
		if(e.getButton() == MouseEvent.BUTTON3) {
			rightMouseButtonClicked(e);
		}
	}	// handleMouseClickEvents
	
	/* Method: leftMouseButtonClicked
	 * Pre Condition: Receives mouse event and executes processes 
	 * 				  that occur when the left mouse button is clicked 
	 * */
	private void leftMouseButtonClicked(MouseEvent e) {
		// Get mouse location on the screen
		int x = e.getX();
		int y = e.getY();
		
		// If mouse is over the close button
		if(cb.onHover(x, y)) {
			cb.click();
		}
		// If mouse is over the reset button
		if(rb.onHover(x, y)) {
			rb.click();
		}
		// If a referenced is being referenced 
		if(clickedButton != null) {
			// Add new gate to the circuit board in current location
			circuitBoard.addNewGate(clickedButton);
			clickedButton = null;	// dereference gate
		}
		// If mouse is hovering over the side bar containing all logic
		// gates
		if(bars.get(0).isHovering(x, y)) {
			// Get new referenced gate
			clickedButton = bars.get(0).getReferencedGate();
			// Set referenced gate location to that of the mouse's location
			clickedButton.setPos(x - (clickedButton.getWidth() / 2), y - (clickedButton.getHeight() / 2));
		}
		// Send event to circuit board
		circuitBoard.onLeftClick(x, y);
	}	// end leftMouseButtonClicked
	
	/* Method: middleMouseButtonClicked
	 * Pre Condition: Receives mouse event and executes processes 
	 * 				  that occur when the middle mouse button is clicked 
	 * */
	private void middleMouseButtonClicked(MouseEvent e) {
		// Get mouse location on the screen
		int x = e.getX();
		int y = e.getY();
		// Send event to circuit board
		circuitBoard.onMiddleClick(x, y);
	}	// end middleMouseButtonClicked

	/* Method: rightMouseButtonClicked
	 * Pre Condition: Receives mouse event and executes processes 
	 * 				  that occur when the right mouse button is clicked 
	 * */
	private void rightMouseButtonClicked(MouseEvent e) {
		// Get mouse location on the screen
		int x = e.getX();
		int y = e.getY();
		// Send event to circuit board
		circuitBoard.onRightClick(x, y);
	}	// end rightMouseButtonClicked
	
	/* Method: handleMouseMovedEvents
	 * Pre Condition: Receives mouse event and executes responding
	 * 				  methods accordingly.
	 * */
	private void handleMouseMovedEvents(MouseEvent e) {
		// Get mouse location on the screen
		int x = e.getX();
		int y = e.getY();
		boolean isHovering = false;
		
		// If mouse is over the close button
		if(cb.onHover(x, y)) {
			isHovering = true;
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		// If mouse is over the resetButton
		if(rb.onHover(x, y)) {
			isHovering = true;
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		// If mouse is over the side bar
		if(bars.get(0).isHovering(x, y)) {
			isHovering = true;
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} 
		// If mouse is over the circuit board 
		if(circuitBoard.onHover(x, y)) {
			isHovering = true;
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		// If mouse is not hovering over any of the prior
		if(!isHovering){
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));	
		}
		// If the referenced gate does not equal null
		if(clickedButton != null) {
			clickedButton.setPos(x - (clickedButton.getWidth() / 2), y - (clickedButton.getHeight() / 2));
		}
	}	// end handleMouseMovedEvents
	
	/* Method: addNotify
	 * Pre Condition: Executes the rendering thread.
	 * */
	public void addNotify() {
		super.addNotify();
		// If rendering thread is null
		if(renderer == null || !running) {
			renderer = new Thread(this);
			renderer.start();
		}
	}	// end addNotify
	
	/* Method: run
	 * Pre Condition: executes the rendering thread's processes. This contains
	 * 				  the rendering cycle loop and also records details of the 
	 *				  rendering performance.
	 * */
	@Override
	public void run() {
		// FPS calculation variables
		long beforeTime, afterTime, timeDiff, sleepTime;
		long overSleepTime = 0L;
		long excess = 0L;
		int noDelays = 0;
		
		// Retrieve rendering start time
		gameStartTime = System.nanoTime();
		prevStatsTime = gameStartTime;
		beforeTime = gameStartTime;
		// Initiate rendering flag
		running = true;
		
		// Render loop
		while(running) {
			// Update the screen
			// Render the screen
			// Draw the screen
			panelUpdate();
			panelRender();
			panelPaint();
			
			afterTime = System.nanoTime();			// Get current time
			timeDiff = afterTime - beforeTime;		// Calculate time between now and the 
													// render cycle end
			sleepTime = (period - timeDiff) - overSleepTime; 	// Calculate sleep time
			
			// If this cycle finishes early
			if(sleepTime > 0) {
				try {
					Thread.sleep(sleepTime / 1000000L);		// nano --> ms
				} catch(InterruptedException e) {
					System.out.println(e);
				}
				// Calculate time thread over-slept 
				overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
			// If this cycle finishes late
			} else {
				excess -= sleepTime;		// Calculate by how late cycle was
				overSleepTime = 0L;			// Clear oversleep value
				
				// Number of delays exceeds maximum delays before yield
				if(++noDelays >= NUM_DELAYS_PER_YIELD) {
					Thread.yield();
					noDelays = 0;
				}	
			}
			beforeTime = System.nanoTime();
			
			// If the game is taking too long to render, perform
			// extra updates per cycle
			int skips = 0;
			while((excess > period) && (skips < MAX_FRAME_SKIPS)) {
				excess -= period;
				panelUpdate();
				skips++;
			}
			framesSkipped += skips;		// Incrememnt no. frames skipped
			storeStats();				// Update statistics
		}
		finishOff();
	}	// end run
	
	/* Method: panelUpdate
	 * Pre Condition: Performs updates to all objects present in the
	 * 				  application.
	 * */
	private void panelUpdate() {
		// If rendering thread is still live
		if(running) {
			// If close button has been pressed
			if(cb.isPressed()) {
				running = false;
			}
			// If reset button has been pressed
			if(rb.isPressed()) {
				circuitBoard.clear();
				rb.acknowledge();
			}
			circuitBoard.update();		// Update circuit board
		}
	}	// end panelUpdate
	
	/* Method: panelRender
	 * Pre Condition: Performs rendering processes for the simulation.
	 * 				  This includes retrieving screen graphics object used
	 * 				  to paint all objects on the screen.
	 * 				  All calls to paint an object will be made in this
	 * 				  method.
	 * */
	private void panelRender() {
		// If panel image has not been initiated
		if(dbImage == null) {
			dbImage = createImage(panelWidth, panelHeight);	// Create new panel image
			if(dbImage == null) {							// If image init failed
				printError("panel image is null");
				return;
			} else {										// If image init succeeded
				dbg = (Graphics2D) dbImage.getGraphics();
			}
		}
		// Apply rendering hints to graphics object
		applyRenderingHints(dbg);
		
		// Clear the panel
		dbg.setColor(new Color(40, 40, 35));
		dbg.fillRect(0, 0, panelWidth, panelHeight);
		
		// Draw all objects from the following
		drawCircuitBoard(dbg);
		drawPanelBars(dbg);
		drawButtons(dbg);
		
		// Display FPS / UPS
		dbg.setColor(Color.white);
		dbg.setFont(font);
		dbg.drawString("Average FPS: " + df.format(averageFPS), 20, 25);
		dbg.drawString("Average UPS: " + df.format(averageUPS), 20, 40);
		
	}	// end panelRender
	
	/* Method: applyRenderingHints
	 * Pre Condition: Receives graphics object, applies desired rendering
	 * 				  hints to the object.
	 * */
	private void applyRenderingHints(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, 
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, 
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_DITHERING, 
				RenderingHints.VALUE_DITHER_ENABLE);
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, 
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, 
				RenderingHints.VALUE_STROKE_PURE);
	}	// end applyRenderingHints
	
	/* Method: panelPaint
	 * Pre Condition: Execute the graphics object, rendering all objects
	 * 				  on the screen. Then disposes of the graphics object.s
	 * */
	private void panelPaint() {
		Graphics g;
		try {
			// Retrieve graphics object for this panel
			g = this.getGraphics();
			if((g != null) && (dbImage != null)) {			// If graphics and background
															// image are present
				g.drawImage(dbImage, 0, 0, null);
			}
			
			Toolkit.getDefaultToolkit().sync();
			g.dispose();
			
		} catch(Exception e) {
			printError("Failed to paint panel: " + e.toString());
		}
	}	// end panelPaint
	
	/* Method: drawPanelBars
	 * Pre Condition: Performs all drawing methods for the display bars
	 * */
	private void drawPanelBars(Graphics2D g) {
		bars.forEach((n) -> n.draw(g));
	}	// end drawPanelBars
	
	/* Method: drawButtons
	 * Pre Condition: Performs all drawing methods for the buttons
	 * */
	private void drawButtons(Graphics2D g) {
		buttons.forEach((n) -> n.draw(g));
		
		if(clickedButton != null) {
			clickedButton.draw(g);
		}	
	}	// end drawButtons
	
	/* Method: drawCircuitBoard
	 * Pre Condition: Performs all drawing methods for the circuitBoard
	 * */
	private void drawCircuitBoard(Graphics2D g) {
		circuitBoard.draw(g);
	}	// end circuitBoard
	
	/* Method: storeStats
	 * Pre Condition: Performs statistics calculations on the rendering performance.
	 * */
	private void storeStats() {
		frameCount++;					// Increment frame count
		statsInterval += period;		// Increment stats interval by cycle time
		
		if(statsInterval >= MAX_STATS_INTERVAL) {		// If stats interval reached
			// Calculate elapsed time since last stats call
			long timeNow = System.nanoTime();			
			long realElapsedTime = timeNow - prevStatsTime;
			totalElapsedTime += realElapsedTime;
			
			// Increment total frames skipped
			totalFramesSkipped += framesSkipped;
			// Calculate FPS & UPS
			double actualFPS = 0.0;
			double actualUPS = 0.0;
			if(totalElapsedTime > 0) {
				actualFPS = (((double) frameCount / totalElapsedTime) * 1000000000L);
				actualUPS = (((double) (frameCount + totalFramesSkipped) / totalElapsedTime) * 1000000000L);
			}
			// Add to FPS & UPS stores
			fpsStore[ (int)statsCount % LogicGates.NUM_FPS ] = actualFPS;
			upsStore[ (int)statsCount % LogicGates.NUM_FPS ] = actualUPS;
			statsCount = statsCount + 1;
			
			// Calculate average FPS & UPS
			double totalFPS = 0.0;
			double totalUPS = 0.0;
			for(int i = 0; i < LogicGates.NUM_FPS; i++) {
				totalFPS += fpsStore[i];
				totalUPS += upsStore[i];
			}
			 
			if(statsCount < LogicGates.NUM_FPS) {
				averageFPS = totalFPS / statsCount;
				averageUPS = totalUPS / statsCount;
			} else {
				averageFPS = totalFPS / LogicGates.NUM_FPS;
				averageUPS = totalUPS / LogicGates.NUM_FPS;
			}
			
			// Reset variables
			framesSkipped = 0L;
			prevStatsTime = timeNow;
			statsInterval = 0L;
		}
	}	//end storeStats
	
	/* Method: printError
	 * Pre Condition: Prints error messages passed to it via a String.
	 * */
	private void printError(String errMessage) {
		System.out.println("ERROR: " + errMessage);
	}	// end printError
	
	/* Method: terminationEvent
	 * Pre Condition: Creates a new thread upon the termination of the rendering
	 * 				  thread, used to perform the final processes.
	 * */
	private void terminationEvent() {
		// Create new thread to handle termination
		Runtime.getRuntime().addShutdownHook( new Thread() {
			public void run() {
				running = false;
				finishOff();
			}
		});
	}	// end terminationEvent
	
	/* Method: finishOff
	 * Pre Condition: Performs final processes before application termination.
	 * */
	private void finishOff() {
		// If termination has been executed
		if(finishOff == false) {
			finishOff = true;
			System.exit(0);
		}
	}	// end finishOff
}	// end LogicGatePanel class
