package es.udc.lbd.hermes.model.sensordata.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.sensordata.SensorData;
import es.udc.lbd.hermes.model.sensordata.SensorDataJson;
import es.udc.lbd.hermes.model.sensordata.SensorsDataJson;
import es.udc.lbd.hermes.model.sensordata.dao.SensorDataDao;

@Service("sensorDataService")
@Transactional
public class SensorDataServiceImpl implements SensorDataService{

	@Autowired
	private SensorDataDao sensorDataDao;
	
	@Override
	public void create(SensorData sensorData) {
		sensorDataDao.create(sensorData);
	}
	
	public void parserSensors(SensorsDataJson sensorsDataJson){
		String userId = sensorsDataJson.getUserid();
		String typeSensor = sensorsDataJson.getTypesensor();
		List<SensorDataJson> sensoresJson = sensorsDataJson.getSensorData();
		
		Calendar tiempoAnterior;
		Double[] valoresAnteriores;
		
		Calendar tiempoActual;
		Double[] valoresActuales;
		
		if (sensoresJson.size()>0){
			//Se lee el primer para <tiempo, evento>
			SensorDataJson sensorDataJson = sensoresJson.get(0);
			
			tiempoAnterior = Calendar.getInstance();
			tiempoAnterior.setTimeInMillis(sensorDataJson.getTimeStamp());
			
			valoresAnteriores = sensorDataJson.getValues();
			
			//Mientras queden eventos
			for (int i = 1; i < sensoresJson.size(); i++) {
				sensorDataJson = sensoresJson.get(i);
				
				tiempoActual = Calendar.getInstance();
				tiempoActual.setTimeInMillis(sensorDataJson.getTimeStamp());

				valoresActuales = sensorDataJson.getValues();
				
				//Creamos el sensorData
				SensorData sensorData = new SensorData(userId, typeSensor, tiempoAnterior, tiempoActual, valoresAnteriores);
				sensorDataDao.create(sensorData);
				
				//Reasignamos los valores
				tiempoAnterior = tiempoActual;
				valoresAnteriores = valoresActuales;
			}
			
			//Insertamos el ultimo
			SensorData sensorData = new SensorData(userId, typeSensor, tiempoAnterior, tiempoAnterior, valoresAnteriores);
			sensorDataDao.create(sensorData);
		}
		
	}
}
