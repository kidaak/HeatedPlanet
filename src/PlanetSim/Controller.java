package PlanetSim;

import messaging.Message;
import messaging.Publisher;
import messaging.events.ProduceContinuousMessage;
import simulation.Model;
import view.View;
import common.Buffer;
import common.ComponentBase;

public class Controller extends ComponentBase {
	
	public static final int DEFAULT_GRID_SPACING = 15;
	public static final int DEFAULT_TIME_STEP = 1;
	public static final float DEFAULT_PRESENTATION_RATE = 0.01f;
	public static final float DEFAULT_AXIAL_TILT = 23.44f;
	public static final float DEFAULT_ECCENTRICITY = 0.0167f;
	
	private Model model;
	private View view;
	
	private Publisher pub = Publisher.getInstance();
	
	private int bufferSize = 100;
	private int precisionDigits;
	private int geographicAccuracy;
	private int temporalAccuracy;
	
	private Thread modelThread;
	private Thread viewThread;
	private Thread t;
	
	public Controller(int precisionDigits, int geographicAccuracy, int temporalAccuracy) {
		
		if (bufferSize < 1 || bufferSize > Integer.MAX_VALUE) 
			throw new IllegalArgumentException("Invalid size");
		
		this.precisionDigits = precisionDigits;
		this.geographicAccuracy = geographicAccuracy;
		this.temporalAccuracy = temporalAccuracy;
	}
	
	public void start(int gs, int timeStep, float presentationInterval, float axialTilt, float eccentricity) {
		
		if (gs < 1 || gs > Integer.MAX_VALUE)
			throw new IllegalArgumentException("Invalid grid spacing");
		
		if (timeStep < 1 || gs > Integer.MAX_VALUE)
			throw new IllegalArgumentException("Invalid time step");
		
		if (presentationInterval < 0)
			throw new IllegalArgumentException("Invalid presentation interval");
		
		Buffer.getBuffer().create(this.bufferSize);
		
		// Instance model/view
		model = new Model(gs, timeStep, axialTilt, eccentricity);
		view = new View(gs, timeStep, presentationInterval);
		
		// Setup model initiative
		pub.subscribe(ProduceContinuousMessage.class, model);
		// kickstart message to the model.  After first message it will 
		// continue to provide the message to itself and fill buffer.
		pub.send(new ProduceContinuousMessage());
		
		
		// Kick off threads
		modelThread = new Thread(model,"model");
		modelThread.start();

		viewThread = new Thread(view,"view");
		viewThread.start();
		
		// Kick off run loop
		paused = false;
		stopThread = false;
		if(t==null) {
			t = new Thread(this,"controller");
			t.start();
		}
	}
	
	public void stop() throws InterruptedException {
		// End run loop
		stopThread = true;
		paused = false;
		
		t.join();
		
		// Stop threads
		modelThread.interrupt();
		modelThread.join();

		viewThread.interrupt();
		viewThread.join();
		
		// remove subscriptions
		Publisher.unsubscribeAll();
		
		// destroy model/view
		model.close();
		model = null;
		view.close();
		view = null;
		
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
