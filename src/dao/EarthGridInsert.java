package dao;

import java.util.Calendar;

import common.EarthGridProperties;
import common.Grid;
import dao.abstracts.EarthGridWrapperAbstract;

public class EarthGridInsert extends EarthGridWrapperAbstract {

	public EarthGridInsert(EarthGridProperties properties, Calendar endDate,
			Grid[] g, Calendar[] gd) {
		super(properties, endDate, g, gd);
	}

}
