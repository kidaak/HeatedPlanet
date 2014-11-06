package dao;

import java.util.Calendar;

import common.Grid;

import dao.abstracts.EarthGridWrapperAbstract;

public class EarthGridQuery extends EarthGridWrapperAbstract {

	public EarthGridQuery(int GridSpacing, double AxialTilt, double Eccentricity,
			Calendar StartDate,	Calendar EndDate)
	{
		super(GridSpacing, AxialTilt, Eccentricity, StartDate, EndDate);
	}
	
}
