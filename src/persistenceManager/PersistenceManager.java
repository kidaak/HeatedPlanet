package persistenceManager;

import java.util.ArrayList;

import interpolator.GridInterprolator;
import messaging.Message;
import messaging.events.SimResultMessage;
import common.ComponentBase;
import common.EarthGridProperties;
import common.Grid;
import common.IGrid;
import dao.EarthGridDao;
import dao.EarthGridQuery;
import dao.EarthGridResponse;
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
			//TODO: store value in persistent storage...how do these methods work?
			//      I want to be able to pass it EarthGridProperties and IGrid and
			//      have it do the right thing.
//			dao.insertEarthGridSimulation(egq);
//			System.out.printf("storing grid %d!\n", msg.result.getTime());
		}
	}

	public static ArrayList<Grid> getQueryData(EarthGridProperties querySpec) {
		// Get raw data for query from persistence layer and passes it through
		// the interpolator to reconstruct full grid results from query.
		EarthGridResponse response = null;
		ArrayList<Grid> gridList = null;
		try {
			//TODO: why does query require endDate outside queryspec?
			//TODO: why is there a separate query method for when named query is performed?
			//TODO: what is best way to tell the query was successful? 
			response = dao.queryEarthGridSimulation(new EarthGridQuery(querySpec, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(response != null) {
			EarthGridProperties simProp = response.getProperties();
			Grid[] grids = response.getAllGrids();
			
			GridInterprolator gridInterpolator = new GridInterprolator(simProp);
			gridList = gridInterpolator.interpolateAll(grids);
		}
		
		return gridList;
	}
	
}
 