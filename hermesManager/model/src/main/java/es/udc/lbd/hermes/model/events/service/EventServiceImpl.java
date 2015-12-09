package es.udc.lbd.hermes.model.events.service;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.events.eventoProcesado.EventoProcesado;
import es.udc.lbd.hermes.model.events.eventoProcesado.dao.EventoProcesadoDao;


@Service("eventService")
@Transactional
public class EventServiceImpl implements EventService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
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
	public Long getEventsToday() {
		return eventoProcesadoDao.getEventsToday();		
	}
	
}
