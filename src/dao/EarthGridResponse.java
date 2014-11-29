package dao;

import java.util.Calendar;

import common.EarthGridProperties;
import common.Grid;
import dao.abstracts.EarthGridWrapperAbstract;

public class EarthGridResponse extends EarthGridWrapperAbstract {
	
	private ResponseType result;	
	
	private EarthGridResponse(ResponseType result, EarthGridProperties gridprops, Grid[] EarthGrid, Calendar[] GridDates) 
	{
		super(gridprops, EarthGrid, GridDates);
		this.setResult(result);
	}
	
	public static EarthGridResponse EarthGridResponseFactory(ResponseType result, Grid[] EarthGrid, Calendar[] GridDates, EarthGridProperties props) 
	{
		return new EarthGridResponse(result, props, EarthGrid, GridDates);
	}
	
	public ResponseType getResult() {
		return result;
	}

	private void setResult(ResponseType result) {
		this.result = result;
	}
	
	//public void setParametersFromQuery(EarthGridQuery query){
	//	this.setProperties(query.getProperties());
	//}
	
}
