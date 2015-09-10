package es.enxenio.smart.citydriver.model.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.enxenio.smart.citydriver.EventManager;
import es.enxenio.smart.citydriver.model.events.eventoProcesado.EventoProcesado;
import es.enxenio.smart.citydriver.model.events.eventoProcesado.dao.EventoProcesadoDao;
import es.enxenio.smart.citydriver.util.ArrancarEventManagerException;
import es.enxenio.smart.citydriver.util.PararEventManagerException;



@Service("eventService")
@Transactional
public class EventServiceImpl implements EventService {
	
	@Autowired
	private EventoProcesadoDao eventoProcesadoDao;
	
	public void startEventManager(EventManager eventManager) throws ArrancarEventManagerException {
		if(eventManager.getSemaphore().tryAcquire()){
			eventManager.startEventProcessor();
			eventManager.getSemaphore().release();	
		} else throw new ArrancarEventManagerException();
	}

	public void stopEventManager(EventManager eventManager) throws PararEventManagerException {		
		if(eventManager.getSemaphore().tryAcquire()){
			eventManager.stopEventProcessor();
			eventManager.getSemaphore().release();
		} else throw new PararEventManagerException();
	}
	
	@Override
	@Transactional(readOnly = true)
	public EventoProcesado get(Long id) {
		return eventoProcesadoDao.get(id);
	}

	@Override
	public void create(EventoProcesado eventoProcesado) {
		eventoProcesadoDao.create(eventoProcesado);
		
	}

	@Override
	public void update(EventoProcesado eventoProcesado) {
		eventoProcesadoDao.update(eventoProcesado);
	}

	@Override
	public void delete(Long id) {
		EventoProcesado eventoProcesado = eventoProcesadoDao.get(id);
		if (eventoProcesado != null) {
			eventoProcesadoDao.delete(id);
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public EventoProcesado obterEventoProcesado() {
		return eventoProcesadoDao.obterEventoProcesado();
	}
	
	@Override
	public void eliminarEventosProcesados() {
		eventoProcesadoDao.eliminarEventosProcesados();		
	}
}
