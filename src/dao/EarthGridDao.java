package dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dao.interfaces.IEarthGridDao;
import database.SimulationDatabase;

public class EarthGridDao implements IEarthGridDao {
	
	private static final EarthGridDao instance;
	private static final SimulationDatabase sdb;
	
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
	public EarthGridResponse queryDatabase(EarthGridQuery query) {
		// TODO Auto-generated method stub
		return EarthGridResponse.EarthGridResponseFactory(ResponseType.ERROR,null,query);
	}


	@Override
	public ResponseType addEarthGrid(EarthGridQuery egq) {
		// TODO Auto-generated method stub
		try{
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bOut);
			out.writeObject(egq.getGrid());
			out.close();
			byte[] objBytes = bOut.toByteArray();
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] md5Byte = md.digest(objBytes);
			String md5String = new sun.misc.BASE64Encoder().encode(md5Byte);
			
		}catch(IOException ex){
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} catch (NoSuchAlgorithmException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		
		return ResponseType.ERROR;
	}


	@Override
	public ResponseType updateEarthGrid(EarthGridQuery egq) {
		// TODO Auto-generated method stub
		return ResponseType.ERROR;
	}


	@Override
	public boolean isNameUnique(String name) throws SQLException {
		
		Statement stmt = null;
		ResultSet rs = null;
		String sqlString = "";
		
		stmt = sdb.getConnection().createStatement();
		// get the count of the number of rows for the specified name 
		sqlString = "select count(*) as count from Simulation where name = '" + name + "'";
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
		String sqlString = "";
		String[] names;
		int counter = 0;
		
		stmt = sdb.getConnection().createStatement();
		// get the count of the number of rows
		sqlString = "select count(*) as count from Simulation";
		rs = stmt.executeQuery(sqlString);
		
		while (rs.next()) {
			counter = rs.getInt("count");
		}
		
		//create an array of Strings of count size counter
		names = new String[counter];
				
		stmt = sdb.getConnection().createStatement();
		sqlString = "select name from Simulation"; 
		rs = stmt.executeQuery(sqlString);

		//reset  counter to 0 to fill the array
		counter = 0;
		while (rs.next()) {
			names[counter] = rs.getString("name");
			counter++;
	    }
		
		return names;
	}
	
	
	
}
