package interpolator;

import java.util.ArrayList;

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
	
	public ArrayList<Grid> interpolateAll(Grid[] daoGrid) {
		ArrayList<Grid> gridList = new ArrayList<Grid>();
		// First interpolate space for each result grid
		for(Grid grid : daoGrid) {
			gridList.add(interpolateSpace(grid));
		}
		// Now interpolate across time
		gridList = interpolateTime(gridList);
		return gridList;
	}
	
	public IGrid decimateSpace(IGrid grid){
		if(grid == null) {
			return null;
		}
		int Percentage = properties.getPropertyInt(EarthGridProperties.EarthGridProperty.GEO_PRECISION);
		int newHeight = grid.getGridHeight()*Percentage/100;
		int newWidth = grid.getGridWidth()*Percentage/100;
		if(newHeight > 180)
			newHeight = 180;
		if(newWidth > 2*newHeight)
			newWidth = 2*newHeight;
			
		//NOTE: here we break the interface abstraction :(
		//      If time allows find a way to update this...
		IGrid newGrid = new Grid(grid.getSunPosition(),grid.getSunPositionDeg(),grid.getTime(),grid.getTimeStep(),newWidth,newHeight,grid.getSunLatitudeDeg(),grid.getDistanceFromSun(),grid.getOrbitalAngle());
		
		for(int j = 0; j < newGrid.getGridHeight(); j++){
			for(int i = 0; i < newGrid.getGridWidth(); i++){
				float tempSum = 0.0f;
				float weight = 0.0f;
				CellCorners newCell = new CellCorners(i,j,newGrid.getGridWidth(),newGrid.getGridHeight());
				for(int m = 0; m < grid.getGridHeight(); m++){
					for(int n = 0; n < grid.getGridWidth(); n++){
						CellCorners oldCell = new CellCorners(n,m,grid.getGridWidth(),grid.getGridHeight());
						float w = newCell.percentOverLap(oldCell); 
						weight += w;
						if(w>0){
							//System.out.println(w);
							//newCell.printCell();
							//oldCell.printCell();
						}
						tempSum += grid.getTemperature(n, m)*w;
					}
				}
				int precision = properties.getPropertyInt(EarthGridProperty.PRECISION);
				newGrid.setTemperature(i, j, roundTemp(tempSum/weight,precision));
			}
		}
		
		return newGrid;
	}
	
	public Grid interpolateSpace(Grid grid){
		//System.out.println("Called InterpolateSpace - " + grid.getGridHeight()*100.0/Percentage);
		int Percentage = properties.getPropertyInt(EarthGridProperties.EarthGridProperty.GEO_PRECISION);
		int newHeight = (int) Math.floor(grid.getGridHeight()*100./Percentage);
		int newWidth = (int) Math.floor(grid.getGridWidth()*100./Percentage);
		if(newHeight > 180)
			newHeight = 180;
		if(newWidth > 2*newHeight)
			newWidth = 2*newHeight;
		//System.out.println(newHeight + ',' + newWidth);
		Grid newGrid = new Grid(grid.getSunPosition(),grid.getSunPositionDeg(),grid.getTime(),grid.getTimeStep(),newWidth,newHeight,grid.getSunLatitudeDeg(),grid.getDistanceFromSun(),grid.getOrbitalAngle());
		for(int j = 0; j < newGrid.getGridHeight(); j++){
			for(int i = 0; i < newGrid.getGridWidth(); i++){
				
				float tempSum = 0.0f;
				float weight = 0.0f;
				CellCorners newCell = new CellCorners(i,j,newGrid.getGridWidth(),newGrid.getGridHeight());
				for(int m = 0; m < grid.getGridHeight(); m++){
					for(int n = 0; n < grid.getGridWidth(); n++){
						CellCorners oldCell = new CellCorners(n,m,grid.getGridWidth(),grid.getGridHeight());
						float w = oldCell.percentOverLap(newCell); 
						weight += w;
						tempSum += grid.getTemperature(n, m)*w;
					}
				}
				int precision = properties.getPropertyInt(EarthGridProperty.PRECISION);
				newGrid.setTemperature(i, j, roundTemp(tempSum/weight,precision));
			}
		}
		
		return newGrid;
	}
	
	public IGrid decimateTime(IGrid grid){
		int Percentage = properties.getPropertyInt(EarthGridProperties.EarthGridProperty.TIME_PRECISION);
		int time = grid.getTime();
		int timestep = grid.getTimeStep();
		int i = time/timestep;
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
	
	public ArrayList<Grid> interpolateTime(ArrayList<Grid> gridList){
		ArrayList<Grid> newGridList = new ArrayList<Grid>();
		Grid lastGrid = null;
		
		for(Grid grid : gridList){
			
			if(lastGrid != null){
				int timestep = lastGrid.getTimeStep();
				int t1 = lastGrid.getTime();
				int t2 = grid.getTime();
				int currentTime = t1 + timestep;
				while(currentTime < t2){
					Grid newGrid = new Grid(lastGrid.getSunPosition(),lastGrid.getSunPositionDeg(),lastGrid.getTime()+lastGrid.getTimeStep(),lastGrid.getTimeStep(),lastGrid.getGridWidth(),lastGrid.getGridHeight(),lastGrid.getSunLatitudeDeg(),lastGrid.getDistanceFromSun(),lastGrid.getOrbitalAngle());
					for(int i = 0; i < lastGrid.getGridWidth(); i++){
						for(int j = 0; j < lastGrid.getGridHeight(); j++){
							double T1 = lastGrid.getTemperature(i, j);
							double T2 = grid.getTemperature(i, j);
							double slope = (T2-T1)/(t2-t1);
							double temp = slope*(currentTime-t1)+T1;
							int precision = properties.getPropertyInt(EarthGridProperty.PRECISION);
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
