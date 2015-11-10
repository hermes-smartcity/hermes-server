package es.udc.lbd.hermes.model.events.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({"Aggregator-Ids","Syntax"})
public class Event {

	@JsonProperty("Event-Id")
	private String eventId;
	@JsonProperty("Application-Id")
	private String applicationId;
	@JsonProperty("Source-Id")
	private String sourceId;
	@JsonProperty("Timestamp")
	private Date timestamp;
	@JsonProperty("Event-Type")
	private String eventType;
	@JsonProperty("Body")
	private EventData eventData;	
	
	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public EventData getEventData() {
		return eventData;
	}

	public void setEventData(EventData eventData) {
		this.eventData = eventData;
	}

	
}
