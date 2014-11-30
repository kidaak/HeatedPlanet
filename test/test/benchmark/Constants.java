package test.benchmark;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Calendar;

import common.EarthGridProperties;
import common.EarthGridProperties.EarthGridProperty;

public class Constants {
	//Actual Constants
	public static final int KB = 1024;
	public static final int MB = 1024*1024;
	public static final int GB = 1024*1024*1024;
	public static final String DATABASE_FILE = System.getProperty("user.home")+"/PlanetSim.mv.db";
	
	//Default Values
	private static final int GRIDSPACING = 15;
	private static final int SIMULATIONTIMESTEP = 1440;
	private static final int SIMULATIONLENGTH = 12;
	private static final int PRECISION = 6;
	private static final int GEOPRECISION = 100;
	private static final int TIMEPRECISION = 100;
	private static final double AXIALTILT = 23.44;
	private static final double ECCENTRICITY = 0.0167;
	private static final double PRESENTATIONRATE = 0.01;
	private static final Calendar STARTDATE = Calendar.getInstance();
	private static final Calendar ENDDATE = Calendar.getInstance();
	
	//Benchmarking parameters
	public static final boolean showPanelsForTesting = false;
	public static final int numberOfBenchmarkRounds = 15;
	
	//Static adjustments
	static{
		ENDDATE.add(Calendar.MONTH, SIMULATIONLENGTH);
	}
	
	//Test Configurations
	/*
	 * PR = Precision
	 * GP = Geographic Precision
	 * TP = Time/Temporal Precision
	 */
	public static final EarthGridProperties args6PR_100GP_100TP = createDefault("default");
	public static final EarthGridProperties argsBaseline10 = createDefault("10");
	public static final EarthGridProperties argsBaseline100 = createDefault("100");
	public static final EarthGridProperties argsBaseline1000 = createDefault("1000");
	
	// Precision = 0
	public static final EarthGridProperties args0PR_1GP_1TP = createVariableTrifecta("args0PR_1GP_1TP",0,1,1);
	public static final EarthGridProperties args0PR_1GP_5TP = createVariableTrifecta("args0PR_1GP_5TP",0,1,5);
	public static final EarthGridProperties args0PR_1GP_10TP = createVariableTrifecta("args0PR_1GP_10TP",0,1,10);
	public static final EarthGridProperties args0PR_1GP_50TP = createVariableTrifecta("args0PR_1GP_50TP",0,1,50);
	public static final EarthGridProperties args0PR_1GP_100TP = createVariableTrifecta("args0PR_1GP_100TP",0,1,100);
	
	public static final EarthGridProperties args0PR_5GP_1TP = createVariableTrifecta("args0PR_5GP_1TP",0,5,1);
	public static final EarthGridProperties args0PR_5GP_5TP = createVariableTrifecta("args0PR_5GP_5TP",0,5,5);
	public static final EarthGridProperties args0PR_5GP_10TP = createVariableTrifecta("args0PR_5GP_10TP",0,5,10);
	public static final EarthGridProperties args0PR_5GP_50TP = createVariableTrifecta("args0PR_5GP_50TP",0,5,50);
	public static final EarthGridProperties args0PR_5GP_100TP = createVariableTrifecta("args0PR_5GP_100TP",0,5,100);
	
	public static final EarthGridProperties args0PR_10GP_1TP = createVariableTrifecta("args0PR_10GP_1TP",0,10,1);
	public static final EarthGridProperties args0PR_10GP_5TP = createVariableTrifecta("args0PR_10GP_5TP",0,10,5);
	public static final EarthGridProperties args0PR_10GP_10TP = createVariableTrifecta("args0PR_10GP_10TP",0,10,10);
	public static final EarthGridProperties args0PR_10GP_50TP = createVariableTrifecta("args0PR_10GP_50TP",0,10,50);
	public static final EarthGridProperties args0PR_10GP_100TP = createVariableTrifecta("args0PR_10GP_100TP",0,10,100);
	
	public static final EarthGridProperties args0PR_50GP_1TP = createVariableTrifecta("args0PR_50GP_1TP",0,50,1);
	public static final EarthGridProperties args0PR_50GP_5TP = createVariableTrifecta("args0PR_50GP_5TP",0,50,5);
	public static final EarthGridProperties args0PR_50GP_10TP = createVariableTrifecta("args0PR_50GP_10TP",0,50,10);
	public static final EarthGridProperties args0PR_50GP_50TP = createVariableTrifecta("args0PR_50GP_50TP",0,50,50);
	public static final EarthGridProperties args0PR_50GP_100TP = createVariableTrifecta("args0PR_50GP_100TP",0,50,100);
	
	public static final EarthGridProperties args0PR_100GP_1TP = createVariableTrifecta("args0PR_100GP_1TP",0,100,1);
	public static final EarthGridProperties args0PR_100GP_5TP = createVariableTrifecta("args0PR_100GP_5TP",0,100,5);
	public static final EarthGridProperties args0PR_100GP_10TP = createVariableTrifecta("args0PR_100GP_10TP",0,100,10);
	public static final EarthGridProperties args0PR_100GP_50TP = createVariableTrifecta("args0PR_100GP_50TP",0,100,50);
	public static final EarthGridProperties args0PR_100GP_100TP = createVariableTrifecta("args0PR_100GP_100TP",0,100,100);
	
// Precision = 5
	public static final EarthGridProperties args5PR_1GP_1TP = createVariableTrifecta("args5PR_1GP_1TP",5,1,1);
	public static final EarthGridProperties args5PR_1GP_5TP = createVariableTrifecta("args5PR_1GP_5TP",5,1,5);
	public static final EarthGridProperties args5PR_1GP_10TP = createVariableTrifecta("args5PR_1GP_10TP",5,1,10);
	public static final EarthGridProperties args5PR_1GP_50TP = createVariableTrifecta("args5PR_1GP_50TP",5,1,50);
	public static final EarthGridProperties args5PR_1GP_100TP = createVariableTrifecta("args5PR_1GP_100TP",5,1,100);
	
	public static final EarthGridProperties args5PR_5GP_1TP = createVariableTrifecta("args5PR_5GP_1TP",5,5,1);
	public static final EarthGridProperties args5PR_5GP_5TP = createVariableTrifecta("args5PR_5GP_5TP",5,5,5);
	public static final EarthGridProperties args5PR_5GP_10TP = createVariableTrifecta("args5PR_5GP_10TP",5,5,10);
	public static final EarthGridProperties args5PR_5GP_50TP = createVariableTrifecta("args5PR_5GP_50TP",5,5,50);
	public static final EarthGridProperties args5PR_5GP_100TP = createVariableTrifecta("args5PR_5GP_100TP",5,5,100);
	
	public static final EarthGridProperties args5PR_10GP_1TP = createVariableTrifecta("args5PR_10GP_1TP",5,10,1);
	public static final EarthGridProperties args5PR_10GP_5TP = createVariableTrifecta("args5PR_10GP_5TP",5,10,5);
	public static final EarthGridProperties args5PR_10GP_10TP = createVariableTrifecta("args5PR_10GP_10TP",5,10,10);
	public static final EarthGridProperties args5PR_10GP_50TP = createVariableTrifecta("args5PR_10GP_50TP",5,10,50);
	public static final EarthGridProperties args5PR_10GP_100TP = createVariableTrifecta("args5PR_10GP_100TP",5,10,100);
	
	public static final EarthGridProperties args5PR_50GP_1TP = createVariableTrifecta("args5PR_50GP_1TP",5,50,1);
	public static final EarthGridProperties args5PR_50GP_5TP = createVariableTrifecta("args5PR_50GP_5TP",5,50,5);
	public static final EarthGridProperties args5PR_50GP_10TP = createVariableTrifecta("args5PR_50GP_10TP",5,50,10);
	public static final EarthGridProperties args5PR_50GP_50TP = createVariableTrifecta("args5PR_50GP_50TP",5,50,50);
	public static final EarthGridProperties args5PR_50GP_100TP = createVariableTrifecta("args5PR_50GP_100TP",5,50,100);
	
	public static final EarthGridProperties args5PR_100GP_1TP = createVariableTrifecta("args5PR_100GP_1TP",5,100,1);
	public static final EarthGridProperties args5PR_100GP_5TP = createVariableTrifecta("args5PR_100GP_5TP",5,100,5);
	public static final EarthGridProperties args5PR_100GP_10TP = createVariableTrifecta("args5PR_100GP_10TP",5,100,10);
	public static final EarthGridProperties args5PR_100GP_50TP = createVariableTrifecta("args5PR_100GP_50TP",5,100,50);
	public static final EarthGridProperties args5PR_100GP_100TP = createVariableTrifecta("args5PR_100GP_100TP",5,100,100);
		
// Precision = 10
	public static final EarthGridProperties args10PR_1GP_1TP = createVariableTrifecta("args10PR_1GP_1TP",10,1,1);
	public static final EarthGridProperties args10PR_1GP_5TP = createVariableTrifecta("args10PR_1GP_5TP",10,1,5);
	public static final EarthGridProperties args10PR_1GP_10TP = createVariableTrifecta("args10PR_1GP_10TP",10,1,10);
	public static final EarthGridProperties args10PR_1GP_50TP = createVariableTrifecta("args10PR_1GP_50TP",10,1,50);
	public static final EarthGridProperties args10PR_1GP_100TP = createVariableTrifecta("args10PR_1GP_100TP",10,1,100);
	
	public static final EarthGridProperties args10PR_5GP_1TP = createVariableTrifecta("args10PR_5GP_1TP",10,5,1);
	public static final EarthGridProperties args10PR_5GP_5TP = createVariableTrifecta("args10PR_5GP_5TP",10,5,5);
	public static final EarthGridProperties args10PR_5GP_10TP = createVariableTrifecta("args10PR_5GP_10TP",10,5,10);
	public static final EarthGridProperties args10PR_5GP_50TP = createVariableTrifecta("args10PR_5GP_50TP",10,5,50);
	public static final EarthGridProperties args10PR_5GP_100TP = createVariableTrifecta("args10PR_5GP_100TP",10,5,100);
	
	public static final EarthGridProperties args10PR_10GP_1TP = createVariableTrifecta("args10PR_10GP_1TP",10,10,1);
	public static final EarthGridProperties args10PR_10GP_5TP = createVariableTrifecta("args10PR_10GP_5TP",10,10,5);
	public static final EarthGridProperties args10PR_10GP_10TP = createVariableTrifecta("args10PR_10GP_10TP",10,10,10);
	public static final EarthGridProperties args10PR_10GP_50TP = createVariableTrifecta("args10PR_10GP_50TP",10,10,50);
	public static final EarthGridProperties args10PR_10GP_100TP = createVariableTrifecta("args10PR_10GP_100TP",10,10,100);
	
	public static final EarthGridProperties args10PR_50GP_1TP = createVariableTrifecta("args10PR_50GP_1TP",10,50,1);
	public static final EarthGridProperties args10PR_50GP_5TP = createVariableTrifecta("args10PR_50GP_5TP",10,50,5);
	public static final EarthGridProperties args10PR_50GP_10TP = createVariableTrifecta("args10PR_50GP_10TP",10,50,10);
	public static final EarthGridProperties args10PR_50GP_50TP = createVariableTrifecta("args10PR_50GP_50TP",10,50,50);
	public static final EarthGridProperties args10PR_50GP_100TP = createVariableTrifecta("args10PR_50GP_100TP",10,50,100);
	
	public static final EarthGridProperties args10PR_100GP_1TP = createVariableTrifecta("args10PR_100GP_1TP",10,100,1);
	public static final EarthGridProperties args10PR_100GP_5TP = createVariableTrifecta("args10PR_100GP_5TP",10,100,5);
	public static final EarthGridProperties args10PR_100GP_10TP = createVariableTrifecta("args10PR_100GP_10TP",10,100,10);
	public static final EarthGridProperties args10PR_100GP_50TP = createVariableTrifecta("args10PR_100GP_50TP",10,100,50);
	public static final EarthGridProperties args10PR_100GP_100TP = createVariableTrifecta("args10PR_100GP_100TP",10,100,100);
		
// Precision = 15	
	public static final EarthGridProperties args15PR_1GP_1TP = createVariableTrifecta("args15PR_1GP_1TP",15,1,1);
	public static final EarthGridProperties args15PR_1GP_5TP = createVariableTrifecta("args15PR_1GP_5TP",15,1,5);
	public static final EarthGridProperties args15PR_1GP_10TP = createVariableTrifecta("args15PR_1GP_10TP",15,1,10);
	public static final EarthGridProperties args15PR_1GP_50TP = createVariableTrifecta("args15PR_1GP_50TP",15,1,50);
	public static final EarthGridProperties args15PR_1GP_100TP = createVariableTrifecta("args15PR_1GP_100TP",15,1,100);
	
	public static final EarthGridProperties args15PR_5GP_1TP = createVariableTrifecta("args15PR_5GP_1TP",15,5,1);
	public static final EarthGridProperties args15PR_5GP_5TP = createVariableTrifecta("args15PR_5GP_5TP",15,5,5);
	public static final EarthGridProperties args15PR_5GP_10TP = createVariableTrifecta("args15PR_5GP_10TP",15,5,10);
	public static final EarthGridProperties args15PR_5GP_50TP = createVariableTrifecta("args15PR_5GP_50TP",15,5,50);
	public static final EarthGridProperties args15PR_5GP_100TP = createVariableTrifecta("args15PR_5GP_100TP",15,5,100);
	
	public static final EarthGridProperties args15PR_10GP_1TP = createVariableTrifecta("args15PR_10GP_1TP",15,10,1);
	public static final EarthGridProperties args15PR_10GP_5TP = createVariableTrifecta("args15PR_10GP_5TP",15,10,5);
	public static final EarthGridProperties args15PR_10GP_10TP = createVariableTrifecta("args15PR_10GP_10TP",15,10,10);
	public static final EarthGridProperties args15PR_10GP_50TP = createVariableTrifecta("args15PR_10GP_50TP",15,10,50);
	public static final EarthGridProperties args15PR_10GP_100TP = createVariableTrifecta("args15PR_10GP_100TP",15,10,100);
	
	public static final EarthGridProperties args15PR_50GP_1TP = createVariableTrifecta("args15PR_50GP_1TP",15,50,1);
	public static final EarthGridProperties args15PR_50GP_5TP = createVariableTrifecta("args15PR_50GP_5TP",15,50,5);
	public static final EarthGridProperties args15PR_50GP_10TP = createVariableTrifecta("args15PR_50GP_10TP",15,50,10);
	public static final EarthGridProperties args15PR_50GP_50TP = createVariableTrifecta("args15PR_50GP_50TP",15,50,50);
	public static final EarthGridProperties args15PR_50GP_100TP = createVariableTrifecta("args15PR_50GP_100TP",15,50,100);
	
	public static final EarthGridProperties args15PR_100GP_1TP = createVariableTrifecta("args15PR_100GP_1TP",15,100,1);
	public static final EarthGridProperties args15PR_100GP_5TP = createVariableTrifecta("args15PR_100GP_5TP",15,100,5);
	public static final EarthGridProperties args15PR_100GP_10TP = createVariableTrifecta("args15PR_100GP_10TP",15,100,10);
	public static final EarthGridProperties args15PR_100GP_50TP = createVariableTrifecta("args15PR_100GP_50TP",15,100,50);
	public static final EarthGridProperties args15PR_100GP_100TP = createVariableTrifecta("args15PR_100GP_100TP",15,100,100);
	
	
	
	//Ease of use methods to create EarthGridProperties
	private static EarthGridProperties createVariableTrifecta(String testName, int precision, int geoPrecision, int timePrecision){
		return configureProperties(testName, GRIDSPACING, SIMULATIONTIMESTEP, SIMULATIONLENGTH, precision, geoPrecision,
				timePrecision, AXIALTILT, ECCENTRICITY, PRESENTATIONRATE, STARTDATE,ENDDATE);
	}
	
	private static EarthGridProperties createDefault(String testName){
		return configureProperties(testName, GRIDSPACING, SIMULATIONTIMESTEP, SIMULATIONLENGTH, PRECISION, GEOPRECISION,
				TIMEPRECISION, AXIALTILT, ECCENTRICITY, PRESENTATIONRATE, STARTDATE,ENDDATE);
	}
	
	private static EarthGridProperties createVariableGeographic(String testName, int geoPrecision){
		return configureProperties(testName, GRIDSPACING, SIMULATIONTIMESTEP, SIMULATIONLENGTH, PRECISION, geoPrecision,
				TIMEPRECISION, AXIALTILT, ECCENTRICITY, PRESENTATIONRATE, STARTDATE,ENDDATE);
	}
	
	private static EarthGridProperties createVariableTemporal(String testName, int timePrecision){
		return configureProperties(testName, GRIDSPACING, SIMULATIONTIMESTEP, SIMULATIONLENGTH, PRECISION, GEOPRECISION,
				timePrecision, AXIALTILT, ECCENTRICITY, PRESENTATIONRATE, STARTDATE,ENDDATE);
	}
	
	private static EarthGridProperties createVariablePrecision(String testName, int precision){
		return configureProperties(testName, GRIDSPACING, SIMULATIONTIMESTEP, SIMULATIONLENGTH, precision, GEOPRECISION,
				TIMEPRECISION, AXIALTILT, ECCENTRICITY, PRESENTATIONRATE, STARTDATE,ENDDATE);
	}
	
	private static EarthGridProperties configureProperties(String n, int gs, int sts, int sl, int p, int gp, int tp,
			double at, double ec, double pr, Calendar sd, Calendar ed){
		EarthGridProperties egp = new EarthGridProperties();
		egp.setProperty(EarthGridProperty.NAME, n);
		egp.setProperty(EarthGridProperty.GRID_SPACING, gs);
		egp.setProperty(EarthGridProperty.SIMULATION_TIME_STEP, sts);
		egp.setProperty(EarthGridProperty.SIMULATION_LENGTH, sl);
		egp.setProperty(EarthGridProperty.PRECISION, p);
		egp.setProperty(EarthGridProperty.GEO_PRECISION, gp);
		egp.setProperty(EarthGridProperty.TIME_PRECISION, tp);
		egp.setProperty(EarthGridProperty.AXIAL_TILT, at);
		egp.setProperty(EarthGridProperty.ECCENTRICITY, ec);
		egp.setProperty(EarthGridProperty.PRESENTATION_RATE, pr);
		egp.setProperty(EarthGridProperty.START_DATE, sd);
		egp.setProperty(EarthGridProperty.END_DATE, ed);
		ed.add(Calendar.MONTH, 12);
		return egp;
	}
	
	protected static final PrintStream outNull = new PrintStream(new OutputStream() {
        public void write(int b) {
            //DO NOTHING
        }
    });
}
