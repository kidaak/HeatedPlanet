// Demo.java
package PlanetSim;

import view.query.QueryDialog;

public class Demo {
	final private int FLOAT_DECIMAL_DIGITS = 6;
	final private int DOUBLE_DECIMAL_DIGITS = 15;
	private int precisionDigits = FLOAT_DECIMAL_DIGITS; // digits stored after decimal
	private int geographicAccuracy = 100; // integer percentage
	private int temporalAccuracy = 100; // integer percentage
	private QueryDialog queryui;
	private ControllerGUI ui;

	public static void main(String[] args) {
		Demo demo = new Demo();
		demo.processArgs(args);
		try{
			demo.run();
		}catch(ExceptionInInitializerError e){
			//JOptionPane.showMessageDialog(null, "The Database is in use by another application. Please close that application and restart PlanetSim.Demo.");
			return;
		}
	}

	private Demo() {
		// empty
	}

	// Note: processArgs ignore args that are not s,p,r,t or b as long as you
	// provide a max of 5 input values.
	private void processArgs(String[] args) {
		
		if (args.length > 6)
			usage();

		for (int i = 0; i < args.length; i++) {
			
			String arg = args[i];

			if ("-p".equalsIgnoreCase(arg)) {
				i++;
				if (i >= args.length) {
					System.out.println("-p needs a value.");
					usage();
				}
				
				String s = args[i];
				
				try {
					precisionDigits = Integer.parseInt(s);
					if(precisionDigits < 0 || precisionDigits > DOUBLE_DECIMAL_DIGITS) {
						throw new NumberFormatException();
					}
				} catch (NumberFormatException nfe) {
					System.out.println("Error reading -p value. Please retry.");
					usage();
				}
			}
			else if ("-g".equalsIgnoreCase(arg)) {
				i++;
				if (i >= args.length) {
					System.out.println("-g needs a value.");
					usage();
				}
				
				String s = args[i];
				
				try {
					geographicAccuracy = Integer.parseInt(s);
					if(geographicAccuracy <= 0 || geographicAccuracy > 100) {
						throw new NumberFormatException();
					}
				} catch (NumberFormatException nfe) {
					System.out.println("Error reading -g value. Please retry.");
					usage();
				}
			}
			else if ("-t".equalsIgnoreCase(arg)) {
				i++;
				if (i >= args.length) {
					System.out.println("-t needs a value.");
					usage();
				}
				
				String s = args[i];
				
				try {
					temporalAccuracy = Integer.parseInt(s);
					if(temporalAccuracy <= 0 || temporalAccuracy > 100) {
						throw new NumberFormatException();
					}
				} catch (NumberFormatException nfe) {
					System.out.println("Error reading -t value. Please retry.");
					usage();
				}
			} 
			else
				usage();
		}
		
	}

	private void usage() {
		System.out.println("Usage: java PlanetSim.Demo [-p #] [-g #] [-t #]");
		System.exit(-1);
	}

	private void run() {
		debug("Demo started with settings:");
		printSettings();
		createAndShowUI();
		debug("Demo running...");
	}

	private void createAndShowUI() {
		queryui = new QueryDialog(null, false, this);
		ui = new ControllerGUI(precisionDigits, geographicAccuracy, temporalAccuracy, this);
		viewQueryGUI();
	}

	public void viewQueryGUI() {
		// call to make queryGUI main control area
		ui.setVisible(false);
		queryui.setEnabled(true);
		queryui.updateSimList();
		queryui.setVisible(true);
	}
	
	public void viewSimGUI() {
		// disable input on query
		queryui.setEnabled(false);
		queryui.setVisible(false);
		
		// activate sim
		ui.setVisible(true);
	}
	
	private void printSettings() {
		
		debug("Precision Digits\t:" + precisionDigits);
		debug("Geographic Accuracy:\t:" + geographicAccuracy);
		debug("Temporal Accuracy:\t:" + temporalAccuracy);
		debug("");
	}

	private void debug(String s) {
		System.out.println(s);
	}
}
