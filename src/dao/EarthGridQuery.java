package dao;

import java.util.Calendar;

import dao.abstracts.EarthGridWrapperAbstract;

public class EarthGridQuery extends EarthGridWrapperAbstract {

	public EarthGridQuery(EarthGridProperties gridprops, Calendar EndDate)
	{
		super(gridprops, EndDate);
	}
	
}
