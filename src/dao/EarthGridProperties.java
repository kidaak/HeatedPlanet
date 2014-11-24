package dao;

import java.util.Properties;

public class EarthGridProperties extends Properties{

	private static final long serialVersionUID = -7343880418422792826L;

	public static enum Property {
		NAME, 
		GRID_SPACING, SIMULATION_TIME_STEP, SIMULATION_LENGTH, PRECISION, GEO_PRECISION, TIME_PRECISION, SIMULATION_END_DATE,
		AXIAL_TILT, ECCENTRICITY,
		GRID, GRID_DATE
	};
}
