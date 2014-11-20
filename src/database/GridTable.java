package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by David Welker on 11/20/14.
 */
public class GridTable
{
    private final Connection connection;

    public GridTable(Connection connection)
    {
        this.connection = connection;
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
        return false;
    }
}
