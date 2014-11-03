package simulation;

import common.ComponentBase;

import messaging.Message;
import messaging.Publisher;
import messaging.events.ProduceContinuousMessage;

public class Model extends ComponentBase {
	private Publisher pub = Publisher.getInstance();
	Earth model;
	
	public Model(int gs, int timeStep, float axialTilt, float eccentricity) {
		model = new Earth();
		model.configure(gs, timeStep, axialTilt, eccentricity);
		model.start();
	}
	
	@Override
	public void dispatchMessage(Message msg) {
		if (msg instanceof ProduceContinuousMessage) {
			process((ProduceContinuousMessage) msg);
		} else {
			System.err.printf("WARNING: No processor specified in class %s for message %s\n",
					this.getClass().getName(), msg.getClass().getName());
		}
	}

	private void process(ProduceContinuousMessage msg) {
		generateData();
		pub.send(msg); // resend message to self (since continuous)
	}

	public void close() {
		// destructor when done with class
	}
	
	private void generateData() {
		try {
			model.generate();
		} catch (InterruptedException e) {
			stopThread = true;
		}
	}
}
