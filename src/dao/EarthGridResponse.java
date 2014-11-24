package dao;

import java.util.Calendar;

import common.Grid;

import dao.abstracts.EarthGridWrapperAbstract;

public class EarthGridResponse extends EarthGridWrapperAbstract {
	
	private ResponseType result;	
	
	private EarthGridResponse(ResponseType result, EarthGridProperties gridprops, Calendar EndDate, Grid[] EarthGrid, Calendar[] GridDates) 
	{
		super(gridprops, EndDate, EarthGrid, GridDates);
		
		this.setResult(result);
	}
	
	public static EarthGridResponse EarthGridResponseFactory(ResponseType result, Grid[] EarthGrid, Calendar[] GridDates, EarthGridQuery query) 
	{
		return new EarthGridResponse(result, query.getProperties(), query.getEndDate(), EarthGrid, GridDates);
	}
	
	public ResponseType getResult() {
		return result;
	}

	private void setResult(ResponseType result) {
		this.result = result;
	}
	
	public void setParametersFromQuery(EarthGridQuery query){
		this.setProperties(query.getProperties());
		this.setEndDate(query.getEndDate());
	}
	
}
