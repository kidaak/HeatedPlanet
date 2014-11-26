package persistenceManager;

import java.util.ArrayList;

import interpolator.GridInterprolator;
import messaging.Message;
import messaging.events.SimResultMessage;
import common.ComponentBase;
import common.EarthGridProperties;
import common.IGrid;
import common.EarthGridProperties.EarthGridProperty;
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

	public static ArrayList<IGrid> getQueryData(EarthGridProperties querySpec) {
		// Get raw data for query from persistence layer and passes it through
		// the interpolator to reconstruct full grid results from query.
		EarthGridResponse response = null;
		ArrayList<IGrid> gridList = null;
		try {
			if(querySpec.isPropertyDefined(EarthGridProperty.NAME)) {
				response = dao.queryEarthGridSimulationByName(querySpec.getPropertyString(EarthGridProperty.NAME));
			}
			else {
				response = dao.queryEarthGridSimulation(new EarthGridQuery(querySpec));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(response.getResult() != ResponseType.NOTFOUND) {
			EarthGridProperties simProp = response.getProperties();
			IGrid[] grids = response.getAllGrids();
			
			GridInterprolator gridInterpolator = new GridInterprolator(simProp);
			gridList = gridInterpolator.interpolateAll(grids);
		}
		
		return gridList;
	}
	
}
 