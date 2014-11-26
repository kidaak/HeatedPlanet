package dao.abstracts;

import java.util.ArrayList;
import java.util.Calendar;

import common.EarthGridProperties;
import common.Grid;
import common.EarthGridProperties.EarthGridProperty;

public abstract class EarthGridWrapperAbstract {
	private EarthGridProperties props;
	private ArrayList<Calendar> gridDate;
	private ArrayList<Grid> grid;
	
	/**
	 * Constructor without a grid object. Used for query.
	 * 
	 * @param properties Properties object for EarthGrid
	 * @param endDate The ending date of the simulation
	 */
	public EarthGridWrapperAbstract(EarthGridProperties properties) {
		
		this.props = properties;
		this.grid = new ArrayList<Grid>(1);
		this.gridDate = new ArrayList<Calendar>(1);
		
		this.setGrid(null);
		this.setGridDate(null);
	}
	
	/**
	 * EarthGrid constructor. For returning data.
	 * 
	 * @param properties Properties object for EarthGrid
	 * @param endDate The ending date of the simulation
	 * @param ga Array of Grid objects associated with the above parameters
	 * @param gd Array of dates corresponding to the array of grids
	 */
	public EarthGridWrapperAbstract(EarthGridProperties properties, Grid[] g, Calendar[] gd) {
		
		if(g != null && gd != null){
			if(g.length != gd.length)
				throw new IllegalArgumentException("Array of Grids must be the same length as the array of Grid Dates. ("+g.length+","+gd.length+")");
			
			this.grid = new ArrayList<Grid>(g.length);
			this.gridDate = new ArrayList<Calendar>(gd.length);
			
			for(int i = 0; i < g.length; i++)
				this.setGridAt(i,g[i]);
			for(int i = 0; i < gd.length; i++)
				this.setGridDateAt(i,gd[i]);
		}else{
			this.grid = new ArrayList<Grid>(1);
			this.gridDate = new ArrayList<Calendar>(1);
			this.setGrid(null);
			this.setGridDate(null);
		}
		
		this.props = properties;		
	}
	
	
	/*
	 * Basic Getters and Setters
	 */
	public String getName() {
		return props.getPropertyString(EarthGridProperty.NAME);
	}
	public int getGridSpacing() {
		return props.getPropertyInt(EarthGridProperty.GRID_SPACING);
	}
	public double getAxialTilt() {
		return props.getPropertyFloat(EarthGridProperty.AXIAL_TILT);
	}
	public double getEccentricity() {
		return props.getPropertyFloat(EarthGridProperty.ECCENTRICITY);
	}
	public Calendar getEndDate() {
		return props.getPropertyCalendar(EarthGridProperty.END_DATE);
	}

	public EarthGridProperties getProperties(){
		return this.props;
	}
	protected void setProperties(EarthGridProperties egp){
		this.props = egp;
	}
	
	/*
	 * Specialized Grid Setters
	 */
	protected void setGrid(Grid g) {
		this.grid.add(0, g);
	}
	protected void setGridAt(int index, Grid g){
		this.grid.add(index, g);
	}
	
	/*
	 * Specialized Grid Getters
	 */
	public Grid getGrid() {
		return getGridAt(0);
	}
	public Grid getGridAt(int index){
		return this.grid.get(index);
	}
	public Grid[] getAllGrids(){
		Grid[] g = {};
		return this.grid.toArray(g);
	}
	
	/*
	 * Specialized Grid Date Setters
	 */
	protected void setGridDate(Calendar c){
		this.gridDate.add(0, c);
	}
	protected void setGridDateAt(int index, Calendar c){
		this.gridDate.add(index, c);
	}
	
	/*
	 * Specialized Grid Date Getters
	 */
	public Calendar getGridDate() {
		return getGridDateAt(0);
	}
	public Calendar getGridDateAt(int index){
		return this.gridDate.get(index);
	}
	public Calendar[] getAllGridDates(){
		Calendar[] c = {};
		return this.gridDate.toArray(c);
	}
}
