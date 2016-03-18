package es.udc.lbd.hermes.model.events.contextData.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.contextData.ContextData;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;


@Repository
public class ContextDataDaoImpl extends GenericDaoHibernate<ContextData, Long> implements ContextDataDao {
		
	@Override
	@SuppressWarnings("unchecked")
	public List<ContextData> obterContextData() {
		return getSession().createCriteria(this.entityClass).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ContextData> obterContextDataSegunUsuario(Long idUsuario) {

		try {
			return getSession().createCriteria(this.entityClass).add(Restrictions.eq("usuarioMovil.id", idUsuario)).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}		
	}	
	
	@SuppressWarnings("unchecked")
	public List<ContextData> obterContextData(Long idUsuario, Calendar fechaIni, Calendar fechaFin, Geometry bounds,
			int startIndex, int count){

		List<ContextData> elementos = null;

		String queryStr =  "from ContextData where within(position, :bounds) = true ";
		if(idUsuario!=null)
			queryStr += "and usuarioMovil.id = :idUsuario ";

		queryStr += "and timeLog > :fechaIni ";
		queryStr += "and timeLog < :fechaFin";

		Query query = getSession().createQuery(queryStr);

		query.setParameter("bounds", bounds);

		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);

		if(startIndex!=-1 && count!=-1)
			query.setFirstResult(startIndex).setMaxResults(count);

		elementos = query.list();
		return elementos;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EventosPorDia> eventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin) {
		String queryStr = "select extract(day from generateddate) as dia, extract(month from generateddate) as mes, extract (year from generateddate) as anio, "
				+ "greatest(neventos, 0)  as neventos from "
					+ " (select cast(v.timeLog as date), count(*) as neventos "
					+ " from ContextData v "
					+ "	where (v.timeLog > :fechaIni "; 
	
					if(idUsuario!=null)
						queryStr += " and v.idusuariomovil = :idUsuario ";
				
					queryStr += " and v.timeLog < :fechaFin ) "
					+ "  group by cast(v.timeLog as date)) as eventos right join "
				+ "(select cast(:fechaIni as date) + s AS generateddate from generate_series(0,(cast(:fechaFin as date) - cast(:fechaIni as date)),1) as s) as todoslosdias "
				+ " on cast(eventos.timeLog as date) = generateddate order by anio, mes, dia";

		
		SQLQuery query = getSession().createSQLQuery(queryStr);
		
		if(idUsuario!=null)
			 query.setParameter("idUsuario", idUsuario);
		
		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);
		
		query.setResultTransformer(Transformers.aliasToBean(EventosPorDia.class));
		return (List<EventosPorDia>) query.list();
	}
	
	@Override
	public long contar() {
		return (Long) getSession()
				.createQuery("select count(*) from ContextData")
				.uniqueResult();
	}
}
