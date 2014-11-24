package dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import dao.EarthGridProperties.EarthGridProperty;
import dao.interfaces.IEarthGridDao;
import database.SimulationDatabase;

public class EarthGridDao implements IEarthGridDao {
	
	private static final EarthGridDao instance;
	private static final SimulationDatabase sdb;
	
	//SQL Statements
	private static final String GetSimulationIdFromNameSql = "SELECT simulationId FROM Simulation WHERE name = ?";
	private static final String InsertSimulationSql = "INSERT INTO Simulation "+
							"(name,axialTilt,eccentricity,gridSpacing,simTimeStep,simLength,precision,geoPrecision,timePrecision,simEndDate) "+
							"VALUES (?,?,?,?,?,?,?,?,?,?)";
	private static final String InsertGridSql = "INSERT INTO Grid (Grid,gridDate,simulationFid) VALUES (?,?,?)";
	private static final String QueryGridSqlStart = "SELECT * FROM Simulation AS S WHERE ";
	private static final String QueryGridSqlJoin = " JOIN Grid AS G ON S.simulationId = g.simulationFid";
	private static final String QueryGridByFidSql = "SELECT * FROM Grid WHERE simulationFid = ? ";

	//Static block initialization...
	static {
		try{
			instance = new EarthGridDao();
			sdb = SimulationDatabase.getSimulationDatabase();
			if(sdb == null){
				throw new Exception("GridTable is Null");
			}
		}catch (Exception e){
			throw new RuntimeException("Failed to create DAO", e);
		}
	}
	
	public static EarthGridDao getEarthGridDao(){
		return instance;
	}
	
	private EarthGridDao(){
	}


	@Override
	//TODO
	public EarthGridResponse queryEarthGridSimulation(EarthGridQuery query) {
		
		return EarthGridResponse.EarthGridResponseFactory(ResponseType.ERROR,null,null,query);
	}
	
	@Override
	//TODO
	public EarthGridResponse queryEarthGridSimulationByName(String name)
			throws SQLException {
		
		int simId = getSimulationIdFromName(name);
		
		PreparedStatement simStmt = sdb.getConnection().prepareStatement(QueryGridByFidSql);
		simStmt.setInt(1,simId);
		ResultSet rs = simStmt.executeQuery();
		
		while(rs.next()){
			System.out.println(rs.toString() );
			System.out.println(String.valueOf(rs.getInt("gridId")) );
		}
		
		return null;
	}

	@Override
	public ResponseType insertEarthGridSimulation(EarthGridInsert insert) throws NumberFormatException, SQLException, IOException {
		// Insert Simulation
		
		//Set Statement values based on the EarthGridProperties object
		EarthGridProperties props = insert.getProperties();
		PreparedStatement simStmt = sdb.getConnection().prepareStatement(InsertSimulationSql);
		simStmt.setString(1, props.getProperty(EarthGridProperty.NAME));
		simStmt.setDouble(2, Double.valueOf(props.getProperty(EarthGridProperty.AXIAL_TILT)));
		simStmt.setDouble(3, Double.valueOf(props.getProperty(EarthGridProperty.ECCENTRICITY)));
		simStmt.setInt(4, Integer.valueOf(props.getProperty(EarthGridProperty.GRID_SPACING)));
		simStmt.setInt(5, Integer.valueOf(props.getProperty(EarthGridProperty.SIMULATION_TIME_STEP)));
		simStmt.setInt(6, Integer.valueOf(props.getProperty(EarthGridProperty.SIMULATION_LENGTH)));
		simStmt.setInt(7, Integer.valueOf(props.getProperty(EarthGridProperty.PRECISION)));
		simStmt.setInt(8, Integer.valueOf(props.getProperty(EarthGridProperty.GEO_PRECISION)));
		simStmt.setInt(9, Integer.valueOf(props.getProperty(EarthGridProperty.TIME_PRECISION)));
		simStmt.setTimestamp(10, new Timestamp(insert.getEndDate().getTimeInMillis()), insert.getEndDate());
		simStmt.execute();
		
		//Get the ID of the newly inserted Simulation
		int newSimId = getSimulationIdFromName(props.getProperty(EarthGridProperty.NAME));
		
		//Insert Each Grid using Simulation ID
		int numGrids = insert.getAllGrids().length;
		PreparedStatement gridStmt = sdb.getConnection().prepareStatement(InsertGridSql);
		for(int i = 0; i < numGrids; i++){
			//Created the BLOB for the Grid object
			Blob gridBlob = sdb.getConnection().createBlob();
			//Convert the Grid into a Byte Array
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bOut);
			out.writeObject(insert.getGridAt(i));
			out.close();
			byte[] objBytes = bOut.toByteArray();
			//Set the BLOB value
			gridBlob.setBytes(1, objBytes);
			//Set Statement Values
			gridStmt.setBlob(1, gridBlob);
			gridStmt.setTimestamp(2, new Timestamp(insert.getGridDateAt(i).getTimeInMillis()),insert.getGridDateAt(i));
			gridStmt.setInt(3, newSimId);
			//For performance, insert in batches
			gridStmt.addBatch();
			//Execute Batch every 1000 records
			if(i % 1000 == 0){
				gridStmt.executeBatch();
			}
		}
		//Execute final batch
		gridStmt.executeBatch();
		
		return ResponseType.INSERTSUCCESS;
	}


	@Override
	public boolean isNameUnique(String name) throws SQLException {
		
		Statement stmt = null;
		ResultSet rs = null;
		String sqlString = "select count(*) as count from Simulation where name = '" + name + "'";
		
		stmt = sdb.getConnection().createStatement();
		// get the count of the number of rows for the specified name 
		rs = stmt.executeQuery(sqlString);
		while (rs.next()) {
			int counter = rs.getInt("count");
			if (counter == 0) {
				return true;
			}
		}
		return false;
	}


	@Override
	public String[] getAllNames() throws SQLException {
		
		Statement stmt = null;
		ResultSet rs = null;
		String sqlString = "select name from Simulation";
		ArrayList<String> names = new ArrayList<String>();
		String[] stringArray = {};
		
		stmt = sdb.getConnection().createStatement();
		//create an array of Strings of count size counter
		rs = stmt.executeQuery(sqlString);
		
		while(rs.next()){
			names.add(rs.getString("name"));
		}
		
		return names.toArray(stringArray);
	}


	@Override
	public void resetDatabase(int secretCode) throws SQLException {
		if(secretCode == 42){
			sdb.executeSqlGeneral("DROP TABLE IF EXISTS Grid");
	        sdb.executeSqlGeneral("DROP TABLE IF EXISTS Simulation");
		}
	}


	@Override
	public int getSimulationIdFromName(String name) throws SQLException {
		
		int id = -1;
		
		PreparedStatement stmt = sdb.getConnection().prepareStatement(GetSimulationIdFromNameSql);
		stmt.setString(1, name);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()){
			id = rs.getInt("simulationId");
		}
		
		return id;
	}
	
	
	
}
