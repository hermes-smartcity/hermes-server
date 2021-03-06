package es.udc.lbd.hermes.model.events.eventoProcesado.dao;


import java.math.BigInteger;
import java.util.Calendar;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.events.eventoProcesado.EventoProcesado;
import es.udc.lbd.hermes.model.util.HelpersModel;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

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
	
	// TODO falta
	//Events today
	@Override
	public Long getEventsToday() {
		Calendar hoy = HelpersModel.getHoy();
		String queryString = "SELECT((select count(*) from VehicleLocation where timestamp > :hoy) "
				+ "+ (select count(*) from Measurement where timestamp > :hoy)"
		+ "+ (select count(*) from DataSection where timestamp > :hoy))";
		
		Query query = getSession().createSQLQuery(queryString).setCalendar("hoy", hoy);
		BigInteger count = (BigInteger) query.uniqueResult();      
	    return count.longValue();
	}
	
}
