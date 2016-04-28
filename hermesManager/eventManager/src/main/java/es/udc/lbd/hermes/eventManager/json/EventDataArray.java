package es.udc.lbd.hermes.eventManager.json;

public class EventDataArray extends EventData {
	private EventData[] events;

	public EventDataArray(EventData[] events) {
		super();
		this.events = events;
	}

	public EventData[] getEvents() {
		return events;
	}

	public void setEvents(EventData[] events) {
		this.events = events;
	}
}
