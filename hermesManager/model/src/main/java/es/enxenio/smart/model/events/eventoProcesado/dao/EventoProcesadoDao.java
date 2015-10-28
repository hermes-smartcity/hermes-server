package es.enxenio.smart.model.events.eventoProcesado.dao;

import es.enxenio.smart.model.events.eventoProcesado.EventoProcesado;
import es.enxenio.smart.model.util.dao.GenericDao;



public interface EventoProcesadoDao extends GenericDao<EventoProcesado, Long> {	
	public EventoProcesado obterEventoProcesado();
	public void eliminarEventosProcesados();
}
