package PlanetSim;

import persistenceManager.PersistenceManager;
import messaging.Message;
import messaging.Publisher;
import messaging.events.ProduceContinuousMessage;
import simulation.Model;
import view.View;
import common.Buffer;
import common.ComponentBase;
import common.EarthGridProperties;

public class Controller extends ComponentBase {
	
	public static final String DEFAULT_SIM_NAME = new String("defaultName");
	public static final int DEFAULT_GRID_SPACING = 15;
	public static final int DEFAULT_TIME_STEP = 1;
	public static final float DEFAULT_PRESENTATION_RATE = 0.01f;
	public static final float DEFAULT_AXIAL_TILT = 23.44f;
	public static final float DEFAULT_ECCENTRICITY = 0.0167f;
	public static final int DEFAULT_DURATION = 12;
	
	private Model model;
	private View view;
	private PersistenceManager persistenceManager;
	
	private int bufferSize = 1000;
	
	private Thread modelThread;
	private Thread viewThread;
	private Thread persistenceThread;
	private Thread t;
	private boolean started=false;
	
	public Controller() {
	}
	
	public void start(EarthGridProperties simProp) {
		
		Buffer.getBuffer().create(this.bufferSize);
		
		// Instance model/view
		model = new Model(simProp);
		view = new View(simProp);
		persistenceManager = new PersistenceManager();
		
		// Setup model initiative
		// kickstart message to the model.  After first message it will 
		// continue to provide the message to itself and fill buffer.
		pub.send(new ProduceContinuousMessage());
		
		
		// Kick off threads
		modelThread = new Thread(model,"model");
		modelThread.start();

		viewThread = new Thread(view,"view");
		viewThread.start();
		
		persistenceThread = new Thread(persistenceManager,"persistenceMgr");
		persistenceThread.start();
		
		// Kick off run loop
		paused = false;
		stopThread = false;
		if(t==null) {
			t = new Thread(this,"controller");
			t.start();
		}
		started = true;
	}
	
	public void stop() throws InterruptedException {
		
		// Nothing to do if never started
		if(!started) {
			return;
		}
		
		// End run loop
		started = false;
		stopThread = true;
		paused = false;
		
		t.join();
		
		// Stop threads
		modelThread.interrupt();
		modelThread.join();

		viewThread.interrupt();
		viewThread.join();
		
		persistenceThread.interrupt();
		persistenceThread.join();
		
		// remove subscriptions
		Publisher.unsubscribeAll();
		
		// destroy model/view
		model.close();
		model = null;
		view.close();
		view = null;
//		persistenceManager.close();
		persistenceManager = null;
		
		t = null;
	}
	
	public void pause() {
		
		// make GUI updates
		// set variable to skip run loop contents
		paused = true;
		model.pause(paused);
		view.pause(paused);
	}
	
	public void resume() {
		
		// make GUI updates
		// set variable to NOT skip run loop contents
		paused = false;
		model.pause(paused);
		view.pause(paused);
	}
	
	@Override
	public void dispatchMessage(Message msg) {
		// No current message subscriptions
	}

}
