package simulation.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import simulation.Earth;

public final class GridCell implements EarthCell<GridCell> {

	// gs: grid spacing
	public int x, y, latitude, longitude, gs;
	
	// average temperature
	private static double avgsuntemp;
	private static double avgArea;

	private boolean visited;
	private double currTemp, newTemp;

	private GridCell top = null, bottom = null, left = null, right = null;

	// Cell properties: surface area, perimeter
	private double lv, lb, lt, surfarea, pm;

	public GridCell(double temp, int x, int y, int latitude, int longitude, int gs) {

		if (temp > Double.MAX_VALUE) throw new IllegalArgumentException("Invalid temp provided");
		if (x > Integer.MAX_VALUE || x < Integer.MIN_VALUE) throw new IllegalArgumentException("Invalid 'x' provided");
		if (y > Integer.MAX_VALUE || y < Integer.MIN_VALUE) throw new IllegalArgumentException("Invalid 'y' provided");

		this.setGridProps(x, y, latitude, longitude, gs);

		this.setTemp(temp);
		this.visited = false;
	}

	public GridCell(GridCell top, GridCell bottom, GridCell left, GridCell right, double temp, int x, int y, int latitude, int longitude, int gs) {
		
		this(temp, x, y, latitude, longitude, gs);

		this.setTop(top);
		this.setBottom(bottom);
		this.setLeft(left);
		this.setRight(right);
	}

	@Override
	public void setTop(GridCell top) {

		if (top == null) return;
		this.top = top;
	}

	@Override
	public GridCell getTop() {
		return this.top;
	}

	@Override
	public void setBottom(GridCell bottom) {

		if (bottom == null) return;
		this.bottom = bottom;
	}

	@Override
	public GridCell getBottom() {
		return this.bottom;
	}

	@Override
	public void setRight(GridCell right) {

		if (right == null) return;
		this.right = right;
	}

	@Override
	public GridCell getRight() {
		return this.right;
	}

	@Override
	public void setLeft(GridCell left) {

		if (left == null) return;
		this.left = left;
	}

	@Override
	public GridCell getLeft() {
		return this.left;
	}

	@Override
	public double getTemp() {
		return this.currTemp;
	}

	@Override
	public void setTemp(double temp) {

		if (temp > Double.MAX_VALUE) throw new IllegalArgumentException("Invalid temp provided");
		this.currTemp = temp;
	}

	@Override
	public void setGridProps(int x, int y, int latitude, int longitude, int gs) {

		this.setX(x);
		this.setY(y);
		this.setLatitude(latitude);
		this.setLongitude(longitude);
		this.setGridSpacing(gs);

		// calc lengths, area, etc.
		this.calSurfaceArea(latitude, gs);
	}

	@Override
	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	@Override
	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setY(int y) {
		this. y = y;
	}
	
	@Override
	public double calculateTemp(int sunPosition,double sunLatitude, double distanceFromSun) {
		double temp   = this.currTemp + (calTneighbors() - this.currTemp) / 5 + ( calTsun(sunPosition,sunLatitude,distanceFromSun) + calTcool(distanceFromSun) ) / 10;
		this.newTemp = (temp > 0) ? temp : 0;    // avoid negative temperature
		return this.newTemp; // new temp
	}

	@Override
	public void swapTemp() {
		this.currTemp = this.newTemp;
		this.newTemp = 0;
	}

	@Override
	public void visited(boolean visited) {
		this.visited = visited;
	}

	@Override
	public Iterator<GridCell> getChildren(boolean visited) {
		List<GridCell> ret = new ArrayList<GridCell>();

		if (this.top != null 	&& this.top.visited == visited) 	ret.add(this.top);
		if (this.bottom != null && this.bottom.visited == visited) 	ret.add(this.bottom);
		if (this.left != null 	&& this.left.visited == visited) 	ret.add(this.left);
		if (this.right != null 	&& this.right.visited == visited) 	ret.add(this.right);

		return ret.iterator();
	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}

	@Override
	public int getLatitude() {
		return this.latitude;
	}

	@Override
	public int getLongitude() {
		return this.longitude;
	}

	@Override
	public void setGridSpacing(int gs) {
		this.gs = gs;
	}

	@Override
	public int getGridSpacing() {
		return this.gs;
	}
	
	public double calTsun(int sunPosition,double sunLatitude, double distanceFromSun) {
		
		int   sunLongitude      = getSunLocationOnEarth(sunPosition);
		double attenuation_lat   = (double) Math.cos(Math.toRadians(sunLatitude + (this.latitude  + 1.0 * this.gs/2) ));
		//double attenuation_longi = (double) (( (Math.abs(sunLongitude - this.longitude) % 360 ) < 90 ) ? Math.cos(Math.toRadians(sunLongitude - this.longitude)) : 0);
		double attenuation_longi = (double) Math.cos(Math.toRadians(sunLongitude - this.longitude*1.0 ));
		//attenuation_longi = attenuation_longi > 0 ? attenuation_longi : 0;
		attenuation_lat = attenuation_lat > 0 ? attenuation_lat : 0;
		
		double cosSunLat = Math.cos(Math.toRadians(sunLatitude));
		double cosGridLat = Math.cos(Math.toRadians(this.latitude  + 1.0 * this.gs / 2));
		double sinSunLat = Math.sin(Math.toRadians(sunLatitude));
		double sinGridLat = Math.sin(Math.toRadians(this.latitude  + 1.0 * this.gs / 2));
		double cosLong = Math.cos(Math.toRadians(sunLongitude - (this.longitude*1.0))+Math.PI);
		double dAngle = Math.acos(-sinSunLat*sinGridLat-cosSunLat*cosGridLat*cosLong);
		//System.out.println(dAngle);
		if(dAngle > Math.PI/2)
			return 0;
		else if(Math.abs(sunLatitude+this.latitude) >= 90)
			return 0;
		else
			return (double) Math.pow(Earth.SEMI_MAJOR_AXIS/distanceFromSun, 2) * 278 * Math.abs(attenuation_lat * attenuation_longi);
		
		}
	
	private void calSurfaceArea(int latitude, int gs) {
		
		double p  = 1.0 * gs / 360;
		this.lv   = (double) (Earth.CIRCUMFERENCE * p);
		this.lb   = (double) (Math.cos(Math.toRadians(latitude)) * this.lv);
		this.lb   = this.lb > 0 ? this.lb: 0;
		this.lt   = (double) (Math.cos(Math.toRadians(latitude + gs)) * this.lv);
		this.lt   = this.lt > 0 ? this.lt: 0;
		double h  = Math.sqrt(Math.pow(this.lv,2) - 1/4 * Math.pow((this.lb - this.lt), 2));

		this.pm = (double) (this.lt + this.lb + 2 * this.lv);
		this.surfarea =  (double) (1.0/2 * (this.lt + this.lb) * h);
	}

	// A help function for get the Sun's corresponding location on longitude.
	private int getSunLocationOnEarth(int sunPosition) {
		
		// Grid column under the Sun at sunPosition
		int cols = 360 / this.gs;
		int j    = sunPosition;
		return j < (cols / 2) ? -(j + 1) * this.gs : (360) - (j + 1) * this.gs;
	}

	public double calTcool(double distanceFromSun) {
//		double beta = (double) (this.surfarea / avgArea);  // actual grid area / average cell area
		//return -1 * beta * avgsuntemp removed beta;
		return (double) ( -1 * this.currTemp / 288 * avgsuntemp / Math.pow(Earth.SEMI_MAJOR_AXIS/distanceFromSun, 2) );
	}
	
	public static void setAvgSuntemp(double avg){
		avgsuntemp = avg;
	}
	
	public static double getAvgSuntemp(){
		return avgsuntemp;
	}
	
	public double getSurfarea() {
		return this.surfarea;
	}
	
	public static void setAverageArea(double avgarea) {
		avgArea = avgarea;
	}
	
	public static double getAverageArea() {
		return avgArea;
	}
	
	public double calTneighbors() {

		double top_temp = 0, bottom_temp = 0;

		if (this.top != null) 	top_temp = this.lt / this.pm * this.top.getTemp();
		if (this.bottom != null) 	bottom_temp = this.lb / this.pm * this.bottom.getTemp();

		//System.out.println(this.lt / this.pm + this.lb / this.pm + this.lv / this.pm * 2);
		return  top_temp + bottom_temp + this.lv / this.pm * (this.left.getTemp() + this.right.getTemp());
	}
}
