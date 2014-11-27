package common;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.TimeZone;

public class EarthGridProperties implements Serializable{

	private static final long serialVersionUID = -7343880418422792826L;
	
	private Hashtable<EarthGridProperty,String> ht;

	public static enum EarthGridProperty {
		NAME, 
		GRID_SPACING, SIMULATION_TIME_STEP, SIMULATION_LENGTH, PRECISION, GEO_PRECISION, TIME_PRECISION,
		AXIAL_TILT, ECCENTRICITY, PRESENTATION_RATE,
		START_DATE, END_DATE
	};
	
	public static final EarthGridProperty[] EarthGridStringProperties = {EarthGridProperty.NAME};
	public static final EarthGridProperty[] EarthGridDoubleProperties = {EarthGridProperty.AXIAL_TILT,
																EarthGridProperty.ECCENTRICITY,
																EarthGridProperty.PRESENTATION_RATE
															};
	public static final EarthGridProperty[] EarthGridFloatProperties = EarthGridDoubleProperties;
	public static final EarthGridProperty[] EarthGridIntProperties = {EarthGridProperty.GRID_SPACING,
																EarthGridProperty.SIMULATION_TIME_STEP,
																EarthGridProperty.SIMULATION_LENGTH,
																EarthGridProperty.PRECISION,
																EarthGridProperty.GEO_PRECISION,
																EarthGridProperty.TIME_PRECISION
															};
	public static final EarthGridProperty[] EarthGridCalendarProperties = {EarthGridProperty.START_DATE,
																EarthGridProperty.END_DATE
															};
	
	public EarthGridProperties(){
		ht = new Hashtable<EarthGridProperty, String>();
	}
	
	public static boolean arrayContains(EarthGridProperty[] props, EarthGridProperty prop){
		for(int i = 0; i<props.length; i++){
			if(props[i].equals(prop))
				return true;
		}
		return false;
	}
	
	public EarthGridProperty[] definedProperties(){
		EarthGridProperty[] definedProps;
		if(!ht.isEmpty()){
			int index = 0;
			int numKeys = ht.size();
			definedProps = new EarthGridProperty[numKeys];
			Enumeration<EarthGridProperty> keys = ht.keys();
			while(keys.hasMoreElements()){
				definedProps[index] = keys.nextElement();
				index++;
			}
		}else{
			definedProps = new EarthGridProperty[0];
		}
		
		return definedProps;
	}
	
	public boolean isPropertyDefined(EarthGridProperty key){
		return ht.containsKey(key);
	}
	
	private void internalSetProperty(EarthGridProperty prop, String value){
		ht.put(prop, value);
	}
	
	public void setProperty(EarthGridProperty prop, Calendar value){
		switch(prop){
			case START_DATE:
				if(value == null)
					throw new IllegalArgumentException("Start Date is empty");
				break;
			case END_DATE:
				if(value == null)
					throw new IllegalArgumentException("End Date is empty");
				break;
			default:
				throw new IllegalArgumentException(prop.name()+" is not expecting a Calendar.");
		}
		internalSetProperty(prop,String.valueOf(value.getTimeInMillis()) );
	}
	
	public void setProperty(EarthGridProperty prop, String value){
		switch(prop){
			case NAME:
				if(value == null || value.trim().length() == 0)
					throw new IllegalArgumentException("Simulation name is empty");
				break;
			default:
				throw new IllegalArgumentException(prop.name()+" is not expecting a string.");
		}
		internalSetProperty(prop,String.valueOf(value));
	}
	
	public void setProperty(EarthGridProperty prop, int value) throws IllegalArgumentException{
		switch(prop){
			case GRID_SPACING:
				if(value <= 0 || value > 180)
					throw new IllegalArgumentException("Grid Spacing must be >= 1 and <= 180 degrees");
				break;
			case SIMULATION_TIME_STEP:
				if(value <= 0 || value > 525600)
					throw new IllegalArgumentException("Simulation Time Step must be >= 1 and <= 525600 minutes");
				break;
			case SIMULATION_LENGTH:
				if(value <= 0 || value > 1200)
					throw new IllegalArgumentException("Simulation Length must be >= 1 and <= 1200 months");
				break;
			case PRECISION:
				if(value <= 0)
					throw new IllegalArgumentException("Precision must be >= 1");
				break;
			case GEO_PRECISION:
				if(value <= 0)
					throw new IllegalArgumentException("Geographic Precision must be >= 1");
				break;
			case TIME_PRECISION:
				if(value <= 0)
					throw new IllegalArgumentException("Time Precision must be >= 1");
				break;
			default:
				throw new IllegalArgumentException(prop.name()+" is not expecting an integer.");
		}
		internalSetProperty(prop,String.valueOf(value));
	}
	
	public void setProperty(EarthGridProperty prop, double value){
		switch(prop){
			case AXIAL_TILT:
				if(value < -180 || value > 180)
					throw new IllegalArgumentException("The Axial Tilt must be between -180 and 180 degrees");
				break;
			case ECCENTRICITY:
				if(value < 0 || value >= 1)
					throw new IllegalArgumentException("The eccentricity must be between 0 and 1, but strictly less than 1.00");
				break;
			case PRESENTATION_RATE:
				if(value < 0)
					throw new IllegalArgumentException("The presentation rate must be 0 or larger");
				break;
			default:
				throw new IllegalArgumentException(prop.name()+" is not expecting a double.");
		}
		internalSetProperty(prop,String.valueOf(value));
	}
	
	public void setProperty(EarthGridProperty prop, float value){
		try{
			setProperty(prop,Double.valueOf(value));
		}catch(IllegalArgumentException ex){
			throw new IllegalArgumentException(prop.name()+" is not expecting a float.");
		}
	}
	
	public String getPropertyString(EarthGridProperty prop) {
		if(ht.containsKey(prop))
			return ht.get(prop);
		throw new NoSuchElementException(prop.name()+" was not found in this property set.");
	}

	public Integer getPropertyInt(EarthGridProperty prop) {
		if(!arrayContains(EarthGridIntProperties,prop))
			throw new IllegalArgumentException(prop.name()+" is not an Integer property");
		return Integer.parseInt(getPropertyString(prop));
	}

	public Float getPropertyFloat(EarthGridProperty prop) {
		if(!arrayContains(EarthGridFloatProperties,prop))
			throw new IllegalArgumentException(prop.name()+" is not a Float property");
		return Float.parseFloat(getPropertyString(prop));
	}
	
	public Calendar getPropertyCalendar(EarthGridProperty prop){			
		if(!arrayContains(EarthGridCalendarProperties,prop))
			throw new IllegalArgumentException(prop.name()+" is not a Calendar property");
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.setTimeInMillis(Long.valueOf(getPropertyString(prop)) );
		return cal;
	}
}
