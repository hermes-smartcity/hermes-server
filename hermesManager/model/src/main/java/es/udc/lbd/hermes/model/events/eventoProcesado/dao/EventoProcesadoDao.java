package es.udc.lbd.hermes.model.events.eventoProcesado.dao;

import es.udc.lbd.hermes.model.events.eventoProcesado.EventoProcesado;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface EventoProcesadoDao extends GenericDao<EventoProcesado, Long> {	
	public EventoProcesado obterEventoProcesado();
	public void eliminarEventosProcesados();
}
