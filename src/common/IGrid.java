package common;

public interface IGrid {
	
	public void setTemperature(int x, int y, double temp);
	
	public double getTemperature(int x, int y);
	
	public float getSunPositionDeg();
	
	public int getCurrentTime();
	
	public int getGridWidth();
	
	public int getGridHeight();
	
	public float getSunLatitudeDeg();
	
	public float getOrbitalAngle();
	
	public float getDistanceFromSun();

}
