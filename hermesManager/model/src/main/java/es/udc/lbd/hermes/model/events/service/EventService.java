package es.udc.lbd.hermes.model.events.service;

import java.util.Calendar;

import es.udc.lbd.hermes.model.events.eventoProcesado.EventoProcesado;

public interface EventService {
	
	public EventoProcesado get(Long id);
	
	public void create(Calendar timestamp, String eventId);
	
	public void update(EventoProcesado eventoProcesado);
	
	public void delete(Long id);
	
	public EventoProcesado obterEventoProcesado();
	
	public void eliminarEventosProcesados();
	
	public Long getEventsToday();
}
