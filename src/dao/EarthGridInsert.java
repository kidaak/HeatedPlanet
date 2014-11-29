package dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import common.EarthGridProperties;
import common.IGrid;
import common.EarthGridProperties.EarthGridProperty;
import dao.abstracts.EarthGridWrapperAbstract;

public class EarthGridInsert extends EarthGridWrapperAbstract {

	public EarthGridInsert(EarthGridProperties properties, IGrid[] g, Calendar[] gd) {
		super(properties, g, gd);
	}

	public EarthGridInsert(EarthGridProperties properties, IGrid g) {
		super(properties, new IGrid[]{g}, new Calendar[]{getCal(g)});
	}
	
	private static Calendar getCal(IGrid g) {
		// Returns a calendar corresponding to grid.  This is a bit hacky, but 
		// I need this so that I can call super on first line of more usable
		// EarthGridInsert Constructor...
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.clear();
		cal.set(2014, 0, 4);
		cal.add(Calendar.MINUTE, g.getCurrentTime());

		SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		dateFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
		System.out.printf("inserting %s %d\n", dateFmt.format(cal.getTime()), g.getCurrentTime());
		return cal;
	}
}
