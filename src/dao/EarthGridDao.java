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
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.EarthGridProperties;
import common.Grid;
import common.EarthGridProperties.EarthGridProperty;
import dao.interfaces.IEarthGridDao;
import database.SimulationDatabase;

public class EarthGridDao implements IEarthGridDao {
	
	private static final EarthGridDao instance;
	private static final SimulationDatabase sdb;
	
	//RegEx to find the row count from a ResultSet toString
	private static final Pattern rowsPattern = Pattern.compile("rows: (\\d+)");
	
	//Canned SQL Statements
	private static final String GetSimulationIdFromNameSql = "SELECT simulationId FROM Simulation WHERE name = ?";
	private static final String InsertSimulationSql = "INSERT INTO Simulation "+
							"(name,axialTilt,eccentricity,gridSpacing,simTimeStep,simLength,precision,geoPrecision,timePrecision,simEndDate) "+
							"VALUES (?,?,?,?,?,?,?,?,?,?)";
	private static final String InsertGridSql = "INSERT INTO Grid (Grid,gridDate,simulationFid) VALUES (?,?,?)";
	private static final String QueryGridSqlStart = "SELECT * FROM Simulation AS S LEFT JOIN Grid AS G ON S.simulationId = G.simulationFid ";
	private static final String QueryGridByFidSql = "SELECT * FROM Grid WHERE simulationFid = ? ";
	private static final String QueryGridBySimName = "SELECT * FROM Simulation AS S LEFT JOIN Grid AS G ON S.simulationId = G.simulationFid WHERE S.name = ?";

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
		//Ordered list of arguments
		ArrayList<String> args = new ArrayList<String>(definedProps.length);
		
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
						//Add the argument to the list to be used later in the PreparedStatement
						args.add(value);
						break;
					default:
						throw new IllegalArgumentException(propType.name()+" is not expecting a string.");
				}
			}else 
				//Check if the property is a Integer Property
				if(EarthGridProperties.arrayContains(EarthGridProperties.EarthGridIntProperties, propType)){
//TODO VERIFY
				int value = props.getPropertyInt(propType);
				switch(propType){
					case GRID_SPACING:
						//Do basic validation on the value
						if(value <= 0)
							throw new IllegalArgumentException("Grid Spacing must be >= 1");
						//Add to the WHERE Clause using StringBuilder
						sb.append("gridSpacing = ? AND ");
						//Add the argument to the list to be used later in the PreparedStatement
						args.add(Integer.toString(value));
						break;
					case SIMULATION_TIME_STEP:
						//Do basic validation on the value
						if(value <= 0)
							throw new IllegalArgumentException("Simulation Time Step must be >= 1");
						//Add to the WHERE Clause using StringBuilder
						sb.append("simTimeStep = ? AND ");
						//Add the argument to the list to be used later in the PreparedStatement
						args.add(Integer.toString(value));
						break;
					case SIMULATION_LENGTH:
						//Do basic validation on the value
						if(value <= 0)
							throw new IllegalArgumentException("Simulation Length must be >= 1");
						//Add to the WHERE Clause using StringBuilder
						sb.append("simLength = ? AND ");
						//Add the argument to the list to be used later in the PreparedStatement
						args.add(Integer.toString(value));
						break;
					case PRECISION:
						//Do basic validation on the value
						if(value <= 0)
							throw new IllegalArgumentException("Precision must be >= 1");
						//Add to the WHERE Clause using StringBuilder
						sb.append("precision = ? AND ");
						//Add the argument to the list to be used later in the PreparedStatement
						args.add(Integer.toString(value));
						break;
					case GEO_PRECISION:
						//Do basic validation on the value
						if(value <= 0)
							throw new IllegalArgumentException("Geographic Precision must be >= 1");
						//Add to the WHERE Clause using StringBuilder
						sb.append("geoPrecision = ? AND ");
						//Add the argument to the list to be used later in the PreparedStatement
						args.add(Integer.toString(value));
						break;
					case TIME_PRECISION:
						//Do basic validation on the value
						if(value <= 0)
							throw new IllegalArgumentException("Time Precision must be >= 1");
						//Add to the WHERE Clause using StringBuilder
						sb.append("timePrecision = ? AND ");
						//Add the argument to the list to be used later in the PreparedStatement
						args.add(Integer.toString(value));
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
						if(value < -180 || value > 180)
							throw new IllegalArgumentException("The Axial Tilt must be between -180 and 180 degrees");
						//Add to the WHERE Clause using StringBuilder
						sb.append("axialTilt = ? AND ");
						//Add the argument to the list to be used later in the PreparedStatement
						args.add(Float.toString(value));
						break;
					case ECCENTRICITY:
						if(value < 0 || value >= 1)
							throw new IllegalArgumentException("The eccentricity must be between 0 and 1, but strictly less than 1.00");
						//Add to the WHERE Clause using StringBuilder
						sb.append("eccentricity = ? AND ");
						//Add the argument to the list to be used later in the PreparedStatement
						args.add(Float.toString(value));
						break;
// we don't need a case for presentation rate here, correct?
					default:
						throw new IllegalArgumentException(propType.name()+" is not expecting a double.");
				}
			}else 
				//Check if the property is a Calendar Property
				if(EarthGridProperties.arrayContains(EarthGridProperties.EarthGridCalendarProperties, propType)){
				Calendar value = props.getPropertyCalendar(propType);
				switch(propType){
// we don't need a case for start date here, correct?
					case END_DATE:
						if(value == null)
							throw new IllegalArgumentException("End Date is empty");
						//Add to the WHERE Clause using StringBuilder
						sb.append("simEndDate = ? AND ");
						//Add the argument to the list to be used later in the PreparedStatement
						args.add(String.valueOf(value.getTimeInMillis()));
						break;
					default:
						throw new IllegalArgumentException(propType.name()+" is not expecting a Calendar.");
				}		
//TODO END VERIFY
			}else{
				throw new IllegalArgumentException("Somehow EarthGridProperty "+propType.name()+" is not in the list for types");
			}
		}
		//TODO Use these as a basis for filling out the above
		/*
		if(!props.getPropertyString(EarthGridProperty.NAME).trim().equals("")){
			sb.append("Name = ? AND ");
			args.add(props.getPropertyString(EarthGridProperty.NAME).trim());
		}
		if(!props.getPropertyString(EarthGridProperty.AXIAL_TILT).trim().equals("")){
			sb.append("axialTilt = ? AND ");
			args.add(props.getPropertyString(EarthGridProperty.AXIAL_TILT).trim());
		}
		*/
		//Get rid of trailing " AND "
		sb.replace(sb.length()-5, sb.length(), "");
		
		PreparedStatement simStmt = sdb.getConnection().prepareStatement(QueryGridSqlStart+sb.toString());
		//TODO Refactor this to be smarter about how it adds arguments. Should be similar to above, and we can skip the ArrayList args
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
//TODO VERIFY
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
// we don't need a case for presentation rate here, correct?
						default:
							throw new IllegalArgumentException(propType.name()+" is not expecting a double.");
					}
			}else 
				//Check if the property is a Calendar Property
				if(EarthGridProperties.arrayContains(EarthGridProperties.EarthGridCalendarProperties, propType)){
					Calendar value = props.getPropertyCalendar(propType);
					switch(propType){
// we don't need a case for start date here, correct?
						case END_DATE:
							//Add the argument to the list to be used later in the PreparedStatement
// not sure how to format value here
							simStmt.setDate(i+1, value);
							break;
						default:
							throw new IllegalArgumentException(propType.name()+" is not expecting a Calendar.");	
				}
//TODO END VERIFY
			}else{
				throw new IllegalArgumentException("Somehow EarthGridProperty "+propType.name()+" is not in the list for types");
			}
		}
		
		//TODO Use this as a framework for above
		/*
		for(int i = 0; i<args.size(); i++){
			String arg = args.get(i);
			if(isDouble(arg)){
				simStmt.setDouble(i+1, Double.parseDouble(arg) );
			}else if(isInteger(arg)){
				simStmt.setInt(i+1, Integer.parseInt(arg) );
			//}else if(){
				//TODO Need to implement adding Calendar and Timestamp query fields
			}else{
				simStmt.setString(i+1, arg);
			}
		}
		*/
		ResultSet rs = simStmt.executeQuery();
		
		return ResultSet2EarthGridResponse(rs, query);
	}
	
	@Override
	public EarthGridResponse queryEarthGridSimulationByName(EarthGridQuery egq)
			throws SQLException, IOException, ClassNotFoundException {
		
		String name = egq.getName();
		
		PreparedStatement simStmt = sdb.getConnection().prepareStatement(QueryGridBySimName);
		simStmt.setString(1, name);
		
		ResultSet rs = simStmt.executeQuery();
		
		return ResultSet2EarthGridResponse(rs, egq);
	}

	@Override
	public ResponseType insertEarthGridSimulation(EarthGridInsert insert) throws NumberFormatException, SQLException, IOException {
		// Insert Simulation
		
		//Set Statement values based on the EarthGridProperties object
		EarthGridProperties props = insert.getProperties();
		PreparedStatement simStmt = sdb.getConnection().prepareStatement(InsertSimulationSql);
		simStmt.setString(1, props.getPropertyString(EarthGridProperty.NAME));
		simStmt.setDouble(2, Double.valueOf(props.getPropertyString(EarthGridProperty.AXIAL_TILT)));
		simStmt.setDouble(3, Double.valueOf(props.getPropertyString(EarthGridProperty.ECCENTRICITY)));
		simStmt.setInt(4, Integer.valueOf(props.getPropertyString(EarthGridProperty.GRID_SPACING)));
		simStmt.setInt(5, Integer.valueOf(props.getPropertyString(EarthGridProperty.SIMULATION_TIME_STEP)));
		simStmt.setInt(6, Integer.valueOf(props.getPropertyString(EarthGridProperty.SIMULATION_LENGTH)));
		simStmt.setInt(7, Integer.valueOf(props.getPropertyString(EarthGridProperty.PRECISION)));
		simStmt.setInt(8, Integer.valueOf(props.getPropertyString(EarthGridProperty.GEO_PRECISION)));
		simStmt.setInt(9, Integer.valueOf(props.getPropertyString(EarthGridProperty.TIME_PRECISION)));
		simStmt.setTimestamp(10, Calendar2Timestamp(insert.getEndDate()), insert.getEndDate());
		
		try{
			simStmt.execute();
		}catch(SQLException e){
			String sqlState = e.getSQLState();
			if(sqlState.equalsIgnoreCase("23505"))
				return ResponseType.ERROR_DUPLICATE;
			throw new SQLException(e);
		}
		
		//Get the ID of the newly inserted Simulation
		int newSimId = getSimulationIdFromName(props.getPropertyString(EarthGridProperty.NAME));
		
		//Insert Each Grid using Simulation ID
		int numGrids = insert.getAllGrids().length;
		PreparedStatement gridStmt = sdb.getConnection().prepareStatement(InsertGridSql);
		for(int i = 0; i < numGrids; i++){
			//Set Statement Values
			gridStmt.setBlob(1, Grid2Blob(insert.getGridAt(i)));
			gridStmt.setTimestamp(2, Calendar2Timestamp(insert.getGridDateAt(i)),insert.getGridDateAt(i));
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
		rs = stmt.executeQuery(sqlString);
		
		while(rs.next()){
			names.add(rs.getString("name"));
		}
		
		return names.toArray(stringArray);
	}

	@Override
	public void resetDatabase(String secretCode) throws SQLException {
		if(secretCode.equals("42") ){
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
	
	private EarthGridResponse ResultSet2EarthGridResponse(ResultSet results, EarthGridQuery query) 
			throws NumberFormatException, ClassNotFoundException, SQLException, IOException{
		
		EarthGridResponse egr;		
		
		if(results.first()){
			//Extract number of rows from the ResultSet toString
			Matcher m = rowsPattern.matcher(results.toString());
			m.find();
			int numRows = Integer.valueOf(m.group(1))-1;
			
			//Create arrays of appropriate size
			Grid[] grids = new Grid[numRows];
			Calendar[] gridDates = new Calendar[numRows];
			
			int count = 0;
			while(results.next()){
				grids[count] = Blob2Grid(results.getBlob("Grid"));
				gridDates[count] = Timestamp2Calendar(results.getTimestamp("gridDate"));
				count++;
			}
			if(count > 1){
				egr = EarthGridResponse.EarthGridResponseFactory(ResponseType.FOUND_MANY, grids, gridDates, query);
			}else if(count == 1){
				egr = EarthGridResponse.EarthGridResponseFactory(ResponseType.FOUND_ONE, grids, gridDates, query);
			}else{
				egr = EarthGridResponse.EarthGridResponseFactory(ResponseType.NOTFOUND, null, null, query);
			}
		}else{
			egr = EarthGridResponse.EarthGridResponseFactory(ResponseType.NOTFOUND, null, null, query);
		}
		
		return egr;
	}
	
	private Grid Blob2Grid(Blob b) throws IOException, ClassNotFoundException, SQLException{
		byte[] tempGrid = b.getBytes(1, (int) b.length());
		ByteArrayInputStream in = new ByteArrayInputStream(tempGrid);
	    ObjectInputStream is = new ObjectInputStream(in);
	    return (Grid) is.readObject();
	}
	
	private Blob Grid2Blob(Grid g) throws IOException, SQLException{
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
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		return c;
	}
	
	private Timestamp Calendar2Timestamp(Calendar c){
		return new Timestamp(c.getTimeInMillis());
	}
	
	private boolean isDouble(String s){
		try{
			double n = Double.parseDouble(s);
		}catch(NumberFormatException e){
			return false;
		}catch(NullPointerException e){
			return false;
		}
		return true;
	}
	
	private boolean isInteger(String s){
		try{
			int n = Integer.parseInt(s);
		}catch(NumberFormatException e){
			return false;
		}catch(NullPointerException e){
			return false;
		}
		return true;
	}
}
