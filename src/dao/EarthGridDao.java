package dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
	
	
	
}
