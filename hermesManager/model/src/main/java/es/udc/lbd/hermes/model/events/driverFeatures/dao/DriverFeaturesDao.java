package es.udc.lbd.hermes.model.events.driverFeatures.dao;

import java.util.List;

import es.udc.lbd.hermes.model.events.driverFeatures.DriverFeatures;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface DriverFeaturesDao extends GenericDao<DriverFeatures, Long> {	
	public List<DriverFeatures> obterDriverFeaturess();
	public List<DriverFeatures> obterDriverFeaturessSegunUsuario(Long idUsuario);
}
