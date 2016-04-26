package es.udc.lbd.hermes.model.sensordata;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorDataJson {

	@JsonProperty("timeStamp")
	private long timeStamp;
	
	@JsonProperty("values")
	private BigDecimal[] values;

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public BigDecimal[] getValues() {
		return values;
	}

	public void setValues(BigDecimal[] values) {
		this.values = values;
	}
	
}
