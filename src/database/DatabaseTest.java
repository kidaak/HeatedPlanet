package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

import dao.EarthGridDao;

/**
 * Created by David Welker on 11/13/14.
 */
public class DatabaseTest
{
    public static void main(String[] args) throws Exception
    {
    	Properties connectionProps = new Properties();
    	String username = "sa";
    	String password = "";
        connectionProps.put("user", username);
        connectionProps.put("password", password);
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.
            getConnection("jdbc:h2:~/test", connectionProps);
        // add application code here
        //GridTable gridTable = new GridTable(conn);
        SimulationDatabase sdb = SimulationDatabase.getSimulationDatabase();
        
        EarthGridDao dao = EarthGridDao.getEarthGridDao();
        System.out.println(dao.isNameUnique("test"));
        String[] names = dao.getAllNames();
        for (int i=0; i < names.length; i++) {
        	System.out.println(names[i]);
        }
        
        sdb.executeSqlGeneral("DROP TABLE Grid");
        sdb.executeSqlGeneral("DROP TABLE Simulation");

        conn.close();
        System.out.println("Done with test!");
    }
}
