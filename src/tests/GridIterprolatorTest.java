package tests;

import static org.junit.Assert.*;
import interpolator.GridInterprolator;

import org.junit.Before;
import org.junit.Test;

import common.EarthGridProperties;
import common.Grid;
import common.EarthGridProperties.EarthGridProperty;
import simulation.Earth;

public class GridIterprolatorTest {
	
	private GridInterprolator GI;
	private Grid grid1;
	private Grid grid2;
	private EarthGridProperties ep;

	@Before
	public void setUp() throws Exception {
		ep = new EarthGridProperties();
		ep.setProperty(EarthGridProperty.GEO_PRECISION, 50);
		GI = new GridInterprolator(ep);
		grid1 = new Grid(0,0,0,1,10,5,0,(float)Earth.SEMI_MAJOR_AXIS,0);
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 10; j++){
				if(i < 2 && j > 5)
					grid1.setTemperature(j,i,100);
				else if(i >= 2 && j <= 5)
					grid1.setTemperature(j,i,200);
				else
					grid1.setTemperature(j,i,300);
			}
		}
		
		grid2 = new Grid(0,0,1,0,4,2,0,(float)Earth.SEMI_MAJOR_AXIS,0);
		grid2.setTemperature(0, 0, 100);
		grid2.setTemperature(1, 0, 100);
		grid2.setTemperature(2, 0, 200);
		grid2.setTemperature(3, 0, 200);
		grid2.setTemperature(0, 1, 200);
		grid2.setTemperature(1, 1, 200);
		grid2.setTemperature(2, 1, 300);
		grid2.setTemperature(3, 1, 300);
	}

	
	@Test
	public void test2() {
		ep.setProperty(EarthGridProperty.GEO_PRECISION, 40);
		ep.setProperty(EarthGridProperty.PRECISION, 3);
		GI = new GridInterprolator(ep);
		Grid newGrid = GI.interpolateSpace(grid2);
		assertEquals(newGrid.getTemperature(0,0),100,0.1);
		assertEquals(newGrid.getTemperature(9,0),200,0.1);
		assertEquals(newGrid.getTemperature(0,4),200,0.1);
		assertEquals(newGrid.getTemperature(9,4),300,0.1);
		assertEquals(newGrid.getTemperature(0,2),150,0.1);
		assertEquals(newGrid.getTemperature(9,2),250,0.1);
		assertTrue(newGrid.getGridWidth() == 10);
		assertTrue(newGrid.getGridHeight() == 5);
	}
	
	@Test
	public void test1() {
		printGrid(grid1);
		ep.setProperty(EarthGridProperty.GEO_PRECISION, 50);
		ep.setProperty(EarthGridProperty.PRECISION, 3);
		GI = new GridInterprolator(ep);
		Grid newGrid = GI.decimateSpace(grid1);
		printGrid(newGrid);
	
		assertEquals(newGrid.getTemperature(0,0),(2*300.0+0.5*200.0)/2.5,0.1);
		assertEquals(newGrid.getTemperature(3,0),(2*100+0.5*300)/2.5,0.1);
		assertEquals(newGrid.getTemperature(0,1),200,0.1);
		assertEquals(newGrid.getTemperature(3,1),300,0.1);
		assertTrue(newGrid.getGridWidth() == 4);
		assertTrue(newGrid.getGridHeight() == 2);
	}

	@Test
	public void test3(){
		float temp = 12.34567898765f;
		
		System.out.println(GI.roundTemp(temp, 12));
		assertEquals(GI.roundTemp(temp, 0),12f,1e-8f);
		assertEquals(GI.roundTemp(temp, 1),12.3f,0.00000001f);
		assertEquals(GI.roundTemp(temp, 2),12.34f,0.0000001f);
		assertEquals(GI.roundTemp(temp, 3),12.345f,0.00000001f);
		assertEquals(GI.roundTemp(temp, 4),12.3456f,0.00000001f);
		assertEquals(GI.roundTemp(temp, 5),12.34567f,0.00000001f);
		//assertEquals(GI.roundTemp(temp, 6),12.345678f,0.0000001f);  Requires Double Precision
	}
	
	public void printGrid(Grid printGrid){
		for(int i = 0; i < printGrid.getGridHeight(); i++){
			for(int j =0; j< printGrid.getGridWidth(); j++){
				System.out.print(printGrid.getTemperature(j, i)+", ");
			}
			System.out.println("");
		}
	}

}
