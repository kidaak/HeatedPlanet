package persistenceManager;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import common.EarthGridProperties;
import common.IGrid;
import common.EarthGridProperties.EarthGridProperty;

public class QueryCalculator {
	private EarthGridProperties simProp;
	private ArrayList<IGrid> grid;
	private boolean doMin;
	private boolean doMax;
	private boolean doAvgAcrossTime;
	private boolean doAvgAcrossGrid;
	private boolean doAllData;
	private float minlat, maxlat, minlon, maxlon;
	
	public void setDoAvgAcrossTime(boolean doAvgAcrossTime) {
		this.doAvgAcrossTime = doAvgAcrossTime;
	}

	public void setDoAvgAcrossGrid(boolean doAvgAcrossGrid) {
		this.doAvgAcrossGrid = doAvgAcrossGrid;
	}

	public void setDoAllData(boolean doAllData) {
		this.doAllData = doAllData;
	}

	public void setGrid(ArrayList<IGrid> grid) {
		this.grid = grid;
	}

	public void setSimProp(EarthGridProperties simProp) {
		this.simProp = simProp;
	}
	
	public void setDoMin(boolean doMin) {
		this.doMin = doMin;
	}

	public void setDoMax(boolean doMax) {
		this.doMax = doMax;
	}
	
	public void setLocation(float minlat, float maxlat, float minlon, float maxlon) {
		this.minlat = minlat;
		this.maxlat = maxlat;
		this.minlon = minlon;
		this.maxlon = maxlon;
	}
	
	public String getOutputText() {
		StringWriter buf = new StringWriter();
		PrintWriter output = new PrintWriter(buf);

		// Standard output for all queries
		output.println("================================");
		output.println(" Query Returned Simulation Info ");
		output.println("================================");
		output.println(simProp);
				
		// Calculate params for region restriction
		int gridH = grid.get(0).getGridHeight();
		int gridW = grid.get(0).getGridWidth();
		double latPerGrid = 180.0f / gridH;
		double lonPerGrid = 360.0f / gridW;
		int minLatIdx = (int) Math.ceil((minlat+90) / latPerGrid);
		int maxLatIdx = (int) Math.floor((maxlat+90) / latPerGrid);
		int minLonIdx = (int) Math.ceil((minlon+180) / lonPerGrid);
		int maxLonIdx = (int) Math.floor((maxlon+180) / lonPerGrid);
		int numLatStep = Math.max(0, maxLatIdx-minLatIdx);
		int numLonStep = Math.max(0, maxLonIdx-minLonIdx);
		
		// min stat variables
		Double minTemp = Double.POSITIVE_INFINITY;
		float minTlat = 0;
		float minTlon = 0;
		int minTimeIdx = 0;
		// max stat variables
		Double maxTemp = Double.NEGATIVE_INFINITY;
		float maxTlat = 0;
		float maxTlon = 0;
		int maxTimeIdx = 0;
		// avg over region variables
		double[] regionAvg = new double[grid.size()]; //NOTE: primitives init to zero
		// avg over time variable
		double[][] timeAvg = new double[numLonStep][numLatStep]; //NOTE: primitives init to zero
		// find and record min temp
		for(int tIdx=0; tIdx < grid.size(); tIdx++) {
			IGrid g = grid.get(tIdx);
			//Restrict search
			for(int lonIdx = minLonIdx; lonIdx < maxLonIdx; lonIdx++) {
				for(int latIdx = minLatIdx; latIdx < maxLatIdx; latIdx++) {
					double gridTemp = g.getTemperature(lonIdx, latIdx);
					if(doMin && (gridTemp < minTemp)) {
						minTemp = gridTemp;
						minTlat = latIdx;
						minTlon = lonIdx;
						minTimeIdx = tIdx;
					}
					if(doMax && (gridTemp > maxTemp)) {
						maxTemp = gridTemp;
						maxTlat = latIdx;
						maxTlon = lonIdx;
						maxTimeIdx = tIdx;
					}
					if(doAvgAcrossGrid) {
						// Store avg temp across region for each time step
						// For now just sum temps, and then do division at the end
						regionAvg[tIdx] += gridTemp;
					}
					if(doAvgAcrossTime) {
						timeAvg[lonIdx-minLonIdx][latIdx-minLatIdx] += gridTemp;
					}
				}
			}
			
			// Final calculations (as required)
			regionAvg[tIdx] = regionAvg[tIdx] / (numLonStep*numLatStep); //normalize
		}
		
		SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		dateFmt.setTimeZone(TimeZone.getTimeZone("UTC"));

		// Do actual printing of results
		if(doMin) {
			double latVal = minTlat * latPerGrid - 90;
			double lonVal = minTlon * lonPerGrid - 180;
			Calendar date = getFirstGridDate();
			date.add(Calendar.MINUTE, Math.round(minTimeIdx * simProp.getPropertyInt(EarthGridProperty.SIMULATION_TIME_STEP)));
			output.printf("Minimum Temperature: %.1f at (%.1f,%.1f) on %s\n", minTemp, latVal, lonVal, dateFmt.format(date.getTime()));
		}
		
		if(doMax) {
			double latVal = maxTlat * latPerGrid - 90;
			double lonVal = maxTlon * lonPerGrid - 180;
			Calendar date = getFirstGridDate();
			date.add(Calendar.MINUTE, Math.round(maxTimeIdx * simProp.getPropertyInt(EarthGridProperty.SIMULATION_TIME_STEP)));
			output.printf("Maximum Temperature: %.1f at (%.1f,%.1f) on %s\n", maxTemp, latVal, lonVal, dateFmt.format(date.getTime()));
		}

		if(doAvgAcrossGrid) {
			// Print avg temp at each time
			Calendar date = getFirstGridDate();
			output.printf("===================================\n");
			output.printf(" Average Region Temps at Each Time \n");
			output.printf("===================================\n");
			for(int tIdx=0; tIdx < regionAvg.length; tIdx++) {
				output.printf("%s - %.1f\n", dateFmt.format(date.getTime()), regionAvg[tIdx]);
				date.add(Calendar.MINUTE, simProp.getPropertyInt(EarthGridProperty.SIMULATION_TIME_STEP));
			}
		}
		
		if(doAvgAcrossTime) {
			output.printf("============================================\n");
			output.printf(" Average Temps at Each Location Across Time \n");
			output.printf("============================================\n");
			for(int y=numLatStep-1; y >=0; y--) {
				for(int x=0; x < numLonStep; x++) {
					// normalize each grid temp
					timeAvg[x][y] = timeAvg[x][y] / grid.size();
					output.printf("%7.1f  ", timeAvg[x][y]);
				}
				output.printf("\n");
			}
		}
		
		if(doAllData) {
			output.printf("==================================\n");
			output.printf(" Grid Temperatures for Every Grid \n");
			output.printf("==================================\n");
			Calendar date = getFirstGridDate();
			for(int tIdx=0; tIdx < regionAvg.length; tIdx++) {
				output.printf("------------------\n");
				output.printf(" %s\n", dateFmt.format(date.getTime()));
				output.printf("------------------\n");
				IGrid g = grid.get(tIdx);
				for(int y=numLatStep-1; y >=0; y--) {
					for(int x=0; x < numLonStep; x++) {
						output.printf("%7.1f  ", g.getTemperature(x, y));
					}
					output.printf("\n");
				}
				date.add(Calendar.MINUTE, simProp.getPropertyInt(EarthGridProperty.SIMULATION_TIME_STEP));
			}
		}
		return buf.toString();
	}

	Calendar getFirstGridDate() {
		// Returns a the date/time of the first valid grid provided.
		// It is assumed that all simulations start at the same time and given
		// a fixed simulation step, one can determine the first grid date as
		// the first grid beyond the provided START_DATE
		Calendar date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		date.clear();
		date.set(2014, 0, 4, 12, 0);
		
		Calendar searchStart = simProp.getPropertyCalendar(EarthGridProperty.START_DATE);
		int simTimeStep = simProp.getPropertyInt(EarthGridProperty.SIMULATION_TIME_STEP);
		while(date.getTimeInMillis() < searchStart.getTimeInMillis()) {
			date.add(Calendar.MINUTE, simTimeStep);
		}
		SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		dateFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
		System.out.println(dateFmt.format(searchStart.getTime()));
		System.out.println(dateFmt.format(date.getTime()));
		return date;
	}
}
