package dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.EarthGridProperties;
import common.Grid;
import common.EarthGridProperties.EarthGridProperty;
import common.IGrid;
import dao.interfaces.IEarthGridDao;
import database.SimulationDatabase;

public class EarthGridDao implements IEarthGridDao {
	
	private static final EarthGridDao instance;
	private static final SimulationDatabase sdb;
	
	//RegEx to find the row count from a ResultSet toString
	private static final Pattern rowsPattern = Pattern.compile("rows: (\\d+)");
	
	//Canned SQL Statements
	private static final String GetSimulationIdFromNameSql = "SELECT simulationId FROM Simulation WHERE name = ?";
	private static final String GetSimulationFieldsFromIdSql = "SELECT * FROM Simulation WHERE simulationId = ?";
	private static final String InsertSimulationSql = "INSERT INTO Simulation "+
							"(name,axialTilt,eccentricity,gridSpacing,simTimeStep,simLength,precision,geoPrecision,timePrecision,simEndDate) "+
							"VALUES (?,?,?,?,?,?,?,?,?,?)";
	private static final String InsertGridSql = "INSERT INTO Grid (Grid,gridDate,simulationFid) VALUES (?,?,?)";
	private static final String QueryGridSqlStart = "SELECT * FROM Simulation AS S LEFT JOIN Grid AS G ON S.simulationId = G.simulationFid ";
	//private static final String QueryGridByFidSql = "SELECT * FROM Grid WHERE simulationFid = ? ";
	//private static final String QueryGridBySimName = "SELECT * FROM Simulation AS S LEFT JOIN Grid AS G ON S.simulationId = G.simulationFid WHERE S.name = ?";

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
	public EarthGridResponse queryEarthGridSimulation(EarthGridQuery query) 
			throws SQLException, NumberFormatException, ClassNotFoundException, IOException {
		
		EarthGridProperties props = query.getProperties();
		EarthGridProperty[] definedProps = props.definedProperties();
		
		//StringBuilder for WHERE statement
		StringBuilder sb = new StringBuilder("WHERE ");
		
		//Loop through all the defined properties and build the WHERE clause
		for(int i = 0; i<definedProps.length; i++){
			//Get the Property Type
			EarthGridProperty propType = definedProps[i];
			//Check if the property is a String Property
			if(EarthGridProperties.arrayContains(EarthGridProperties.EarthGridStringProperties, propType)){
				String value = props.getPropertyString(propType).trim();
				switch(propType){
					case NAME:
						//Do basic validation on the value
						if(value == null || value.trim().length() == 0)
							throw new IllegalArgumentException("Simulation name is empty");
						//Add to the WHERE Clause using StringBuilder
						sb.append("Name = ? AND ");
						break;
					default:
						throw new IllegalArgumentException(propType.name()+" is not expecting a string.");
				}
			}else if(EarthGridProperties.arrayContains(EarthGridProperties.EarthGridIntProperties, propType)){
				switch(propType){
					case GRID_SPACING:
						//Add to the WHERE Clause using StringBuilder
						sb.append("gridSpacing = ? AND ");
						break;
					case SIMULATION_TIME_STEP:
						//Add to the WHERE Clause using StringBuilder
						sb.append("simTimeStep = ? AND ");
						break;
					case SIMULATION_LENGTH:
						//Add to the WHERE Clause using StringBuilder
						sb.append("simLength = ? AND ");
						break;
					case PRECISION:
						//Add to the WHERE Clause using StringBuilder
						sb.append("precision = ? AND ");
						break;
					case GEO_PRECISION:
						//Add to the WHERE Clause using StringBuilder
						sb.append("geoPrecision = ? AND ");
						break;
					case TIME_PRECISION:
						//Add to the WHERE Clause using StringBuilder
						sb.append("timePrecision = ? AND ");
						break;
					default:
						throw new IllegalArgumentException(propType.name()+" is not expecting an integer.");
				}
			}else if(EarthGridProperties.arrayContains(EarthGridProperties.EarthGridFloatProperties, propType)){
				switch(propType){
					case AXIAL_TILT:
						//Add to the WHERE Clause using StringBuilder
						sb.append("axialTilt = ? AND ");
						break;
					case ECCENTRICITY:
						//Add to the WHERE Clause using StringBuilder
						sb.append("eccentricity = ? AND ");
						break;
					default:
						throw new IllegalArgumentException(propType.name()+" is not expecting a double.");
				}
			}else if(EarthGridProperties.arrayContains(EarthGridProperties.EarthGridCalendarProperties, propType)){
				switch(propType){
					case START_DATE:
						//Add to the WHERE Clause using StringBuilder
						sb.append("G.gridDate >= ? AND ");
						break;
					case END_DATE:
						//Add to the WHERE Clause using StringBuilder
						sb.append("G.gridDate <= ? AND ");
						break;
					default:
						throw new IllegalArgumentException(propType.name()+" is not expecting a Calendar.");
				}		
			}else{
				throw new IllegalArgumentException("Somehow EarthGridProperty "+propType.name()+" is not in the list for types");
			}
		}
		//Get rid of trailing " AND "
		sb.replace(sb.length()-5, sb.length(), "");
		
		if(props.isPropertyDefined(EarthGridProperty.END_DATE)){
			sb.append(" AND S.simEndDate >= ?");
		}
		
		PreparedStatement simStmt = sdb.getConnection().prepareStatement(QueryGridSqlStart+sb.toString());
		
		for(int i = 0; i<definedProps.length; i++){
			//Get the Property Type
			EarthGridProperty propType = definedProps[i];
			//Check if the property is a String Property
			if(EarthGridProperties.arrayContains(EarthGridProperties.EarthGridStringProperties, propType)){
				String value = props.getPropertyString(propType).trim();
				switch(propType){
					case NAME:
						//Add the argument to the list to be used later in the PreparedStatement
						simStmt.setString(i+1, value);
						break;
					default:
						throw new IllegalArgumentException(propType.name()+" is not expecting a string.");
				}
			}else 
				//Check if the property is a Integer Property
				if(EarthGridProperties.arrayContains(EarthGridProperties.EarthGridIntProperties, propType)){
					int value = props.getPropertyInt(propType);
					switch(propType){
						case GRID_SPACING:
							//Add the argument to the list to be used later in the PreparedStatement
							simStmt.setInt(i+1, value);
							break;
						case SIMULATION_TIME_STEP:
							//Add the argument to the list to be used later in the PreparedStatement
							simStmt.setInt(i+1, value);
							break;
						case SIMULATION_LENGTH:
							//Add the argument to the list to be used later in the PreparedStatement
							simStmt.setInt(i+1, value);
							break;
						case PRECISION:
							//Add the argument to the list to be used later in the PreparedStatement
							simStmt.setInt(i+1, value);
							break;
						case GEO_PRECISION:
							//Add the argument to the list to be used later in the PreparedStatement
							simStmt.setInt(i+1, value);
							break;
						case TIME_PRECISION:
							//Add the argument to the list to be used later in the PreparedStatement
							simStmt.setInt(i+1, value);
							break;
						default:
							throw new IllegalArgumentException(propType.name()+" is not expecting an integer.");
				}
			}else 
				//Check if the property is a Float Property
				if(EarthGridProperties.arrayContains(EarthGridProperties.EarthGridFloatProperties, propType)){
					float value = props.getPropertyFloat(propType);
					switch(propType){
						case AXIAL_TILT:
							//Add the argument to the list to be used later in the PreparedStatement
							simStmt.setFloat(i+1, value);
							break;
						case ECCENTRICITY:
							//Add the argument to the list to be used later in the PreparedStatement
							simStmt.setFloat(i+1, value);
							break;
						default:
							throw new IllegalArgumentException(propType.name()+" is not expecting a double.");
					}
			}else 
				//Check if the property is a Calendar Property
				if(EarthGridProperties.arrayContains(EarthGridProperties.EarthGridCalendarProperties, propType)){
					//Handle Calendar/Timestamp Time Zones
					Calendar value = props.getPropertyCalendar(propType);
					Calendar calConvert = convertToLocal(value);
					Timestamp tsInsert = Calendar2Timestamp(calConvert);
					switch(propType){
						case START_DATE:
							simStmt.setTimestamp(i+1, tsInsert);
							break;
						case END_DATE:
							simStmt.setTimestamp(i+1, tsInsert);
							break;
						default:
							throw new IllegalArgumentException(propType.name()+" is not expecting a Calendar.");	
				}
			}else{
				throw new IllegalArgumentException("Somehow EarthGridProperty "+propType.name()+" is not in the list for types");
			}
		}
		
		if(props.isPropertyDefined(EarthGridProperty.END_DATE)){
			//Handle Calendar/Timestamp Time Zones
			Calendar value = props.getPropertyCalendar(EarthGridProperty.END_DATE);
			Calendar calConvert = convertToLocal(value);
			Timestamp tsInsert = Calendar2Timestamp(calConvert);
			simStmt.setTimestamp(definedProps.length+1, tsInsert);
		}
		
		ResultSet rs = simStmt.executeQuery();
		EarthGridResponse response = ResultSet2EarthGridResponse(rs, query);
		
		rs.close();
		simStmt.close();
		
		return response;
	}
	
	@Override
	public EarthGridResponse queryEarthGridSimulationByName(String name)
			throws SQLException, IOException, ClassNotFoundException {
		
		EarthGridProperties props = new EarthGridProperties();
		props.setProperty(EarthGridProperty.NAME, name);
		
		EarthGridQuery egq = new EarthGridQuery(props);
		return queryEarthGridSimulation(egq);
	}

	@Override
	public ResponseType insertEarthGridSimulation(EarthGridInsert insert) 
			throws NumberFormatException, SQLException, IOException, IllegalArgumentException {
		// Insert Simulation
		IGrid[] allGrids = insert.getAllGrids();
		int numGrids = allGrids.length;
		EarthGridProperties simProps = insert.getProperties();
		String simName = simProps.getPropertyString(EarthGridProperty.NAME);
		
		if(numGrids == 0)
			throw new IllegalArgumentException("EarthGridInsert does not contain any Grids to insert.");
		if(isNameUnique(simName)){
			//Set Statement values based on the EarthGridProperties object
			
			PreparedStatement simStmt = sdb.getConnection().prepareStatement(InsertSimulationSql);
			simStmt.setString(1, simName);
			simStmt.setDouble(2, Double.valueOf(simProps.getPropertyString(EarthGridProperty.AXIAL_TILT)));
			simStmt.setDouble(3, Double.valueOf(simProps.getPropertyString(EarthGridProperty.ECCENTRICITY)));
			simStmt.setInt(4, Integer.valueOf(simProps.getPropertyString(EarthGridProperty.GRID_SPACING)));
			simStmt.setInt(5, Integer.valueOf(simProps.getPropertyString(EarthGridProperty.SIMULATION_TIME_STEP)));
			simStmt.setInt(6, Integer.valueOf(simProps.getPropertyString(EarthGridProperty.SIMULATION_LENGTH)));
			simStmt.setInt(7, Integer.valueOf(simProps.getPropertyString(EarthGridProperty.PRECISION)));
			simStmt.setInt(8, Integer.valueOf(simProps.getPropertyString(EarthGridProperty.GEO_PRECISION)));
			simStmt.setInt(9, Integer.valueOf(simProps.getPropertyString(EarthGridProperty.TIME_PRECISION)));
			//Insert Calendar as Timestamp, converting Time Zone along the way
			Calendar calInsert = insert.getEndDate();
			Calendar calConvert = convertToLocal(calInsert);
			Timestamp tsInsert = Calendar2Timestamp(calConvert);
			simStmt.setTimestamp(10, tsInsert, calConvert);
			
			try{
				simStmt.execute();
			}catch(SQLException e){
				String sqlState = e.getSQLState();
				if(!sqlState.equalsIgnoreCase("23505"))
					return ResponseType.ERROR_DUPLICATE;
				throw new SQLException(e);
			}finally{
				if(simStmt != null)
					simStmt.close();
			}
		}
		//Get the ID of the newly inserted Simulation
		int newSimId = getSimulationIdFromName(simName);
		
		//Insert Each Grid using Simulation ID
		
		PreparedStatement gridStmt = sdb.getConnection().prepareStatement(InsertGridSql);
		for(int i = 0; i < numGrids; i++){
			//Set Statement Values
			gridStmt.setBlob(1, Grid2Blob(allGrids[i]));
			
			//Handle Calendar/Timestamps with Time Zones
			Calendar calGridInsert = insert.getGridDateAt(i);
			Calendar calGridConvert = convertToLocal(calGridInsert);
			Timestamp tsGridInsert = Calendar2Timestamp(calGridConvert);
			gridStmt.setTimestamp(2, tsGridInsert,calGridConvert);
			
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
		gridStmt.close();
		
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
			if (counter == 0){
				rs.close();
				stmt.close();
				return true;
			}
		}
		rs.close();
		stmt.close();
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
		rs = stmt.executeQuery(sqlString);
		
		while(rs.next()){
			names.add(rs.getString("name"));
		}
		rs.close();
		stmt.close();
		return names.toArray(stringArray);
	}

	@Override
	public void resetDatabase(String secretCode) throws SQLException {
		if(secretCode.equals("42") ){
	        sdb.resetDatabase();
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
		rs.close();
		stmt.close();
		return id;
	}
	
	/**
	 * This method takes a ResultSet from a query specified by an EarthGridQuery and transforms it
	 * into an EarthGridResponse. If results from more than one simulation are found, only results
	 * from the "most populous" simulation will be returned.
	 * 
	 * @param results A ResultSet
	 * @param query The EarthGridQuery object that created the ResultSet
	 * @return The EarthGridResponse object containing the results from the ResultSet
	 * @throws NumberFormatException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	private EarthGridResponse ResultSet2EarthGridResponse(ResultSet results, EarthGridQuery query) 
			throws NumberFormatException, ClassNotFoundException, SQLException, IOException{
		
		EarthGridResponse egr;		
		
		if(results.isBeforeFirst()){
			//Extract number of rows from the ResultSet toString
			Matcher m = rowsPattern.matcher(results.toString());
			m.find();
			int numRows = Integer.valueOf(m.group(1));
			
			//Create arrays of appropriate size
			ArrayList<Grid> grids = new ArrayList<Grid>(numRows);
			ArrayList<Calendar> gridDates = new ArrayList<Calendar>(numRows);
			int[] simIds = new int[numRows];
			int[] simSortIds = new int[numRows];
			
			int count = 0;
			
			while(results.next()){
				simIds[count] = results.getInt("simulationId");
				simSortIds[count] = simIds[count];
				grids.add(Blob2Grid(results.getBlob("Grid")) );
				gridDates.add(Timestamp2Calendar(results.getTimestamp("gridDate")) );
				count++;
			}
			
			//Determine which Simulation returns the most results (grids)
			Arrays.sort(simSortIds);
			int prevId = simSortIds[0];
			int maxId = -1;
			int maxCount = -1;
			int curCount = 1;
			for(int i = 1; i<simSortIds.length; i++){
				if(simSortIds[i] == prevId){
					curCount++;
				}else{
					if(curCount > maxCount){
						maxCount = curCount;
						maxId = prevId;
					}
					curCount = 1;
					prevId = simSortIds[i];
				}
			}
			if(curCount > maxCount){
				maxCount = curCount;
				maxId = prevId;
			}
			
			//Remove any Grids that aren't from the biggest simulation
			for(int i = simIds.length-1; i>=0;i--){
				if(simIds[i] != maxId){
					grids.remove(i);
					gridDates.remove(i);
				}
			}
			
			//Fix the sorting of the arrays
			Grid[] finalGrids = grids.toArray(new Grid[grids.size()]);
			Arrays.sort(finalGrids);
			Calendar[] finalGridDates = gridDates.toArray(new Calendar[gridDates.size()]);
			Arrays.sort(finalGridDates);
			
			//Get the Simulation Properties
			EarthGridProperties props = new EarthGridProperties();
			PreparedStatement ps = sdb.getConnection().prepareStatement(GetSimulationFieldsFromIdSql);
			ps.setInt(1, maxId);
			ResultSet simResults = ps.executeQuery();
			if(simResults.next()){
				props.setProperty(EarthGridProperty.NAME, simResults.getString("name"));
				props.setProperty(EarthGridProperty.AXIAL_TILT, simResults.getDouble("axialTilt"));
				props.setProperty(EarthGridProperty.ECCENTRICITY, simResults.getDouble("eccentricity"));
				props.setProperty(EarthGridProperty.GRID_SPACING, simResults.getInt("gridSpacing"));
				props.setProperty(EarthGridProperty.SIMULATION_TIME_STEP, simResults.getInt("simTimeStep"));
				props.setProperty(EarthGridProperty.SIMULATION_LENGTH, simResults.getInt("simLength"));
				props.setProperty(EarthGridProperty.PRECISION, simResults.getInt("precision"));
				props.setProperty(EarthGridProperty.GEO_PRECISION, simResults.getInt("geoPrecision"));
				props.setProperty(EarthGridProperty.TIME_PRECISION, simResults.getInt("timePrecision"));
				props.setProperty(EarthGridProperty.END_DATE, convertToUTC(Timestamp2Calendar(simResults.getTimestamp("simEndDate"))) );
				
			}else{
				throw new SQLException("Really F'ed up here. Couldn't find the Simulation ID after finding the Simulation ID.");
			}
			
			if(count > 1){
				egr = EarthGridResponse.EarthGridResponseFactory(
						ResponseType.FOUND_MANY, finalGrids, finalGridDates, props);
			}else if(count == 1){
				egr = EarthGridResponse.EarthGridResponseFactory(
						ResponseType.FOUND_ONE, finalGrids, finalGridDates, props);
			}else{
				//Should not get here...
				egr = EarthGridResponse.EarthGridResponseFactory(ResponseType.NOTFOUND, null, null, props);
			}
		}else{
			egr = EarthGridResponse.EarthGridResponseFactory(ResponseType.NOTFOUND, null, null, null);
		}
		
		return egr;
	}
	
	private Grid Blob2Grid(Blob b) throws IOException, ClassNotFoundException, SQLException{
		byte[] tempGrid = b.getBytes(1, (int) b.length());
		ByteArrayInputStream in = new ByteArrayInputStream(tempGrid);
	    ObjectInputStream is = new ObjectInputStream(in);
	    return (Grid) is.readObject();
	}
	
	private Blob Grid2Blob(IGrid g) throws IOException, SQLException{
		//Create empty BLOB for the Grid object
		Blob b = sdb.getConnection().createBlob();
		//Convert the Grid into a Byte Array
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bOut);
		out.writeObject(g);
		out.close();
		byte[] objBytes = bOut.toByteArray();
		//Set the BLOB value
		b.setBytes(1, objBytes);
		bOut.close();
		return b;
	}
	
	private Calendar Timestamp2Calendar(Timestamp t){
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		c.setTimeInMillis(t.getTime());
		return c;
	}
	
	private Timestamp Calendar2Timestamp(Calendar c){
		Timestamp t = new Timestamp(c.getTimeInMillis());
		return t;
	}
	
	private Calendar convertToUTC(Calendar c){
		if(c.get(Calendar.ZONE_OFFSET) != 0){
			c.setTimeZone(TimeZone.getTimeZone("UTC"));
		}
		return c;
	}
	
	private Calendar convertToLocal(Calendar c){
		if(c.get(Calendar.ZONE_OFFSET) == 0){
			c.setTimeZone(TimeZone.getDefault());
		}
		return c;
	}
}
