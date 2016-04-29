package es.udc.lbd.hermes.model.events.driverFeatures.service;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.ListaDriverFeatures;
import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.driverFeatures.DriverFeatures;

public interface DriverFeaturesService {

	public DriverFeatures get(Long id);
	
	public void create(DriverFeatures dataSection, String sourceId);
	
	public void update(DriverFeatures dataSection);
	
	public void delete(Long id);

	public List<DriverFeatures> obterDriverFeaturess();
	
	public List<DriverFeatures> obterDriverFeaturesSegunUsuario(Long idUsuario);
	
	public long contar();
	
	public ListaDriverFeatures obterDriverFeatures(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
}
