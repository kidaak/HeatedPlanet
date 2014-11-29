package persistenceManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import interpolator.GridInterprolator;
import messaging.Message;
import messaging.events.SimResultMessage;
import common.ComponentBase;
import common.EarthGridProperties;
import common.IGrid;
import dao.EarthGridDao;
import dao.EarthGridInsert;
import dao.EarthGridQuery;
import dao.EarthGridResponse;
import dao.ResponseType;
import dao.interfaces.IEarthGridDao;

public class PersistenceManager extends ComponentBase {
	static IEarthGridDao dao = EarthGridDao.getEarthGridDao();

	public PersistenceManager() {
		// Setup message subscriptions
		pub.subscribe(SimResultMessage.class, this);
	}
	
	@Override
	public void dispatchMessage(Message msg) {
		if (msg instanceof SimResultMessage) {
			process((SimResultMessage) msg);
		} else {
			System.err.printf("WARNING: No processor specified in class %s for message %s\n",
					this.getClass().getName(), msg.getClass().getName());
		}
	}

	private void process(SimResultMessage msg) {
		// Decimate message for persistent storage
		GridInterprolator gridInterpolator = new GridInterprolator(msg.simProps);
		IGrid decimatedGrid = gridInterpolator.decimateAll(msg.result);
		
//		System.out.printf("processing grid %d!\n", msg.result.getTime());
		// Only store the value if time decimation indicated it should be
		if(decimatedGrid != null) {
			try {
//				System.out.printf("storing grid %d!\n", msg.result.getTime());
				dao.insertEarthGridSimulation(new EarthGridInsert(msg.simProps, msg.result));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static PersistenceManagerQueryResult getQueryData(EarthGridProperties querySpec) {
		// Get raw data for query from persistence layer and passes it through
		// the interpolator to reconstruct full grid results from query.
		EarthGridResponse response = null;
		ArrayList<IGrid> gridList = null;
		try {
			response = dao.queryEarthGridSimulation(new EarthGridQuery(querySpec));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(response.getResult() == ResponseType.FOUND_ONE || response.getResult() == ResponseType.FOUND_MANY) {
			EarthGridProperties simProp = response.getProperties();
			IGrid[] grids = response.getAllGrids();
			
			GridInterprolator gridInterpolator = new GridInterprolator(simProp);
			gridList = gridInterpolator.interpolateAll(grids);
			return new PersistenceManagerQueryResult(gridList,querySpec,simProp);
		}
		
		return null;
	}
	
	public static ArrayList<String> getAllSimNames() {
		ArrayList<String> ret = new ArrayList<String>();
		try {
			Collections.addAll(ret, dao.getAllNames());
		} catch (SQLException e) {
//			e.printStackTrace();
		}
		Collections.sort(ret); // put in sorted order
		return ret;
	}

	public static void clearDb() {
		System.out.printf("CLEARING DATABASE...");
		try {
			dao.resetDatabase("42");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.printf("DONE\n");
	}
}
 