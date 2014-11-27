package test.dao;

import java.util.Calendar;
import java.util.TimeZone;

import common.EarthGridProperties;
import common.EarthGridProperties.EarthGridProperty;

public class TestDeterminantOrderOfPropertiesInList {

	public static void main(String[] args) {
		
		Calendar start = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		Calendar end = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		
		EarthGridProperties EGProps1 = new EarthGridProperties();
        EGProps1.setProperty(EarthGridProperty.NAME, "InsertedName1");
        System.out.println(EGProps1.getPropertyString(EarthGridProperty.NAME));
        EGProps1.definedProperties();
        System.out.println("------------");
        
        EarthGridProperties EGProps2 = new EarthGridProperties();
        EGProps2.setProperty(EarthGridProperty.NAME, "InsertedName2");
        EGProps2.setProperty(EarthGridProperty.START_DATE, start);
        EGProps2.setProperty(EarthGridProperty.END_DATE, end);
        System.out.println(EGProps2.getPropertyString(EarthGridProperty.NAME));
        EGProps2.definedProperties();
        System.out.println("------------");
        
        EarthGridProperties EGProps3 = new EarthGridProperties();
        EGProps3.setProperty(EarthGridProperty.NAME, "InsertedName3");
        EGProps3.setProperty(EarthGridProperty.END_DATE, end);
        EGProps3.setProperty(EarthGridProperty.START_DATE, start);
        System.out.println(EGProps3.getPropertyString(EarthGridProperty.NAME));
        EGProps3.definedProperties();
        System.out.println("------------");
        
        EarthGridProperties EGProps4 = new EarthGridProperties();
        EGProps4.setProperty(EarthGridProperty.NAME, "InsertedName4");
        EGProps4.setProperty(EarthGridProperty.START_DATE, start);
        EGProps4.setProperty(EarthGridProperty.AXIAL_TILT, 0.01);
        EGProps4.setProperty(EarthGridProperty.END_DATE, end);
        System.out.println(EGProps4.getPropertyString(EarthGridProperty.NAME));
        EGProps4.definedProperties();
        System.out.println("------------");
        
        EarthGridProperties EGProps5 = new EarthGridProperties();
        EGProps5.setProperty(EarthGridProperty.START_DATE, start);
        EGProps5.setProperty(EarthGridProperty.NAME, "InsertedName5");
        EGProps5.setProperty(EarthGridProperty.END_DATE, end);
        EGProps5.setProperty(EarthGridProperty.START_DATE, start);
        System.out.println(EGProps5.getPropertyString(EarthGridProperty.NAME));
        EGProps5.definedProperties();
        System.out.println("------------");
        
        
	}
}
