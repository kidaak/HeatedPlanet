// GUI.java
package PlanetSim;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import persistenceManager.PersistenceManager;
import common.EarthGridProperties;
import common.EarthGridProperties.EarthGridProperty;

public class ControllerGUI extends JFrame implements ActionListener, WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6146431536208036768L;
	
	private Controller controller;

	private int precisionDigits;
	private int geographicAccuracy;
	private int temporalAccuracy;
	private Demo pcontrol; // for handling view between query and control gui
	
	private HashMap<String, JTextField> inputs = new HashMap<String, JTextField>();
	private HashMap<String, JButton> buttons = new HashMap<String, JButton>();
	private HashMap<String, JCheckBox> checkboxes = new HashMap<String, JCheckBox>();

	public ControllerGUI(int precisionDigits, int geographicAccuracy, int temporalAccuracy, Demo pcontrol) {
		this.precisionDigits = precisionDigits;
		this.geographicAccuracy = geographicAccuracy;
		this.temporalAccuracy = temporalAccuracy;
		this.pcontrol = pcontrol;
		
		controller = new Controller();

		setupWindow();
	}

	private void setupWindow() {
		
		// setup overall app ui
		setTitle("Heated Earth Diffusion Simulation");
		
		setSize(300, 400);
		
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		addWindowListener(this);
		setAlwaysOnTop(true);
		
		add(settingsAndControls(), BorderLayout.CENTER);
		pack();
		lowerRightWindow(); // Set window location to lower right (so we don't hide dialogs)
	}
	
	private void lowerRightWindow() {
	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) (dimension.getWidth() - this.getWidth());
	    int y = (int) (dimension.getHeight() - this.getHeight());
	    this.setLocation(x, y);
	}
	
	private JPanel settingsAndControls() {
		
		JPanel sncPanel = new JPanel();
		sncPanel.setLayout(new BoxLayout(sncPanel, BoxLayout.PAGE_AXIS));
		sncPanel.setAlignmentY(Component.TOP_ALIGNMENT);

		sncPanel.add(settings(), BorderLayout.WEST);
		sncPanel.add(runControls(), BorderLayout.WEST);

		return sncPanel;
	}

	private JPanel settings() {
		
		JPanel settingsPanel = new JPanel();
		settingsPanel.setBorder(BorderFactory.createTitledBorder("Settings"));
		settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.PAGE_AXIS));
		settingsPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		
		settingsPanel.add(inputField("Sim Name", Controller.DEFAULT_SIM_NAME));
		settingsPanel.add(inputField("Grid Spacing", Integer.toString(Controller.DEFAULT_GRID_SPACING)));
		settingsPanel.add(inputField("Simulation Time Step",Integer.toString(Controller.DEFAULT_TIME_STEP)));
		settingsPanel.add(inputField("Presentation Rate",Float.toString(Controller.DEFAULT_PRESENTATION_RATE)));
		settingsPanel.add(inputField("Axial Tilt",Float.toString(Controller.DEFAULT_AXIAL_TILT)));
		settingsPanel.add(inputField("Orbital Eccentricity",Float.toString(Controller.DEFAULT_ECCENTRICITY)));		
		settingsPanel.add(inputField("Simulation Duration (months)",Integer.toString(Controller.DEFAULT_DURATION)));
		settingsPanel.add(inputField("Precision Digits After Decimal",Integer.toString(precisionDigits)));
		settingsPanel.add(inputField("Geographic Precision (0-100)",Integer.toString(geographicAccuracy)));
		settingsPanel.add(inputField("Temporal Precision (0-100)",Integer.toString(temporalAccuracy)));
		settingsPanel.add(inputCheckbox("Show Animation?", true));
		return settingsPanel;
	}

	private JPanel runControls() {
		
		JPanel ctrlsPanel = new JPanel(new FlowLayout());
		ctrlsPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);

		ctrlsPanel.add(button("Start"));
		ctrlsPanel.add(button("Pause"));
		ctrlsPanel.add(button("Resume"));
		ctrlsPanel.add(button("Stop"));

		buttons.get("Start").setEnabled(true);
		buttons.get("Pause").setEnabled(false);
		buttons.get("Resume").setEnabled(false);
		buttons.get("Stop").setEnabled(false);
		
		return ctrlsPanel;
	}

	private JPanel inputField(String name, String defaultText) {
		
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new FlowLayout());
		inputPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);

		JLabel l = new JLabel(name);
		l.setAlignmentX(Component.LEFT_ALIGNMENT);
		inputPanel.add(l);

		JTextField t = new JTextField(defaultText, 10);
		t.setAlignmentX(Component.RIGHT_ALIGNMENT);
		l.setLabelFor(t);
		inputPanel.add(t);

		inputs.put(name, t);
		return inputPanel;
	}

	private JPanel inputCheckbox(String name, boolean setting) {
		
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new FlowLayout());
		inputPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);

		JCheckBox t = new JCheckBox(name, setting);
		t.setAlignmentX(Component.CENTER_ALIGNMENT);
		inputPanel.add(t);

		checkboxes.put(name, t);
		return inputPanel;
	}

	private JButton button(String name) {
		
		JButton button = new JButton(name);
		button.setActionCommand(name);
		button.addActionListener(this);
		buttons.put(name, button);
		return button;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		String cmd = e.getActionCommand();
		
		if ("Start".equals(cmd)) {
			if (configureEngine()) {
				//do gui stuff to indicate start has occurred.
				buttons.get("Start").setEnabled(false);
				buttons.get("Pause").setEnabled(true);
				buttons.get("Resume").setEnabled(false);
				buttons.get("Stop").setEnabled(true);
			}
		}
		
		else if ("Pause".equals(cmd)) {
			controller.pause();
			buttons.get("Pause").setEnabled(false);
			buttons.get("Resume").setEnabled(true);
		}
		
		else if ("Resume".equals(cmd)) {
			controller.resume();
			buttons.get("Pause").setEnabled(true);
			buttons.get("Resume").setEnabled(false);
			
		}
		
		else if ("Stop".equals(cmd)) {
			try {
				controller.stop();
			} catch (InterruptedException e1) {
			}
			
			buttons.get("Start").setEnabled(true);
			buttons.get("Pause").setEnabled(false);
			buttons.get("Resume").setEnabled(false);
			buttons.get("Stop").setEnabled(false);
		}
	}

	private boolean configureEngine() {
		
		try {
			
			final String name = inputs.get("Sim Name").getText();
			final int gs = Integer.parseInt(inputs.get("Grid Spacing").getText());
			final int timeStep = Integer.parseInt(inputs.get("Simulation Time Step").getText());
			final float presentationRate = Float.parseFloat(inputs.get("Presentation Rate").getText());
			final float axialTilt = Float.parseFloat(inputs.get("Axial Tilt").getText());
			final float eccentricity = Float.parseFloat(inputs.get("Orbital Eccentricity").getText());
			final int simDuration = Integer.parseInt(inputs.get("Simulation Duration (months)").getText());
			final int runPrecision = Integer.parseInt(inputs.get("Precision Digits After Decimal").getText());
			final int runGeoPrecision = Integer.parseInt(inputs.get("Geographic Precision (0-100)").getText());
			final int runTimePrecision = Integer.parseInt(inputs.get("Temporal Precision (0-100)").getText());
			
			// Insure sim name is not already used
			if(PersistenceManager.getAllSimNames().contains(name)) {
				throw new IllegalArgumentException("Simulation by that name already exists!");
			}
			
			EarthGridProperties simProp = new EarthGridProperties();
			simProp.setProperty(EarthGridProperty.NAME, name);
			simProp.setProperty(EarthGridProperty.GRID_SPACING, gs);
			simProp.setProperty(EarthGridProperty.SIMULATION_TIME_STEP, timeStep);
			simProp.setProperty(EarthGridProperty.PRESENTATION_RATE, presentationRate);
			simProp.setProperty(EarthGridProperty.AXIAL_TILT, axialTilt);
			simProp.setProperty(EarthGridProperty.ECCENTRICITY, eccentricity);
			simProp.setProperty(EarthGridProperty.SIMULATION_LENGTH, simDuration);
			simProp.setProperty(EarthGridProperty.PRECISION, runPrecision);
			simProp.setProperty(EarthGridProperty.TIME_PRECISION, runTimePrecision);
			simProp.setProperty(EarthGridProperty.GEO_PRECISION, runGeoPrecision);
			
			Calendar endDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			endDate.clear();
			endDate.set(2014, 0, 4, 12 ,0);
			endDate.add(Calendar.MONTH, simDuration);
			simProp.setProperty(EarthGridProperty.END_DATE, endDate);
			
			controller.start(simProp, checkboxes.get("Show Animation?").isSelected());
			
			return true;

		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(null, "Please correct input: "+ex.getMessage());
		}
				
		return false;
	}
	
	@Override
    public void windowClosed(WindowEvent e) {
    }

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
        // Simulate stop button press
		JButton stop = buttons.get("Stop");
		ActionEvent pseudoStop = new ActionEvent(stop, ActionEvent.ACTION_FIRST, stop.getActionCommand());
		actionPerformed(pseudoStop);
		// switch control back to GUI
        System.out.printf("Closing sim window\n");
        if(pcontrol != null) {
            pcontrol.viewQueryGUI();
        }
        else {
        	System.out.printf("No pcontrol available to swith back to query!  Start with Demo...\n");
        }
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
