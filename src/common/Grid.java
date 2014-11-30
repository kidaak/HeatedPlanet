package common;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class Grid implements IGrid, Serializable, Comparable<Grid> {
	
	static final long serialVersionUID = -7894194242L;
	
	// Used to transport the temps in the buffer
	private final int sunPosition, time, width, height, timestep;
	private final double sunPositionDeg;
	private final double sunLatitudeDeg;
	private final double distanceFromSun;
	private final double orbitalAngle;
	
	//Variables for Fitting Grids
	private final int size;
	private final double[] coef;
	private final double[] dCoef;
	private double stepRate = 1;
	private double numberOfIterations = 50;
	
	private double[][] grid; 

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
		this.size = 16;
		this.coef = new double[size];
		this.dCoef = new double[size];
		grid = new double[width][height];
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
		// copy 2d array
		this.grid = new double[toCopy.grid.length][];
		for(int i = 0; i < toCopy.grid.length; i++)
			this.grid[i] = toCopy.grid[i].clone();
		
		this.size = 16;
		this.coef = new double[size];
		this.dCoef = new double[size];
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
		
		grid[x][y] = temp;
	}

	@Override
	public double getTemperature(int x, int y) {
		if (y >= height || x >= width || x < 0 || y < 0)
			throw new IllegalArgumentException("index (" + x + ", " + y + ") out of bounds");
		
		return grid[x][y];
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
	
	public double getRSquared(){
		fitCoef();
		double sv = getSquaredVariation();
		double rv = 0.0;
		for(int i = 0; i < width; i++){
			for( int j = 0; j < height; j++){
				rv += Math.pow(getTemperature(i, j)-pred(i,j), 2);
			}
		}
		return 1 - rv/sv;
	}
	
	private void fitCoef(){
		double averageTemp = getAverageTemperature();
		for(int k = 0; k <= numberOfIterations; k++){
			
			for(int n = 0; n < dCoef.length; n++){
				dCoef[n] = 0.0;
			}
			
			for(int i = 0; i < width; i++){
				for( int j = 0; j < height; j++){
					double dy = pred(i,j)-(getTemperature(i, j)-averageTemp);
					double[] x = getVars(i, j);
					for(int n = 0; n < dCoef.length; n++){
						dCoef[n] += dy*x[n];
					}
				}
			}
			
			for(int n = 0; n < dCoef.length; n++){
				coef[n] -= stepRate*dCoef[n]/(height*width);
				//System.out.print(coef[n]+", ");
			}
			//System.out.println("");
			
		}
	}
	
	private double pred(int i, int j){
		double prediction = 0;
		double[] x = getVars(i,j);
		for(int k = 0; k < coef.length; k++){
			prediction += coef[k]*x[k];
		}
		return prediction;
	}
	
	private double[] getVars(int i, int j){
		

		double theta = (i-sunPosition)+(180.0/height)/2.0;
		double phi = j+180.0/height/2.0;
		double z = Math.sin(phi); //z
		double x = Math.cos(phi)*Math.cos(theta); 
		double y = Math.cos(phi)*Math.sin(theta);
		
		//l = 0
		double[] var = new double[16];
		var[0] = 0;
		
		//l = 1\
		var[1] = y;
		var[2] = x;
		var[3] = z;
		
		//l = 2
		var[4] = x*y;
		var[5] = y*z;
		var[6] = z*x;
		var[7] = 2*z*z-x*x-y*y;
		var[8] = x*x-y*y;
		
		
		//l = 3
		var[9] = (3*x*x-y*y)*y;
		var[10] = x*y*z;
		var[11] = y*(4*z*z-x*x-y*y);
		var[12] = z*(2*z*z-3*x*x-3*y*y);
		var[13] = x*(4*z*z-x*x-y*y);
		var[14] = z*(x*x-y*y);
		var[15] = x*(x*x-3*y*y);
		
		
		return var;
	}
	
	private double getAverageTemperature(){
		double temp = 0.0;
		
		for(int i = 0; i < width; i++){
			for( int j = 0; j < height; j++){
				temp += getTemperature(i, j);
			}
		}
		temp = temp/(height*width);
		return temp;
	}
	
	private double getSquaredVariation(){
		double averageTemperature = getAverageTemperature();
		double sqrVar = 0.0;
		
		for(int i = 0; i < width; i++){
			for( int j = 0; j < height; j++){
				sqrVar += Math.pow(getTemperature(i, j)-averageTemperature,2);
			}
		}
		return sqrVar;
	}
	

}
