package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

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
        GridTable gridTable = GridTable.getGridTable();
        
        gridTable.executeSqlGeneral("DROP TABLE Grid");

        conn.close();
        System.out.println("Done with test!");
    }
}