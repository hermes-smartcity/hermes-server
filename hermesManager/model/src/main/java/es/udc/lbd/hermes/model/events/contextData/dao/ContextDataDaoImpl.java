package es.udc.lbd.hermes.model.events.contextData.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.spatial.GeometryType;
import org.hibernate.transform.Transformers;
import org.hibernate.type.CalendarType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.contextData.ContextData;
import es.udc.lbd.hermes.model.events.contextData.ContextDataDTO;
import es.udc.lbd.hermes.model.util.GeomUtil;
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
	
	public Long contarContextData(Long idUsuario, Calendar fechaIni, Calendar fechaFin, Geometry bounds){

		String queryStr =  "select count(*) from ContextData where within(position, :bounds) = true ";
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

		Long numero  = (Long) query.uniqueResult();

		return numero;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ContextDataDTO> obterContextDataWithLimit(Long idUsuario, Calendar fechaIni, Calendar fechaFin, Geometry bounds,
			int startIndex, Integer limit){

		List<ContextDataDTO> elementos = null;
		int significantDecimals = GeomUtil.computeSignificantDecimals(bounds);

		String queryStr = "select last(id) as id, last(timelog) as \"timeLog\", last(position) as position, "
				+ "(select sourceid from usuario_movil where id = last(idUsuarioMovil)) as \"userId\", "
				+ "last(accuracy) as accuracy, last(detectedactivity) as \"detectedActivity\" "
				+ "from contextdata where st_within(position, :bounds) = true ";
		
		if(idUsuario!=null)
			queryStr += "and idUsuarioMovil = :idUsuario ";

		queryStr += "and timeLog > :fechaIni ";
		queryStr += "and timeLog < :fechaFin ";
		
		queryStr += "group by round(cast(st_x(position) as numeric), " + significantDecimals + "), round(cast(st_y(position) as numeric), " + significantDecimals + ") ";

		SQLQuery query = getSession().createSQLQuery(queryStr);
		query.addScalar("id", LongType.INSTANCE);
		query.addScalar("timeLog", CalendarType.INSTANCE);
		query.addScalar("position", GeometryType.INSTANCE);		
		query.addScalar("userId", StringType.INSTANCE);
		query.addScalar("accuracy", IntegerType.INSTANCE);
		query.addScalar("detectedActivity", StringType.INSTANCE);
				
		query.setParameter("bounds", bounds);

		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);

		if(startIndex!=-1)
            query.setFirstResult(startIndex);
     
		if(limit!=-1)                                
            query.setMaxResults(limit);

		query.setResultTransformer(Transformers.aliasToBean(ContextDataDTO.class));
		
		elementos = query.list();
		return elementos;
	}
}
