package dao.interfaces;

import java.sql.SQLException;

import dao.EarthGridQuery;
import dao.EarthGridResponse;
import dao.ResponseType;

public interface IEarthGridDao {
	
	public EarthGridResponse queryDatabase(EarthGridQuery query);
	
	public ResponseType addEarthGrid(EarthGridQuery egq);
	
	public ResponseType updateEarthGrid(EarthGridQuery egq);
	
	public boolean isNameUnique(String name) throws SQLException;
	
	public String[] getAllNames() throws SQLException;
}
