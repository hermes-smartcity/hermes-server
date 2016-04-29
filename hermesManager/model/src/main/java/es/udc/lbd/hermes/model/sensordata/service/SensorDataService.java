package es.udc.lbd.hermes.model.sensordata.service;

import java.util.Calendar;

import es.udc.lbd.hermes.model.sensordata.Row;
import es.udc.lbd.hermes.model.sensordata.SensorData;
import es.udc.lbd.hermes.model.sensordata.SensorDataType;
import es.udc.lbd.hermes.model.sensordata.SensorsDataJson;

public interface SensorDataService {

	public void create(SensorData sensorData, String userId);
	public void parserSensors(SensorsDataJson sensorsDataJson);
	
	public Row obtenerInfoPorDia(SensorDataType tipo, Long idUsuario, Calendar fechaIni, Calendar fechaFin);
}
