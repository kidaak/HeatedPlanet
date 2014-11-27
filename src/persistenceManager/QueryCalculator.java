package persistenceManager;

import java.util.ArrayList;

import common.EarthGridProperties;
import common.IGrid;

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
		return new String("SAMPLE OUTPUT TEXT");
	}

}
