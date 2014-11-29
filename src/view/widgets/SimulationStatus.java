package view.widgets;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class SimulationStatus extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4874764682275993951L;
	
	private JTextField sunPosStatus, orbitalPosStatus, sunDistanceStatus, currTimeStatus, gsStatus, timeStepStatus;
	private JLabel lblSunPos, lblOrbitalPos, lblSunDistance, lblCurrTime, lblGs, lblTimeStep;
	
	private static final int HEIGHT = 6;
	private static final int WIDTH = 2;
	private static final int HGAP = 1;
	private static final int VGAP = 1;
	
	//private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yy HH:mm:SS");
	
	public SimulationStatus() {
		
		this.setBorder(new EmptyBorder(10,10,10,10));
		this.setLayout(new GridLayout(HEIGHT,  WIDTH, HGAP, VGAP));
		
		sunPosStatus 	= new JTextField("0");
                orbitalPosStatus = new JTextField("0");
                sunDistanceStatus = new JTextField("0");
		currTimeStatus 	= new JTextField("0");
		gsStatus 		= new JTextField("0");
		timeStepStatus 	= new JTextField("0");
		
		lblSunPos 	= new JLabel("Rotational Position:");
                lblOrbitalPos = new JLabel("Orbital Position:");
                lblSunDistance = new JLabel("Distance From Sun:");
		lblCurrTime = new JLabel("Time:");
		lblGs 		= new JLabel("Grid Spacing:");
		lblTimeStep = new JLabel("Simulation Time Step:");
                
		
		sunPosStatus.setPreferredSize(new Dimension(10, 10));
		sunPosStatus.setMaximumSize(new Dimension(10, 10));
		sunPosStatus.getFont().deriveFont(Font.PLAIN, 10);
		sunPosStatus.setEditable(false);
                
                orbitalPosStatus.setPreferredSize(new Dimension(10, 10));
		orbitalPosStatus.setMaximumSize(new Dimension(10, 10));
		orbitalPosStatus.getFont().deriveFont(Font.PLAIN, 10);
		orbitalPosStatus.setEditable(false);
                
                sunDistanceStatus.setPreferredSize(new Dimension(10, 10));
		sunDistanceStatus.setMaximumSize(new Dimension(10, 10));
		sunDistanceStatus.getFont().deriveFont(Font.PLAIN, 10);
		sunDistanceStatus.setEditable(false);
		
		currTimeStatus.setPreferredSize(new Dimension(10, 10));
		currTimeStatus.setMaximumSize(new Dimension(10, 10));
		currTimeStatus.getFont().deriveFont(Font.PLAIN, 10);
		currTimeStatus.setEditable(false);
		
		gsStatus.setPreferredSize(new Dimension(10, 10));
		gsStatus.setMaximumSize(new Dimension(10, 10));
		gsStatus.getFont().deriveFont(Font.PLAIN, 10);
		gsStatus.setEditable(false);
		
		timeStepStatus.setPreferredSize(new Dimension(10, 10));
		timeStepStatus.setMaximumSize(new Dimension(10, 10));
		timeStepStatus.getFont().deriveFont(Font.PLAIN, 10);
		timeStepStatus.setEditable(false);
		
		lblSunPos.getFont().deriveFont(Font.PLAIN, 8);
                lblOrbitalPos.getFont().deriveFont(Font.PLAIN, 8);
                lblSunDistance.getFont().deriveFont(Font.PLAIN, 8);
		lblCurrTime.getFont().deriveFont(Font.PLAIN, 8);
		lblGs.getFont().deriveFont(Font.PLAIN, 8);
		lblTimeStep.getFont().deriveFont(Font.PLAIN, 8);
		
		this.add(lblSunPos);
		this.add(sunPosStatus);
                
                this.add(lblOrbitalPos);
                this.add(orbitalPosStatus);
                
                this.add(lblSunDistance);
                this.add(sunDistanceStatus);
		
		this.add(lblCurrTime);
		this.add(currTimeStatus);
		
		this.add(lblGs);
		this.add(gsStatus);
		
		this.add(lblTimeStep);
		this.add(timeStepStatus);
	}
	
	public void init() {
		this.sunPosStatus.setText("0");
                this.orbitalPosStatus.setText("0");
                this.sunDistanceStatus.setText("0");
		this.currTimeStatus.setText("0");
		this.gsStatus.setText("0");
		this.timeStepStatus.setText("0");
	}
	
	public void update(double sunPosition, double orbitalPosition, double sunDistance, int currentTime, int gs, int timeStep) {
		
		this.sunPosStatus.setText(String.format("%.1f", sunPosition));
                this.orbitalPosStatus.setText(String.format("%.1f", Math.toDegrees(orbitalPosition) % 360.0));             
                this.sunDistanceStatus.setText(String.format("%.1f", sunDistance));
		this.currTimeStatus.setText(Integer.toString(currentTime));
		this.gsStatus.setText(Integer.toString(gs));
		this.timeStepStatus.setText(Integer.toString(timeStep));
		
		this.validate();
	}
}
