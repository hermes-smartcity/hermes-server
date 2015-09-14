package es.enxenio.smart.citydriver.model.events.service;


import es.enxenio.smart.citydriver.EventManager;
import es.enxenio.smart.citydriver.model.events.eventoProcesado.EventoProcesado;
import es.enxenio.smart.citydriver.util.ArrancarEventManagerException;
import es.enxenio.smart.citydriver.util.PararEventManagerException;

public interface EventService {
	
	public void startEventManager(EventManager eventManager) throws InterruptedException, ArrancarEventManagerException;
	
	public void stopEventManager(EventManager eventManager) throws InterruptedException, PararEventManagerException;
	
	public EventoProcesado get(Long id);
	
	public void create(EventoProcesado eventoProcesado);
	
	public void update(EventoProcesado eventoProcesado);
	
	public void delete(Long id);
	
	public EventoProcesado obterEventoProcesado();
	
	public void eliminarEventosProcesados();
}
