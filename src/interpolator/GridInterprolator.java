package interpolator;

import java.util.ArrayList;

import common.CrossPlatformSimpleRng;
import common.EarthGridProperties;
import common.EarthGridProperties.EarthGridProperty;
import common.Grid;
import common.IGrid;

public class GridInterprolator {

	private EarthGridProperties properties;
	
	public GridInterprolator(EarthGridProperties properties){
		this.properties = properties;
	}
	
	public IGrid decimateAll(IGrid grid) {
		// Will return null if determined that grid should not be stored
		return decimateSpace(decimateTime(grid));
	}
	
	public ArrayList<IGrid> interpolateAll(IGrid[] daoGrid) {
		ArrayList<IGrid> gridList = new ArrayList<IGrid>();
		// First interpolate space for each result grid
		for(IGrid grid : daoGrid) {
			gridList.add(interpolateSpace(grid));
		}
		// Now interpolate across time
		gridList = interpolateTime(gridList);
		return gridList;
	}
	
	// This function randomly drops a percentage of the columns given a random
	// seed.  This uses a deterministic rng that is consistent across platforms
	// to ensure the same columns are selected at reconstruction.
	private boolean[] keepColMask(int orignumcols, int keepPcnt) {
		// keepPcnt is assumed 0-100
		boolean[] keepMask = new boolean[orignumcols];
		float[] r = (new CrossPlatformSimpleRng("42")).getVal(orignumcols);
		float keepThresh = (float)keepPcnt / 100;
		boolean foundKeep = false;
		for(int i=0; i < orignumcols; i++) {
			if(r[i] < keepThresh) {
				keepMask[i] = true;
				foundKeep = true;
			}
			else {
				keepMask[i] = false;
			}
		}
		// Make sure we keep atleast one col no matter what
		if(!foundKeep) {
			keepMask[0] = true;
		}
		return keepMask;
	}
	
	public IGrid decimateSpace(IGrid grid){
		if(grid == null) {
			return null;
		}
		int Percentage = properties.getPropertyInt(EarthGridProperties.EarthGridProperty.GEO_PRECISION);
		int precision = properties.getPropertyInt(EarthGridProperty.PRECISION);
		// Don't bother if not decimated in any way
		if(Percentage == 100) {
			return grid;
		}
		
		int newHeight = grid.getGridHeight();
		
		// Find new width based on keepMask
		boolean[] keepMask = keepColMask(grid.getGridWidth(), Percentage);
		int newWidth = 0;
		for(boolean keep : keepMask) {
			if(keep)
				newWidth++;
		}
		
		//NOTE: here we break the interface abstraction :(
		//      If time allows find a way to update this...
		IGrid newGrid = new Grid(grid.getSunPosition(),grid.getSunPositionDeg(),grid.getTime(),grid.getTimeStep(),newWidth,newHeight,grid.getSunLatitudeDeg(),grid.getDistanceFromSun(),grid.getOrbitalAngle());
		
		int outCol = 0;
		for(int i = 0; i < grid.getGridWidth(); i++){
			if(keepMask[i]) {
				for(int j = 0; j < newGrid.getGridHeight(); j++){
					newGrid.setTemperature(outCol, j, roundTemp(grid.getTemperature(i, j),precision));
				}
				outCol++;
			}
		}
		
		return newGrid;
	}
	
	public IGrid interpolateSpace(IGrid grid){
		//System.out.println("Called InterpolateSpace - " + grid.getGridHeight()*100.0/Percentage);
		int Percentage = properties.getPropertyInt(EarthGridProperty.GEO_PRECISION);
		// Don't bother if not decimated in any way
		if(Percentage == 100) {
			return grid;
		}
		
		int newHeight = grid.getGridHeight();
		int newWidth = 360 / properties.getPropertyInt(EarthGridProperty.GRID_SPACING); 
		// Generate keepmask so we know how to redistribute rows
		boolean[] keepMask = keepColMask(newWidth, Percentage);
		
		//NOTE: here we break the interface abstraction :(
		//      If time allows find a way to update this...
		IGrid newGrid = new Grid(grid.getSunPosition(),grid.getSunPositionDeg(),grid.getTime(),grid.getTimeStep(),newWidth,newHeight,grid.getSunLatitudeDeg(),grid.getDistanceFromSun(),grid.getOrbitalAngle());
		
		// First we'll just reestablish stored original values (columns)
		int inCol = 0;
		for(int i = 0; i < newWidth; i++){
			if(keepMask[i]) {
				for(int j = 0; j < newGrid.getGridHeight(); j++){
					newGrid.setTemperature(i, j, grid.getTemperature(inCol, j));
				}
				inCol++;
			}
		}
		
		// now it's time to interpolate to restore missing values
		// For each column not in the keep mask, we'll find the closest columns
		// on either side(which may wrap) and linearly interpolate them to fill 
		// in values for the column.  We'll skip columns that were stored full 
		// quality.
		for(int i = 0; i < newWidth; i++){
			if(!keepMask[i]) {
				// Find westward populated column
				int westIdx = findValidCol(keepMask, i, -1);
				int eastIdx = findValidCol(keepMask, i,  1);
				// find scaled distance to valid entry in each direction
				double westDist = i-westIdx;
				if(westDist < 0) {
					westDist = i-(westIdx-newWidth); // account for possible wrap
				}
				double eastDist = eastIdx-i;
				if(eastDist < 0) {
					eastDist = (eastIdx+newWidth)-i; // account for possible wrap
				}
				double totalDist = westDist+eastDist;
				//normalize distances
				westDist = westDist / totalDist;
				eastDist = eastDist / totalDist;
				//now interpolate column values
				for(int j = 0; j < newGrid.getGridHeight(); j++){
					//NOTE: now that we've normlized distances, we'll use 1-dist
					//      in calc below because lower distance==more weight
					double interpTemp = newGrid.getTemperature(westIdx, j) * (1-westDist) + 
										newGrid.getTemperature(eastIdx, j) * (1-eastDist);
					newGrid.setTemperature(i, j, interpTemp);
				}
			}
		}
		return newGrid;
	}
	
	private int findValidCol(boolean[] keepMask, int start, int step) {
		// helper function for locating the index of the next valid column
		// in a given direction.  Use step=-1 to search west, 1 to search east.
		int numCol = keepMask.length;
		int curIdx = start;
		while(!keepMask[curIdx]) {
			curIdx += step;
			// Take care of wrapping
			if(curIdx < 0) {
				curIdx = numCol-1;
			}
			if(curIdx >= numCol) {
				curIdx = 0;
			}
			if(curIdx == start) {
				throw new RuntimeException("It should never be possible to return back to start since keepMask is assumed to always have atleast 1 true value.");
			}
		}
		return curIdx;
	}
	public IGrid decimateTime(IGrid grid){
		if(grid.getTime() == 0)
			return grid;
		
		int endtime = 43800*properties.getPropertyInt(EarthGridProperty.SIMULATION_LENGTH);
		if(grid.getTime() == endtime)
			return grid;
		int Percentage = properties.getPropertyInt(EarthGridProperties.EarthGridProperty.TIME_PRECISION);
		int time = grid.getTime();
		int timestep = grid.getTimeStep();
		int i = (time/timestep)%100;
		float value = (float) Math.floor(100.0f/Percentage);
		float x = i/value;
		//System.out.println(time + "," + timestep + "," + i + "," + value + "," + x);
		for(int j = 0; j <= Percentage; j++){
			//System.out.print(Math.abs(x-j)+", ");
			if( Math.abs(x-j) < 1e-4 ){
				//System.out.println("");
				int precision = properties.getPropertyInt(EarthGridProperty.PRECISION);
				for(int m = 0; m < grid.getGridHeight(); m++){
					for(int n = 0; n < grid.getGridWidth(); n++){
						grid.setTemperature(n, m, roundTemp(grid.getTemperature(n, m),precision));
					}
				}
				return grid;
			}
		}	
		//System.out.println("");
		return null;
	}
	
	public ArrayList<IGrid> interpolateTime(ArrayList<IGrid> gridList){
		
		int Percentage = properties.getPropertyInt(EarthGridProperties.EarthGridProperty.TIME_PRECISION);
		// Don't bother if not decimated in any way
		if(Percentage == 100) {
			return gridList;
		}

		ArrayList<IGrid> newGridList = new ArrayList<IGrid>();
		IGrid lastGrid = null;
		double T1 = 0;
		double T2 = 0;
		double slope = 0;
		double temp = 0;
		int precision = properties.getPropertyInt(EarthGridProperty.PRECISION);
		for(IGrid grid : gridList){
			
			if(lastGrid != null){
				int timestep = lastGrid.getTimeStep();
				int t1 = lastGrid.getTime();
				int t2 = grid.getTime();
				int currentTime = t1 + timestep;
				while(currentTime < t2){
					Grid newGrid = new Grid(lastGrid.getSunPosition(),lastGrid.getSunPositionDeg(),lastGrid.getTime()+lastGrid.getTimeStep(),lastGrid.getTimeStep(),lastGrid.getGridWidth(),lastGrid.getGridHeight(),lastGrid.getSunLatitudeDeg(),lastGrid.getDistanceFromSun(),lastGrid.getOrbitalAngle());
					for(int i = 0; i < lastGrid.getGridWidth(); i++){
						for(int j = 0; j < lastGrid.getGridHeight(); j++){
							T1 = lastGrid.getTemperature(i, j);
							T2 = grid.getTemperature(i, j);
							slope = (T2-T1)/(t2-t1);
							temp = slope*(currentTime-t1)+T1;
							newGrid.setTemperature(i, j, roundTemp(temp,precision));
						}
					}
					newGridList.add(newGrid);
					currentTime += timestep;
				}
			}
			
			lastGrid = grid;
			
			newGridList.add(lastGrid);
		}
		
		return newGridList;
	}
	
	public class CellCorners{
		
		int gs = 0;
		float lat1 = 0.0f;
		float lat2 = 0.0f;
		float lon1 = 0.0f;
		float lon2 = 0.0f;
		
		public CellCorners(int x, int y, int width, int height){
			gs = 180/height;
			lat1 = y*gs;
			lat2 = (y+1)*gs;
			lon1 = x*gs;
			lon2 = (x+1)*gs;
		}
		
		public void printCell(){
			System.out.println(lat1 + "," + lat2 + "," + lon1 + "," + lon2);
		}
		
		public float percentOverLap(CellCorners cell){
			if(cell.lat2 < lat1 || cell.lat1 > lat2 || cell.lon2 < lon1 || cell.lon1 > lon2)
				return 0.0f;
			float p1 = 0.0f;
			float p2 = 0.0f;
				
			if( cell.lat2 > lat2 ){
				p1 = (lat2-Math.max(lat1,cell.lat1))*1.0f/(lat2-lat1);
				//System.out.println("First - " + p1);
			}else{
				p1 = (cell.lat2-Math.max(lat1,cell.lat1))*1.0f/(lat2-lat1);
				//System.out.println("Second - " + p1);
			}
			if( cell.lon2 > lon2 ){
				p2 = (lon2-Math.max(lon1,cell.lon1))*1.0f/(lon2-lon1);
				//System.out.println("Third - " + p2);
			}else{
				p2 = (cell.lon2-Math.max(lon1,cell.lon1))*1.0f/(lon2-lon1);
				//System.out.println("Fourth - " + p2);
			}
			return p2*p1;
			
		}
	}
	
	public double roundTemp(double temp, int precision){
		//System.out.println(Math.floor(temp * Math.pow(10.0,precision)));
		return (double) (Math.floor(temp * Math.pow(10.0,precision)) / Math.pow(10.0, precision));
	}
}
