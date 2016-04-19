package es.udc.lbd.hermes.model.sensordata.service;

import es.udc.lbd.hermes.model.sensordata.SensorData;
import es.udc.lbd.hermes.model.sensordata.SensorsDataJson;

public interface SensorDataService {

	public void create(SensorData sensorData, String userId);
	public void parserSensors(SensorsDataJson sensorsDataJson);
}
