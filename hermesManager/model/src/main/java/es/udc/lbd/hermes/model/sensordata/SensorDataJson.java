package es.udc.lbd.hermes.model.sensordata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorDataJson {

	@JsonProperty("timeStamp")
	private long timeStamp;
	
	@JsonProperty("values")
	private Double[] values;

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Double[] getValues() {
		return values;
	}

	public void setValues(Double[] values) {
		this.values = values;
	}
	
}
