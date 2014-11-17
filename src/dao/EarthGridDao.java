package dao;

import common.Grid;

import dao.interfaces.IEarthGridDao;

public class EarthGridDao implements IEarthGridDao {
	
	private static final EarthGridDao instance;
	
	//Static block initialization...
	static {
		try{
			instance = new EarthGridDao();
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
		
		
		return ResponseType.ERROR;
	}


	@Override
	public ResponseType updateEarthGrid(EarthGridQuery egq) {
		// TODO Auto-generated method stub
		return ResponseType.ERROR;
	}
	
	
	
}
