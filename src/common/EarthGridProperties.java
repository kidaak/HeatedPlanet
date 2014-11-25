package common;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.NoSuchElementException;

public class EarthGridProperties implements Serializable{

	private static final long serialVersionUID = -7343880418422792826L;
	
	private Hashtable<EarthGridProperty,String> ht;

	public static enum EarthGridProperty {
		NAME, 
		GRID_SPACING, SIMULATION_TIME_STEP, SIMULATION_LENGTH, PRECISION, GEO_PRECISION, TIME_PRECISION,
		AXIAL_TILT, ECCENTRICITY, PRESENTATION_RATE, 
	};
	
	public EarthGridProperties(){
		ht = new Hashtable<EarthGridProperty, String>();
	}
	
	private void internalSetProperty(EarthGridProperty prop, String value){
		ht.put(prop, value);
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
				if(value <= 0)
					throw new IllegalArgumentException("Grid Spacing must be >= 1");
				break;
			case SIMULATION_TIME_STEP:
				if(value <= 0)
					throw new IllegalArgumentException("Simulation Time Step must be >= 1");
				break;
			case SIMULATION_LENGTH:
				if(value <= 0)
					throw new IllegalArgumentException("Simulation Length must be >= 1");
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
	
	public String getPropertyString(EarthGridProperty prop) {
		if(ht.containsKey(prop))
			return ht.get(prop);
		throw new NoSuchElementException(prop.name()+" was not found in this property set.");
	}

	public Integer getPropertyInt(EarthGridProperty prop) {
		return Integer.parseInt(getPropertyString(prop));
	}

	public Float getPropertyFloat(EarthGridProperty prop) {
		return Float.parseFloat(getPropertyString(prop));
	}
}
