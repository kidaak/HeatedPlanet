package simulation;

import common.ComponentBase;
import common.EarthGridProperties;
import messaging.Message;
import messaging.Publisher;
import messaging.events.ProduceContinuousMessage;
import messaging.events.ViewPauseSimMessage;
import messaging.events.ViewResumeSimMessage;

public class Model extends ComponentBase {
	Earth model;
	boolean throttled = false;
	
	public Model(EarthGridProperties simProp) {
		model = new Earth();
		model.configure(simProp);
		model.start();
		
		//Setup message subscriptions
		pub.subscribe(ProduceContinuousMessage.class, this);
		pub.subscribe(ViewPauseSimMessage.class, this);
		pub.subscribe(ViewResumeSimMessage.class, this);
	}
	
	@Override
	public void dispatchMessage(Message msg) {
		if (msg instanceof ProduceContinuousMessage) {
			process((ProduceContinuousMessage) msg);
		} else if (msg instanceof ViewPauseSimMessage) {
			process((ViewPauseSimMessage) msg);
		} else if (msg instanceof ViewResumeSimMessage) {
			process((ViewResumeSimMessage) msg);
		} else {
			System.err.printf("WARNING: No processor specified in class %s for message %s\n",
					this.getClass().getName(), msg.getClass().getName());
		}
	}

	private void process(ProduceContinuousMessage msg) {
		if(!throttled) {
			generateData();
		}
		pub.send(msg); // resend message to self (since continuous)
	}

	private void process(ViewPauseSimMessage msg) {
//		System.out.printf("Throttling simulator...\n");
		throttled = true;
	}

	private void process(ViewResumeSimMessage msg) {
//		System.out.printf("...simulator producing\n");
		throttled = false;
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
