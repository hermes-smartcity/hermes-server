package es.udc.lbd.hermes.model.sensordata.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.events.GraficasSensorData;
import es.udc.lbd.hermes.model.sensordata.SensorData;
import es.udc.lbd.hermes.model.sensordata.SensorDataJson;
import es.udc.lbd.hermes.model.sensordata.SensorDataType;
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
						lastSensor.setEndtime(tiempoAnterior);
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
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public GraficasSensorData obtenerInfoPorDia(SensorDataType tipo, Long idUsuario, Calendar fechaIni, Calendar fechaFin){
		
		List<String> labels = new ArrayList<String>();
		List<String> series = new ArrayList<String>();
		List<SensorData> informacion = sensorDataDao.informacionPorDia(tipo, idUsuario, fechaIni, fechaFin);
		
		//En primer lugar construimos las series
		if (informacion.size()>0){
			SensorData sensorData = informacion.get(0);
			int numValues = sensorData.getValues().length;
			
			for (int i = 0; i < numValues; i++) {
				series.add(String.valueOf(i));
			}
		}

		//inicializamos el objeto que tendra los resultos
		List<List<BigDecimal>> data = new ArrayList<List<BigDecimal>>(series.size());
		for (int i=0;i<series.size();i++) {
			data.add(i,new ArrayList<BigDecimal>());
		}
		
		for (SensorData sensorData : informacion) {
			//Tomamos la fecha de startime que sera la que ira en labels
			Calendar startTime = sensorData.getStartime();
			String fecha = calendarToString(startTime, "yyyy-MM-dd HH:mm:ss.SSS");
			labels.add(fecha);
			
			BigDecimal[] valores = sensorData.getValues();
			for(int i=0;i<valores.length;i++){
				//Recuperamos del data, la lista que corresponda
				List<BigDecimal> values = data.get(i);
				//Le anadimos a esa lista el elemento
				values.add(valores[i]);
			}
			
		}
		
		
		GraficasSensorData grafica = new GraficasSensorData(labels, series, data);
		return grafica;
		
	}
	
	private String calendarToString(Calendar calendar, String formato){
		String strdate = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		strdate = sdf.format(calendar.getTime());
		
		return strdate;
	}
	
	
	
}
