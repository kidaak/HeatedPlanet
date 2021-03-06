package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;

import simulation.Earth;
import view.util.ThermalVisualizer;
import view.widgets.EarthImage;
import view.widgets.GridDisplay;
import view.widgets.SimulationStatus;
import common.IGrid;

public class EarthDisplay extends JFrame implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -309131746356718870L;
	
	// core display
	private final JLayeredPane display;
	
	// widgets
	private SimulationStatus simStatus;
	private EarthImage earthImage;
	private GridDisplay gridDisplay;
	
	private static final String COLORMAP = "thermal";
	private static final float OPACITY = 0.6f;
			
	private static final int EARTH = 0;
	private static final int GRID = 1;
	
	private int gs = 0, timeStep = 0;
	
	public EarthDisplay() {
		
		super("Earth Simulation");
		
		EarthDisplay.setDefaultLookAndFeelDecorated(true);
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(this); // for window close actions
		this.setLayout(new BorderLayout());
		this.setResizable(true);
		
		// Add sim settings
		simStatus = new SimulationStatus();
		this.add(simStatus, BorderLayout.SOUTH);
		
		// Add the display region
		display = new JLayeredPane();
		this.add(display, BorderLayout.CENTER);
		
		// Add EarthImage
		earthImage = new EarthImage();
		display.add(earthImage, new Integer(EARTH));
		
		int w = earthImage.getImageWidth();
		int h = earthImage.getImageHeight();

		// Add grid
		gridDisplay = new GridDisplay(new ThermalVisualizer(COLORMAP, Earth.MIN_TEMP, Earth.MAX_TEMP, OPACITY), w, h);
		display.add(gridDisplay, new Integer(GRID));
		
		this.setPreferredSize(new Dimension(w, h + 130));

	}
	
	public void display(int gs, int timeStep) {
		
		this.gs = gs;
		this.timeStep = timeStep;
		
		this.pack();
		this.setVisible(true);
		this.validate();
	}

	public void close() {
		this.dispose();
	}
	
	public void update(IGrid grid) {
			
		if (grid != null){
			//System.out.println(((Grid)grid).getRSquared());
			simStatus.update(
                                grid.getSunPositionDeg(), 
                                grid.getOrbitalAngle(), 
                                grid.getDistanceFromSun(), 
                                grid.getCurrentTime(), 
                                this.gs, 
                                this.timeStep);
		} else
			simStatus.update(0, 0, 0, 0, this.gs, this.timeStep);
		gridDisplay.update(grid);
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		JOptionPane.showMessageDialog(this, "Please terminate simulation using control GUI.");
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
