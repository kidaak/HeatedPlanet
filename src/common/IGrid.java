package common;

public interface IGrid {
	
	public void setTemperature(int x, int y, double temp);
	
	public double getTemperature(int x, int y);
	
	public double getSunPositionDeg();
	
	public int getCurrentTime();
	
	public int getGridWidth();
	
	public int getGridHeight();
	
	public double getSunLatitudeDeg();
	
	public double getOrbitalAngle();
	
	public double getDistanceFromSun();

}
