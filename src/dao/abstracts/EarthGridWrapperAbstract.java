package dao.abstracts;

import java.util.ArrayList;
import java.util.Calendar;

import common.Grid;

public abstract class EarthGridWrapperAbstract {
	private int gridSpacing;
	private double axialTilt, eccentricity;
	private boolean hasRangeOfDates;
	private Calendar startDate,endDate;
	private ArrayList<Grid> grid;
	
	
	/**
	 * Constructor without a grid object. Used mostly for query.
	 * 
	 * @param gs Grid Spacing value
	 * @param at Axial Tilt value
	 * @param e Eccentricity value
	 * @param sd Start Date value
	 * @param ed End Date value
	 */
	public EarthGridWrapperAbstract(int gs, double at, double e, Calendar sd, Calendar ed) {
		
		validateInputs(gs, at, e, sd, ed);
		
		grid = new ArrayList<Grid>(1);
		
		this.setGridSpacing(gs);
		this.setAxialTilt(at);
		this.setEccentricity(e);
		this.setStartDate(sd);
		this.setEndDate(ed);
		this.setGrid(null);
		
		if(sd.equals(ed)){
			this.setHasRangeOfDates(false);
		}else{
			this.setHasRangeOfDates(true);
		}
	}
	
	/**
	 * Single Grid constructor.
	 * 
	 * @param gs Grid Spacing value
	 * @param at Axial Tilt value
	 * @param e Eccentricity value
	 * @param sd Start Date value
	 * @param ed End Date value
	 * @param g Grid object associated with the above parameters
	 */
	public EarthGridWrapperAbstract(int gs, double at, double e, Calendar sd, Calendar ed, Grid g) {
		
		validateInputs(gs, at, e, sd, ed);
		
		grid = new ArrayList<Grid>(1);
		
		this.setGridSpacing(gs);
		this.setAxialTilt(at);
		this.setEccentricity(e);
		this.setStartDate(sd);
		this.setEndDate(ed);
		this.setGrid(g);
		
		if(sd.equals(ed)){
			this.setHasRangeOfDates(false);
		}else{
			this.setHasRangeOfDates(true);
		}
	}
	
	/**
	 * Multiple Grid constructor.
	 * 
	 * @param gs Grid Spacing value
	 * @param at Axial Tilt value
	 * @param e Eccentricity value
	 * @param sd Start Date value
	 * @param ed End Date value
	 * @param ga Array of Grid objects associated with the above parameters
	 */
	public EarthGridWrapperAbstract(int gs, double at, double e, Calendar sd, Calendar ed, Grid[] ga) {
		
		validateInputs(gs, at, e, sd, ed);
		
		grid = new ArrayList<Grid>(ga.length);
		
		this.setGridSpacing(gs);
		this.setAxialTilt(at);
		this.setEccentricity(e);
		this.setStartDate(sd);
		this.setEndDate(ed);
		
		for(int i = 0; i < grid.size(); i++)
			this.setGridAt(i,ga[i]);
		
		if(sd.equals(ed)){
			this.setHasRangeOfDates(false);
		}else{
			this.setHasRangeOfDates(true);
		}
	}
	
	/**
	 * Method to validate the values of the parameters
	 * 
	 * @param gs Grid Spacing. Must be >= 1
	 * @param e Eccentricity. Must be >= 0 and < 1
	 * @param at Axial Tilt. Must be between -180 and +180
	 * @param sd Start Date. Must be equal to or before End Date
	 * @param ed End Date. Must be equal to or after End Date
	 */
	private void validateInputs(int gs, double e, double at, Calendar sd, Calendar ed){
		if(gs <= 0){
			throw new IllegalArgumentException("Grid Spacing must be >= 1");
		}
		if(e < 0 || e >= 1){
			throw new IllegalArgumentException("The eccentricity must be between 0 and 1, but strictly less than 1.00");
		}
		if(at < -180 || at > 180){
			throw new IllegalArgumentException("The Axial Tilt must be between -180 and 180");
		}
		if(sd.after(ed)){
			throw new IllegalArgumentException("The Start Date must be a date before the End Date");
		}
	}
	
	/*
	 * Basic Getters and Setters
	 */
	public int getGridSpacing() {
		return gridSpacing;
	}
	protected void setGridSpacing(int gridSpacing) {
		this.gridSpacing = gridSpacing;
	}
	public double getAxialTilt() {
		return axialTilt;
	}
	protected void setAxialTilt(double axialTilt) {
		this.axialTilt = axialTilt;
	}
	public double getEccentricity() {
		return eccentricity;
	}
	protected void setEccentricity(double eccentricity) {
		this.eccentricity = eccentricity;
	}
	public boolean hasRangeOfDates() {
		return hasRangeOfDates;
	}
	protected void setHasRangeOfDates(boolean hasRangeOfDates) {
		this.hasRangeOfDates = hasRangeOfDates;
	}
	public Calendar getStartDate() {
		return startDate;
	}
	protected void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}
	public Calendar getEndDate() {
		return endDate;
	}
	protected void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}
	
	
	/*
	 * Specialized Grid Setters
	 */
	protected void setGrid(Grid g) {
		this.setGridAt(0,g);
	}
	protected void setGridAt(int index, Grid g){
		this.grid.set(index,g);
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
		return (Grid[]) this.grid.toArray();
	}
	
	
}
