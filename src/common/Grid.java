package common;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class Grid implements IGrid, Serializable {
	
	static final long serialVersionUID = -7894194241L;
	
	// Used to transport the temps in the buffer
	private final int sunPosition, time, width, height, timestep;
	private final float sunPositionDeg;
	private final float sunLatitudeDeg;
	private final float distanceFromSun;
	private final float orbitalAngle;
	
	// We use a TreeMap to not consume a contiguous amount of memory. It's
	// backed by a Red/Black Tree, so we get pretty decent access times
	private final Map<Integer, Float> grid;

	public Grid(int sunPosition, float sunPositionDeg, int time, int timestep, int width, int height, float sunLatitudeDeg, float distanceFromSun,float orbitalAngle) {
		this.timestep = timestep;
		this.sunPosition = sunPosition;
		this.sunPositionDeg = sunPositionDeg;
		this.time = time;
		this.width = width;
		this.height = height;
		this.sunLatitudeDeg = sunLatitudeDeg;
		this.distanceFromSun = distanceFromSun;
		this.orbitalAngle = orbitalAngle;

		grid = new TreeMap<Integer, Float>();
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
		this.grid = new TreeMap<Integer, Float>(toCopy.grid);
	}

	public int getSunPosition(){
		return this.sunPosition;
	}
	
	public int getTime(){
		return this.time;
	}
	@Override
	public void setTemperature(int x, int y, float temp) {
		if (y > height || x > width || x < 0 || y < 0)
			throw new IllegalArgumentException("index (" + x + ", " + y + ") out of bounds");
		
		grid.put(y * width + x, temp);
	}

	@Override
	public float getTemperature(int x, int y) {
		if (y >= height || x >= width || x < 0 || y < 0)
			throw new IllegalArgumentException("index (" + x + ", " + y + ") out of bounds");
		
		return grid.get(y * width + x);
	}
	

	@Override
	public float getSunPositionDeg() {
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
	
	public float getSunLatitudeDeg(){
		return this.sunLatitudeDeg;
	}
	
	public float getDistanceFromSun(){
		return this.distanceFromSun;
	}
	
	public float getOrbitalAngle(){
		return this.orbitalAngle;
	}
	
	public int getTimeStep(){
		return this.timestep;
	}

}
