package interpolator;

import java.util.ArrayList;
import common.Grid;

public class GridInterprolator {

	public Grid decimateSpace(Grid grid, int Percentage){
		int newHeight = grid.getGridHeight()*Percentage/100;
		int newWidth = grid.getGridWidth()*Percentage/100;
		if(newHeight > 180)
			newHeight = 180;
		if(newWidth > 2*newHeight)
			newWidth = 2*newHeight;
			
		Grid newGrid = new Grid(grid.getSunPosition(),grid.getSunPositionDeg(),grid.getTime(),newWidth,newHeight,grid.getSunLatitudeDeg(),grid.getDistanceFromSun(),grid.getOrbitalAngle());
		
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
				newGrid.setTemperature(i, j, tempSum/weight);
			}
		}
		
		return newGrid;
	}
	
	public Grid interpolateSpace(Grid grid, int Percentage){
		//System.out.println("Called InterpolateSpace - " + grid.getGridHeight()*100.0/Percentage);
		int newHeight = (int) Math.floor(grid.getGridHeight()*100./Percentage);
		int newWidth = (int) Math.floor(grid.getGridWidth()*100./Percentage);
		if(newHeight > 180)
			newHeight = 180;
		if(newWidth > 2*newHeight)
			newWidth = 2*newHeight;
		//System.out.println(newHeight + ',' + newWidth);
		Grid newGrid = new Grid(grid.getSunPosition(),grid.getSunPositionDeg(),grid.getTime(),newWidth,newHeight,grid.getSunLatitudeDeg(),grid.getDistanceFromSun(),grid.getOrbitalAngle());
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
				
				newGrid.setTemperature(i, j, tempSum/weight);
			}
		}
		
		return newGrid;
	}
	
	
	public ArrayList<Grid> decimate(ArrayList<Grid> gridList,int spacialPercentage, int temporalPercentage){
		ArrayList<Grid> newGrid = new ArrayList<Grid>();
		Grid lastGrid = null;
		
		for(Grid grid : gridList){
			
			if(lastGrid != null){
				
			}
			
			lastGrid = grid;
		}
		
		return newGrid;
	}
	
	public ArrayList<Grid> interprolate(ArrayList<Grid> gridList, int spacialPercentage, int temporalPercentage ){
		ArrayList<Grid> newGrid = new ArrayList<Grid>();
		Grid lastGrid = null;
		
		for(Grid grid : gridList){
			
			if(lastGrid != null){
				
			}
			
			lastGrid = grid;
		}
		
		return newGrid;
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
}
