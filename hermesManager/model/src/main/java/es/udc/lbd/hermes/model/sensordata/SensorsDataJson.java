package es.udc.lbd.hermes.model.sensordata;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorsDataJson {

	@JsonProperty("userID")
	private String userid;
	
	@JsonProperty("typeSensor")
	private String typesensor;
	
	@JsonProperty("sensorData")
	private List<SensorDataJson> sensorData;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getTypesensor() {
		return typesensor;
	}

	public void setTypesensor(String typesensor) {
		this.typesensor = typesensor;
	}

	public List<SensorDataJson> getSensorData() {
		return sensorData;
	}

	public void setSensorData(List<SensorDataJson> sensorData) {
		this.sensorData = sensorData;
	}
	
}
