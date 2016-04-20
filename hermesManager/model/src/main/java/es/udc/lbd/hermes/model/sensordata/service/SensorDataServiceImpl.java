package es.udc.lbd.hermes.model.sensordata.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.sensordata.SensorData;
import es.udc.lbd.hermes.model.sensordata.SensorDataJson;
import es.udc.lbd.hermes.model.sensordata.SensorsDataJson;
import es.udc.lbd.hermes.model.sensordata.dao.SensorDataDao;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.dao.UsuarioMovilDao;

@Service("sensorDataService")
@Transactional
public class SensorDataServiceImpl implements SensorDataService{

	@Autowired
	private SensorDataDao sensorDataDao;
	
	@Autowired
	private UsuarioMovilDao usuarioMovilDao;
	
	@Override
	public void create(SensorData sensorData, String userId) {
				
		UsuarioMovil usuarioMovil= usuarioMovilDao.findBySourceId(userId);
		if(usuarioMovil == null){
			usuarioMovil = new UsuarioMovil();
			usuarioMovil.setSourceId(userId);
			usuarioMovilDao.create(usuarioMovil);
		}
		sensorData.setUsuarioMovil(usuarioMovil);
		sensorDataDao.create(sensorData);
	}
	
	public void parserSensors(SensorsDataJson sensorsDataJson){
		String userId = sensorsDataJson.getUserid();
		String typeSensor = sensorsDataJson.getTypesensor();
		Boolean firstSend = sensorsDataJson.getFirstSend();
		Boolean lastSend = sensorsDataJson.getLastSend();
		List<SensorDataJson> sensoresJson = sensorsDataJson.getSensorData();
		
		Calendar tiempoAnterior;
		BigDecimal[] valoresAnteriores;
		
		Calendar tiempoActual;
		BigDecimal[] valoresActuales;
		
		if (sensoresJson.size()>0){
			//Se lee el primer <tiempo, evento>
			SensorDataJson sensorDataJson = sensoresJson.get(0);
			
			tiempoAnterior = Calendar.getInstance();
			tiempoAnterior.setTimeInMillis(sensorDataJson.getTimeStamp());
			
			valoresAnteriores = sensorDataJson.getValues();
			
			//Si el envio es el del medio o el ultimo, hay que encontrar el ultimo evento del mismo
			//usuario en la base de datos y actualizar su endtime con el tiempo de este envio
			if((!firstSend && !lastSend) || lastSend){
				UsuarioMovil usuarioMovil= usuarioMovilDao.findBySourceId(userId);
				if (usuarioMovil != null){
					SensorData lastSensor = sensorDataDao.findLast(usuarioMovil.getId(), typeSensor);
					if (lastSensor != null){
						lastSensor.setEnditme(tiempoAnterior);
						sensorDataDao.update(lastSensor);
					}
				}
			}
			
			//Mientras queden eventos
			for (int i = 1; i < sensoresJson.size(); i++) {
				sensorDataJson = sensoresJson.get(i);
				
				tiempoActual = Calendar.getInstance();
				tiempoActual.setTimeInMillis(sensorDataJson.getTimeStamp());

				valoresActuales = sensorDataJson.getValues();
				
				//Creamos el sensorData
				SensorData sensorData = new SensorData(typeSensor, tiempoAnterior, tiempoActual, valoresAnteriores);
				create(sensorData, userId);
				
				//Reasignamos los valores
				tiempoAnterior = tiempoActual;
				valoresAnteriores = valoresActuales;
			}
			
			//Insertamos el ultimo
			//Consideraciones: si es el primer envio o el del medio, el tiempo final sera nulo. Si es el final sera el valor
			if (firstSend || (!firstSend && !lastSend)){
				SensorData sensorData = new SensorData(typeSensor, tiempoAnterior, null, valoresAnteriores);
				create(sensorData,userId);
			}else{
				SensorData sensorData = new SensorData(typeSensor, tiempoAnterior, tiempoAnterior, valoresAnteriores);
				create(sensorData,userId);
			}
			
		}
		
	}
}
