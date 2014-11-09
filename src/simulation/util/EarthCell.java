package simulation.util;

public interface EarthCell<T> extends Cell<T> {
	
public void setLatitude(int lat);
	
	public int getLatitude();
	
	public void setLongitude(int longitude);
	
	public int getLongitude();
	
	public float calculateTemp(int time,float sunLatitude, float distanceFromSun);
	
	public float calTsun(int sunPosition,float sunLatitude, float distanceFromSun);

	public void setGridProps(int x, int y, int latitude, int longitude, int gs);

	public void setGridSpacing(int gs);

	public int getGridSpacing();

}
