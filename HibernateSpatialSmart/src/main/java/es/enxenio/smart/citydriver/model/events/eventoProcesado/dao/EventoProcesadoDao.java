package es.enxenio.smart.citydriver.model.events.eventoProcesado.dao;

import es.enxenio.smart.citydriver.model.events.eventoProcesado.EventoProcesado;
import es.enxenio.smart.citydriver.model.util.dao.GenericDao;

public interface EventoProcesadoDao extends GenericDao<EventoProcesado, Long> {	
	public EventoProcesado obterEventoProcesado();
	public void eliminarEventosProcesados();
}
