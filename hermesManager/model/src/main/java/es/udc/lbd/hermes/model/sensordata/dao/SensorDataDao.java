package es.udc.lbd.hermes.model.sensordata.dao;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.sensordata.SensorData;
import es.udc.lbd.hermes.model.sensordata.SensorDataType;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface SensorDataDao extends GenericDao<SensorData, Long>{

	public SensorData findLast(Long userId, String type);
	
	public List<SensorData> informacionPorDia(SensorDataType tipo,Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public Long getCadaCuantos(SensorDataType tipo, Long idUsuario, Calendar fechaIni, Calendar fechaFin);
}
