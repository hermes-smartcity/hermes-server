package es.udc.lbd.hermes.model.events.driverFeatures.service;

import java.util.List;

import es.udc.lbd.hermes.model.events.driverFeatures.DriverFeatures;

public interface DriverFeaturesService {

	public DriverFeatures get(Long id);
	
	public void create(DriverFeatures dataSection, String sourceId);
	
	public void update(DriverFeatures dataSection);
	
	public void delete(Long id);

	public List<DriverFeatures> obterDriverFeaturess();
	
	public List<DriverFeatures> obterDriverFeaturessSegunUsuario(Long idUsuario);
	
	public long contar();
}
