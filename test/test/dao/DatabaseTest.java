package test.dao;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import common.EarthGridProperties;
import common.Grid;
import common.EarthGridProperties.EarthGridProperty;
import dao.EarthGridDao;
import dao.EarthGridInsert;
import dao.EarthGridQuery;
import dao.EarthGridResponse;
import dao.ResponseType;
import database.SimulationDatabase;

/**
 * Created by David Welker on 11/13/14.
 */
public class DatabaseTest
{
    public static void main(String[] args) throws Exception
    {
        SimulationDatabase sdb = SimulationDatabase.getSimulationDatabase();
        EarthGridDao dao = EarthGridDao.getEarthGridDao();
                
        EarthGridProperties EGProps = new EarthGridProperties();
        EGProps.setProperty(EarthGridProperty.NAME, "InsertedName1");
        EGProps.setProperty(EarthGridProperty.AXIAL_TILT, 1.0);
        EGProps.setProperty(EarthGridProperty.ECCENTRICITY, 0.99);
        EGProps.setProperty(EarthGridProperty.GRID_SPACING, 1);
        EGProps.setProperty(EarthGridProperty.SIMULATION_TIME_STEP, 1);
        EGProps.setProperty(EarthGridProperty.SIMULATION_LENGTH, 1);
        EGProps.setProperty(EarthGridProperty.PRECISION, 1);
        EGProps.setProperty(EarthGridProperty.GEO_PRECISION, 1);
        EGProps.setProperty(EarthGridProperty.TIME_PRECISION, 1);
        
        
        Grid grid1 = new Grid(1, 1, 1, 1, 1, 1, 1, 1, 1);
        Grid grid2 = new Grid(2, 2, 2, 2, 2, 2, 2, 2, 2);
        Grid grid3 = new Grid(3, 3, 3, 3, 3, 3, 3, 3, 3);
        Grid grid4 = new Grid(4, 4, 4, 4, 4, 4, 4, 4, 4);
        
        Grid[] gridArray1 = {grid1};
        Grid[] gridArray2 = {grid2,grid3};
        Grid[] gridArray3 = {grid1,grid2,grid3,grid4};
        
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Calendar cal3 = Calendar.getInstance();
        Calendar cal4 = Calendar.getInstance();
        
        Calendar[] calArray1 = {cal1};
        Calendar[] calArray2 = {cal2,cal3};
        Calendar[] calArray3 = {cal1,cal2,cal3,cal4};
        Calendar[] calArray4 = {};
        
        try{
        	EarthGridInsert insert1 = new EarthGridInsert(EGProps, cal1, gridArray1, calArray1);
        	ResponseType response1 = dao.insertEarthGridSimulation(insert1);
        	System.out.println(response1.name());
        	
            EGProps.setProperty(EarthGridProperty.NAME, "InsertedName2");
            EarthGridInsert insert2 = new EarthGridInsert(EGProps, cal2, gridArray2, calArray2);
            ResponseType response2 = dao.insertEarthGridSimulation(insert2);
            System.out.println(response2.name());
        	
            EGProps.setProperty(EarthGridProperty.NAME, "InsertedName3");
            EarthGridInsert insert3 = new EarthGridInsert(EGProps, cal1, gridArray3, calArray3);
            ResponseType response3 = dao.insertEarthGridSimulation(insert3);
            System.out.println(response3.name());
        	
	        System.out.println(dao.isNameUnique("InsertedName2"));
	        System.out.println(dao.isNameUnique("name"));
	        System.out.println(dao.getAllNames().length);
	        String[] names = dao.getAllNames();
	        for (int i=0; i < names.length; i++) {
	        	System.out.println("Name = "+names[i]);
	        	System.out.println("ID = "+dao.getSimulationIdFromName(names[i]));
	        }
	        
	        EGProps.setProperty(EarthGridProperty.NAME, "InsertedName3");
	        EarthGridQuery egq = new EarthGridQuery(EGProps, cal4);
	        EarthGridResponse response4 = dao.queryEarthGridSimulationByName(egq);
	        
	        EGProps.setProperty(EarthGridProperty.NAME, "InsertedName2");
	        EarthGridQuery egq2 = new EarthGridQuery(EGProps, cal2);
	        EarthGridResponse response5 = dao.queryEarthGridSimulation(egq2);
	        
	        System.out.println(response4.getResult().name());
	        System.out.println(response5.getResult().name());
	        
        }catch(SQLException ex){
        	for (Throwable e : ex)
            {
                if (e instanceof SQLException)
                {
                    SQLException sqlException = (SQLException) e;
                    if ( true )
                    {
                    	sqlException.printStackTrace(System.out);
                        System.out.println("SQLState: " + sqlException.getSQLState());
                        System.out.println("Error Code: " + sqlException.getErrorCode());
                        System.out.println("Message: " + sqlException.getMessage());
                        Throwable t = ex.getCause();
                        while (t != null)
                        {
                            System.out.println("Cause: " + t);
                            t = t.getCause();
                        }
                    }
                }
            }
        }finally{
        	dao.resetDatabase("42");
        }

        System.out.println("Done with test!");
    }
}
