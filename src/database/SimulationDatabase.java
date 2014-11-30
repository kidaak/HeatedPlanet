package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.h2.jdbc.JdbcSQLException;

/**
 * Created by David Welker on 11/20/14.
 */
public class SimulationDatabase
{
    private static Connection connection;
    private static final SimulationDatabase sdb;
    
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:split:~/PlanetSim";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    
    private static final String SIM_NAME_INDEX_SQL = "CREATE INDEX simName_INDEX ON Simulation(name)";
    private static final String SIM_ENDDATE_INDEX_SQL = "CREATE INDEX simEndDate_INDEX ON Simulation(simEndDate)";
    private static final String GRID_GRIDDATE_INDEX_SQL = "CREATE INDEX gridDate_INDEX ON Grid(gridDate)";
    private static final String GRID_SIMULATIONFID_INDEX_SQL = "CREATE INDEX simulationFid_INDEX ON Grid(simulationFid)";
    private static final String GRID_SIMULATIONFID_FOREIGNKEY_SQL = "CONSTRAINT Grid2Simulation FOREIGN KEY (simulationFid) "+
    																"REFERENCES Simulation(simulationId) "+
    																"ON DELETE CASCADE ON UPDATE CASCADE";
    private static final String GRID_SIMFIDGRIDDATE_INDEX_SQL = "CREATE INDEX simulationFidAndGridDate_INDEX ON Grid(simulationFid,gridDate)";
    
    
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
		}catch (JdbcSQLException e){
			if(e.getErrorCode() == 90020)
				JOptionPane.showMessageDialog(null, "The Database is in use by another application. Please close that application and restart PlanetSim.Demo.");
				conn = null;
		} catch (SQLException e) {
			printSQLException(e);
		} 
        sdb = new SimulationDatabase(conn);
    	try {
    		sdb.createSimulationTable();
    		sdb.createGridTable();
		} catch (SQLException e) {
			if(!ignoreSQLException(e))
				printSQLException(e);
		}
    }
    
    private SimulationDatabase(Connection c){
    	SimulationDatabase.connection = c;
    }
    
    public static SimulationDatabase getSimulationDatabase(){
    	return sdb;
    }
    
    public Connection getConnection(){
    	return connection;
    }
    
    private void createSimulationTable() throws SQLException
    {
        String createSimulationTableString =
                "CREATE TABLE Simulation " +
                        "(simulationId INTEGER UNSIGNED NOT NULL AUTO_INCREMENT, " +
                        //Physical Factors
                        "axialTilt DOUBLE NOT NULL," +
                        "eccentricity DOUBLE NOT NULL, " +
                        //Simulation Settings
                        "name VARCHAR(32) NOT NULL, " +
                        "gridSpacing INTEGER NOT NULL, " +
                        "simTimeStep INTEGER NOT NULL, " +
                        "simLength INTEGER NOT NULL, " +
                        "simEndDate TIMESTAMP NOT NULL," +
                        //Invocation Parameters
                        "precision INTEGER NOT NULL, " +
                        "geoPrecision INTEGER NOT NULL, " +
                        "timePrecision INTEGER NOT NULL, " +
                        //Indices
                        "PRIMARY KEY (simulationId), "+
                        "UNIQUE INDEX simId_UNIQUE (simulationId ASC)," +
                        "UNIQUE INDEX name_UNIQUE (name ASC)" +
                        ")";
        
        executeSqlGeneral(createSimulationTableString);
	    executeSqlGeneral(SIM_NAME_INDEX_SQL);
	    executeSqlGeneral(SIM_ENDDATE_INDEX_SQL);
    }
    
    private void createGridTable() throws SQLException
    {
        String createGridTableString =
                "CREATE TABLE Grid " +
                        "(gridId INTEGER UNSIGNED NOT NULL AUTO_INCREMENT, " +
                        //Grid Properties
                        "grid BLOB NOT NULL," +
                        "gridDate TIMESTAMP NOT NULL," +
                        "simulationFid INTEGER UNSIGNED NOT NULL," +
                        //Indices
                        "PRIMARY KEY (gridId),"+
                        "UNIQUE INDEX gridId_UNIQUE (gridId ASC)," +
                        GRID_SIMULATIONFID_FOREIGNKEY_SQL +
                        ")";

        executeSqlGeneral(createGridTableString);
        executeSqlGeneral(GRID_GRIDDATE_INDEX_SQL);
        executeSqlGeneral(GRID_SIMULATIONFID_INDEX_SQL);
        executeSqlGeneral(GRID_SIMFIDGRIDDATE_INDEX_SQL);
    }
    
    private void executeSqlGeneral(String SqlStatement) throws SQLException{
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
        // 42S11: Index already exists in schema
        if (sqlState.equalsIgnoreCase("42S11"))
        	return true;
        return false;
    }
    
    public void resetDatabase() throws SQLException{
    	sdb.executeSqlGeneral("DROP ALL OBJECTS");
    	sdb.createSimulationTable();
		sdb.createGridTable();
    }
}
