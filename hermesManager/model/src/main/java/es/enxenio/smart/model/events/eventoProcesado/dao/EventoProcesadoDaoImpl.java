package es.enxenio.smart.model.events.eventoProcesado.dao;


import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import es.enxenio.smart.model.events.eventoProcesado.EventoProcesado;
import es.enxenio.smart.model.util.dao.GenericDaoHibernate;




@Repository
public class EventoProcesadoDaoImpl extends GenericDaoHibernate<EventoProcesado, Long> implements
EventoProcesadoDao {
	
	// Sólo va a haber uno
	@Override
	public EventoProcesado obterEventoProcesado() {
		return (EventoProcesado) getSession().createCriteria(this.entityClass).setMaxResults(1).uniqueResult();
	}
	
	// Solo tiene que haber uno, el último que es el que se va a guardar
	@Override
	public void eliminarEventosProcesados() {
		String queryString = "DELETE FROM EventoProcesado";
		Query query = getSession().createQuery(queryString);
		query.executeUpdate();
		
	}
}
