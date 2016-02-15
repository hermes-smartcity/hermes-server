package es.udc.lbd.hermes.model.events.service;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.events.eventoProcesado.EventoProcesado;
import es.udc.lbd.hermes.model.events.eventoProcesado.dao.EventoProcesadoDao;


@Service("eventService")
@Transactional
public class EventServiceImpl implements EventService {
	
	static Logger logger = Logger.getLogger(EventServiceImpl.class);
	@Autowired
	private EventoProcesadoDao eventoProcesadoDao;
		
	@Override
	@Transactional(readOnly = true)
	public EventoProcesado get(Long id) {
		return eventoProcesadoDao.get(id);
	}

	@Override
	public void create(Calendar timestamp, String eventId) {
		eventoProcesadoDao.eliminarEventosProcesados();
		EventoProcesado eventoProcesado = new EventoProcesado();
		eventoProcesado.setEventId(eventId);
		eventoProcesado.setTimestamp(timestamp);
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
	
	@Override
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public Long getEventsToday() {
		return eventoProcesadoDao.getEventsToday();		
	}
	
}
