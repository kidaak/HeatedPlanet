package messaging.events;

import common.EarthGridProperties;
import common.IGrid;
import messaging.Message;

// Contains the IGrid result generated for a single simulation time
public class SimResultMessage extends Message {
	public IGrid result;
	public EarthGridProperties simProps;
	public SimResultMessage(EarthGridProperties simProps, IGrid igrid) {
		this.simProps = simProps;
		result = igrid;
	}
}