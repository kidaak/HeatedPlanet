package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by David Welker on 11/20/14.
 */
public class GridTable
{
    private static Connection connection;
    private static final GridTable gt;
    
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:~/test";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    
    
    static{
    	Properties connectionProps = new Properties();
        connectionProps.put("user", DB_USER);
        connectionProps.put("password", DB_PASSWORD);
        Connection conn = null;
        try {
			Class.forName(DB_DRIVER);
			conn = DriverManager.getConnection(DB_CONNECTION, connectionProps);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			printSQLException(e);
		}
    	gt = new GridTable(conn);
    	try {
			gt.createTable();
		} catch (SQLException e) {
			if(!ignoreSQLException(e))
				printSQLException(e);
		}
    }
    
    private GridTable(Connection c){
    	this.connection = c;
    }
    
    public static GridTable getGridTable(){
    	return gt;
    }
    
    public Connection getConnection(){
    	return connection;
    }
    
    public void createTable() throws SQLException
    {
        String createString =
                "create table GRID " +
                        "(gridId INTEGER not null, " +
                        //Physical Factors
                        "tilt INTEGER not null," +
                        "eccentricity DOUBLE not null, " +
                        //Simulation Settings
                        "name VARCHAR(32) not null, " +
                        "gridSpacing INTEGER not null, " +
                        "simTimeStep INTEGER not null, " +
                        "simLength INTEGER not null, " +
                        //Invocation Parameters
                        "precision INTEGER not null, " +
                        "geographicPrecision INTEGER not null, " +
                        "temporalPrecision INTEGER not null, " +
                        //Grid Data
                        "latitude INTEGER not null, " +
                        "longitude INTEGER not null, " +
                        "temperature INTEGER not null, " +
                        "readingDate DATE not null, " +
                        "readingTime TIME not null, " +
                        "PRIMARY KEY (gridId))";

        Statement statement = null;
        try
        {
            statement = connection.createStatement();
            statement.executeUpdate(createString);
        }
        catch (SQLException e)
        {
            printSQLException(e);
        }
        finally
        {
            if (statement != null)
            {
                statement.close();
            }
        }
    }
    
    public void executeSqlGeneral(String SqlStatement) throws SQLException{
    	Statement statement = null;
    	try{
    		statement = connection.createStatement();
    		statement.execute(SqlStatement);
    	}catch(SQLException e){
    		printSQLException(e);
    	}finally{
    		if(statement != null){
    			statement.close();
    		}
    	}
    }
    
    
    private static void printSQLException(SQLException ex)
    {
        for (Throwable e : ex)
        {
            if (e instanceof SQLException)
            {
                SQLException sqlException = (SQLException) e;
                if ( !ignoreSQLException(sqlException) )
                {
                    e.printStackTrace(System.out);
                    System.out.println("SQLState: " + ((SQLException) e).getSQLState());
                    System.out.println("Error Code: " + ((SQLException) e).getErrorCode());
                    System.out.println("Message: " + e.getMessage());
                    Throwable t = ex.getCause();
                    while (t != null)
                    {
                        System.out.println("Cause: " + t);
                        t = t.getCause();
                    }
                }
            }
        }
    }

    public static boolean ignoreSQLException(SQLException sqlException)
    {
        String sqlState = sqlException.getSQLState();
        if (sqlState == null)
        {
            System.out.println("The SQL state is not defined!");
            return false;
        }
        // X0Y32: Jar file already exists in schema
        if (sqlState.equalsIgnoreCase("X0Y32"))
            return true;
        // 42Y55: Table already exists in schema
        if (sqlState.equalsIgnoreCase("42Y55"))
            return true;
        if (sqlState.equalsIgnoreCase("42S01"))
        	return true;
        return false;
    }
}
