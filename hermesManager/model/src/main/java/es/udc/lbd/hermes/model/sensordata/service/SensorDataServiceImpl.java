package es.udc.lbd.hermes.model.sensordata.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.udc.lbd.hermes.model.events.GraficasSensorData;
import es.udc.lbd.hermes.model.sensordata.C;
import es.udc.lbd.hermes.model.sensordata.Row;
import es.udc.lbd.hermes.model.sensordata.SensorData;
import es.udc.lbd.hermes.model.sensordata.SensorDataJson;
import es.udc.lbd.hermes.model.sensordata.SensorDataType;
import es.udc.lbd.hermes.model.sensordata.SensorsDataJson;
import es.udc.lbd.hermes.model.sensordata.V;
import es.udc.lbd.hermes.model.sensordata.VBigDecimal;
import es.udc.lbd.hermes.model.sensordata.VDate;
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
		
		if (sensoresJson != null){
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
		
		
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public Row obtenerInfoPorDia(SensorDataType tipo, Long idUsuario, Calendar fechaIni, Calendar fechaFin){

		List<SensorData> informacion = sensorDataDao.informacionPorDia(tipo, idUsuario, fechaIni, fechaFin);
		
		Row row = new Row();	
		for (SensorData sensorData : informacion) {
			
			C puntoStart =new C();
			C puntoEnd =new C();
			Calendar start= sensorData.getStartime();
			Calendar end= sensorData.getEndtime();
			VDate vDateStart = new VDate(calendarToString(start));
			VDate vDateEnd = new VDate(calendarToString(end));
			
			puntoStart.addV(vDateStart);
			puntoEnd.addV(vDateEnd);
			
			if(sensorData.getValues()!=null && sensorData.getValues().length>2){				
				VBigDecimal vBigDecimalX = new VBigDecimal();
				vBigDecimalX.setV(sensorData.getValues()[0]);
				puntoStart.addV(vBigDecimalX);
				puntoEnd.addV(vBigDecimalX);
				
				VBigDecimal vBigDecimalY = new VBigDecimal();
				vBigDecimalY.setV(sensorData.getValues()[1]);
				puntoStart.addV(vBigDecimalY);
				puntoEnd.addV(vBigDecimalY);
				
				VBigDecimal vBigDecimalZ = new VBigDecimal();
				vBigDecimalZ.setV(sensorData.getValues()[2]);
				puntoStart.addV(vBigDecimalZ);		
				puntoEnd.addV(vBigDecimalZ);	
			}
			row.addC(puntoStart);
			row.addC(puntoEnd);
		}	
		
		return row;
		
	}

	
	private String calendarToString(Calendar calendar){
		String strdate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		strdate = sdf.format(calendar.getTime());
		
		return strdate;
	}
	
	public static Calendar getFecha(String fecha){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date parsedEndDate;
		try {
			parsedEndDate = sdf.parse(fecha);
			Calendar fin = Calendar.getInstance();
			fin.setTime(parsedEndDate);
			return fin;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	
}
