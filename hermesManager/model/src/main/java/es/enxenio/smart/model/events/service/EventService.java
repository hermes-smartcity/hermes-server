package es.enxenio.smart.model.events.service;

import es.enxenio.smart.model.events.eventoProcesado.EventoProcesado;


public interface EventService {
	
	public EventoProcesado get(Long id);
	
	public void create(EventoProcesado eventoProcesado);
	
	public void update(EventoProcesado eventoProcesado);
	
	public void delete(Long id);
	
	public EventoProcesado obterEventoProcesado();
	
	public void eliminarEventosProcesados();
}
