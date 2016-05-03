package es.udc.lbd.hermes.model.events.stepsData.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.CalendarType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.stepsData.StepsData;
import es.udc.lbd.hermes.model.events.stepsData.StepsDataDTO;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;


@Repository
public class StepsDataDaoImpl extends GenericDaoHibernate<StepsData, Long> implements StepsDataDao {
		
	@Override
	@SuppressWarnings("unchecked")
	public List<StepsData> obterStepsData() {
		return getSession().createCriteria(this.entityClass).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StepsData> obterStepsDataSegunUsuario(Long idUsuario) {

		try {
			return getSession().createCriteria(this.entityClass).add(Restrictions.eq("usuarioMovil.id", idUsuario)).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}		
	}	
	
	@Override
	public long contar() {
		return (Long) getSession()
				.createQuery("select count(*) from StepsData")
				.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EventosPorDia> eventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin) {
		String queryStr = "select extract(day from generateddate) as dia, extract(month from generateddate) as mes, extract (year from generateddate) as anio, "
				+ "greatest(neventos, 0)  as neventos from "
				+ " (select cast(v.timelog as date), count(*) as neventos "
				+ " from StepsData v "
				+ "	where (v.timelog > :fechaIni "; 

		if(idUsuario!=null)
			queryStr += " and v.idusuariomovil = :idUsuario ";

		queryStr += " and v.timelog < :fechaFin ) "
				+ "  group by cast(v.timelog as date)) as eventos right join "
				+ "(select cast(:fechaIni as date) + s AS generateddate from generate_series(0,(cast(:fechaFin as date) - cast(:fechaIni as date)),1) as s) as todoslosdias "
				+ " on cast(eventos.timelog as date) = generateddate order by anio, mes, dia";


		SQLQuery query = getSession().createSQLQuery(queryStr);

		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);

		query.setResultTransformer(Transformers.aliasToBean(EventosPorDia.class));
		return (List<EventosPorDia>) query.list();
	}
	
	public Long contarStepsData(Long idUsuario, Calendar fechaIni, Calendar fechaFin){

		String queryStr =  "select count(*) from StepsData where 1 = 1 ";
		if(idUsuario!=null)
			queryStr += "and usuarioMovil.id = :idUsuario ";

		queryStr += "and timelog > :fechaIni ";
		queryStr += "and timelog < :fechaFin ";

		Query query = getSession().createQuery(queryStr);

		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);

		Long numero  = (Long) query.uniqueResult();

		return numero;
	}
	
	@SuppressWarnings("unchecked")
	public List<StepsDataDTO> obterStepsDataWithLimit(Long idUsuario, Calendar fechaIni, 
			Calendar fechaFin, int startIndex, Integer limit){

		List<StepsDataDTO> elementos = null;
		
		String queryStr = "select id as id, eventid as \"eventId\", timelog as \"timeLog\", "
				+ "steps as steps, "
				+ "(select sourceid from usuario_movil where id = idUsuarioMovil) as \"userId\" "
				+ "from stepsdata where 1 = 1 ";
		if(idUsuario!=null)
			queryStr += "and idUsuarioMovil = :idUsuario ";

		queryStr += "and timelog > :fechaIni ";
		queryStr += "and timelog < :fechaFin ";


		SQLQuery query = getSession().createSQLQuery(queryStr);
		query.addScalar("id", LongType.INSTANCE);
		query.addScalar("eventId", StringType.INSTANCE);
		query.addScalar("timeLog", CalendarType.INSTANCE);
		query.addScalar("steps", IntegerType.INSTANCE);
		query.addScalar("userId", StringType.INSTANCE);
				
		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);

		if(startIndex!=-1)
            query.setFirstResult(startIndex);
     
		if(limit!=-1)                                
            query.setMaxResults(limit);

		query.setResultTransformer(Transformers.aliasToBean(StepsDataDTO.class));
		elementos = query.list();
		
		return elementos;
	}
	
	@SuppressWarnings("unchecked")
	public List<StepsData> obterStepsData(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, int startIndex, int count){
		
		List<StepsData> elementos = null;

		String queryStr =  "from StepsData d where 1=1 ";
		if(idUsuario!=null)
			queryStr += "and d.usuarioMovil.id = :idUsuario ";

		if(fechaIni!=null)
			queryStr += "and d.timeLog > :fechaIni ";

		if(fechaFin!=null)
			queryStr += "and d.timeLog < :fechaFin";

		Query query = getSession().createQuery(queryStr);

		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);
		if(fechaIni!=null)
			query.setCalendar("fechaIni", fechaIni);
		if(fechaFin!=null)
			query.setCalendar("fechaFin", fechaFin);
		if(startIndex!=-1 && count!=-1)
			query.setFirstResult(startIndex).setMaxResults(count);

		elementos = query.list();
		return elementos;
	}
}
