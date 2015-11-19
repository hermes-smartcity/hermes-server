package es.udc.lbd.hermes.model.events.sleepData.dao;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.sleepData.SleepData;
import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface SleepDataDao extends GenericDao<SleepData, Long> {
	
	public List<SleepData> obterSleepData();
	public List<SleepData> obterSleepDataSegunUsuario(Long idUsuario);
}
