package messaging.events;

import common.IGrid;

import messaging.Message;

// Contains the IGrid result generated for a single simulation time
public class SimResultMessage extends Message {
	public IGrid result;
	public SimResultMessage(IGrid igrid) {
		result = igrid;
	}
}