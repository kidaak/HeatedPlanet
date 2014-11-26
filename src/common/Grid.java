package common;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class Grid implements IGrid, Serializable, Comparable<Grid> {
	
	static final long serialVersionUID = -7894194241L;
	
	// Used to transport the temps in the buffer
	private final int sunPosition, time, width, height, timestep;
	private final double sunPositionDeg;
	private final double sunLatitudeDeg;
	private final double distanceFromSun;
	private final double orbitalAngle;
	
	// We use a TreeMap to not consume a contiguous amount of memory. It's
	// backed by a Red/Black Tree, so we get pretty decent access times
	private final Map<Integer, Double> grid;

	public Grid(int sunPosition, double sunPositionDeg, int time, int timestep, int width, int height, double sunLatitudeDeg, double distanceFromSun,double orbitalAngle) {
		this.timestep = timestep;
		this.sunPosition = sunPosition;
		this.sunPositionDeg = sunPositionDeg;
		this.time = time;
		this.width = width;
		this.height = height;
		this.sunLatitudeDeg = sunLatitudeDeg;
		this.distanceFromSun = distanceFromSun;
		this.orbitalAngle = orbitalAngle;

		grid = new TreeMap<Integer, Double>();
	}
	
	public Grid(Grid toCopy) {
		
		this.sunPosition = toCopy.sunPosition;
		this.sunPositionDeg = toCopy.sunPositionDeg;
		this.time = toCopy.time;
		this.width = toCopy.width;
		this.height = toCopy.height;
		this.sunLatitudeDeg = toCopy.sunLatitudeDeg;
		this.distanceFromSun = toCopy.distanceFromSun;
		this.orbitalAngle = toCopy.orbitalAngle;
		this.timestep = toCopy.timestep;
		this.grid = new TreeMap<Integer, Double>(toCopy.grid);
	}

	@Override
	public int getSunPosition(){
		return this.sunPosition;
	}
	
	@Override
	public int getTime(){
		return this.time;
	}
	@Override
	public void setTemperature(int x, int y, double temp) {
		if (y > height || x > width || x < 0 || y < 0)
			throw new IllegalArgumentException("index (" + x + ", " + y + ") out of bounds");
		
		grid.put(y * width + x, temp);
	}

	@Override
	public double getTemperature(int x, int y) {
		if (y >= height || x >= width || x < 0 || y < 0)
			throw new IllegalArgumentException("index (" + x + ", " + y + ") out of bounds");
		
		return grid.get(y * width + x);
	}
	

	@Override
	public double getSunPositionDeg() {
		return this.sunPositionDeg;
	}
	
	@Override
	public int getCurrentTime() {
		return this.time;
	}

	@Override
	public int getGridWidth() {
		return this.width;
	}

	@Override
	public int getGridHeight() {
		return this.height;
	}
	
	@Override
	public double getSunLatitudeDeg(){
		return this.sunLatitudeDeg;
	}
	
	@Override
	public double getDistanceFromSun(){
		return this.distanceFromSun;
	}
	
	@Override
	public double getOrbitalAngle(){
		return this.orbitalAngle;
	}
	
	@Override
	public int getTimeStep(){
		return this.timestep;
	}

	@Override
	public int compareTo(Grid g) {
		return this.time-g.getTime();
	}

}
