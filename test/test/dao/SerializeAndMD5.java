package test.dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import dao.EarthGridDao;
import dao.EarthGridQuery;

public class SerializeAndMD5 {

	public static void main(String[] args) {
		
		ArrayList<String> egq = new ArrayList<String>();
		egq.add("First");
		egq.add("Second");
		
		
		try{
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bOut);
			out.writeObject(egq);
			out.close();
			byte[] objBytes = bOut.toByteArray();
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] md5Byte = md.digest(objBytes);
			String md5String = new sun.misc.BASE64Encoder().encode(md5Byte);
			
			System.out.println(md5String);
			
		}catch(IOException ex){
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} catch (NoSuchAlgorithmException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}

	}

}
