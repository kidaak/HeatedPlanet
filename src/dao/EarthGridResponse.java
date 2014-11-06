package dao;

import java.util.Calendar;

import common.Grid;

import dao.abstracts.EarthGridWrapperAbstract;

public class EarthGridResponse extends EarthGridWrapperAbstract {
	
	private ResponseType result;
	
	
	private EarthGridResponse(ResponseType result, int GridSpacing, double AxialTilt, double Eccentricity,
			Calendar StartDate,	Calendar EndDate, Grid EarthGrid) 
	{
		super(GridSpacing, AxialTilt, Eccentricity, StartDate, EndDate, EarthGrid);
		
		this.setResult(result);
	}
	
	public static EarthGridResponse EarthGridResponseFactory(ResponseType result, Grid EarthGrid, EarthGridQuery query) 
	{
		return new EarthGridResponse(result, query.getGridSpacing(), query.getAxialTilt(),
				query.getEccentricity(),query.getStartDate(), query.getEndDate(), EarthGrid);
	}
	
	public ResponseType getResult() {
		return result;
	}

	private void setResult(ResponseType result) {
		this.result = result;
	}
	
	public void setParametersFromQuery(EarthGridQuery query){
		this.setGridSpacing(query.getGridSpacing());
		this.setAxialTilt(query.getAxialTilt());
		this.setEccentricity(query.getEccentricity());
		this.setStartDate(query.getStartDate());
		this.setEndDate(query.getEndDate());
	}
	
}
