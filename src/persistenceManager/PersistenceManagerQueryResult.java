package persistenceManager;

import java.util.ArrayList;

import common.EarthGridProperties;
import common.IGrid;

// A storage class for returning query results
public class PersistenceManagerQueryResult {
	public ArrayList<IGrid> grids;
	public EarthGridProperties querySpec;
	public EarthGridProperties gridProps;
	
	public PersistenceManagerQueryResult(ArrayList<IGrid> grids, EarthGridProperties querySpec, EarthGridProperties gridProps) {
		this.grids = grids;
		this.querySpec = querySpec;
		this.gridProps = gridProps;
	}
}
