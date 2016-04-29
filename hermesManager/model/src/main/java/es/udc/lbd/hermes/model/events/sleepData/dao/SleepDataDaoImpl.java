package es.udc.lbd.hermes.model.events.sleepData.dao;

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
import es.udc.lbd.hermes.model.events.sleepData.SleepData;
import es.udc.lbd.hermes.model.events.sleepData.SleepDataDTO;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;


@Repository
public class SleepDataDaoImpl extends GenericDaoHibernate<SleepData, Long> implements SleepDataDao {
		
	@Override
	@SuppressWarnings("unchecked")
	public List<SleepData> obterSleepData() {
		return getSession().createCriteria(this.entityClass).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SleepData> obterSleepDataSegunUsuario(Long idUsuario) {

		try {
			return getSession().createCriteria(this.entityClass).add(Restrictions.eq("usuario.id", idUsuario)).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}		
	}	
	
	@Override
	public long contar() {
		return (Long) getSession()
				.createQuery("select count(*) from SleepData")
				.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EventosPorDia> eventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin) {
		String queryStr = "select extract(day from generateddate) as dia, extract(month from generateddate) as mes, extract (year from generateddate) as anio, "
				+ "greatest(neventos, 0)  as neventos from "
				+ " (select cast(v.startTime as date), count(*) as neventos "
				+ " from SleepData v "
				+ "	where (v.starttime > :fechaIni "; 

		if(idUsuario!=null)
			queryStr += " and v.idusuariomovil = :idUsuario ";

		queryStr += " and v.endtime < :fechaFin ) "
				+ "  group by cast(v.starttime as date)) as eventos right join "
				+ "(select cast(:fechaIni as date) + s AS generateddate from generate_series(0,(cast(:fechaFin as date) - cast(:fechaIni as date)),1) as s) as todoslosdias "
				+ " on cast(eventos.starttime as date) = generateddate order by anio, mes, dia";


		SQLQuery query = getSession().createSQLQuery(queryStr);

		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);

		query.setResultTransformer(Transformers.aliasToBean(EventosPorDia.class));
		return (List<EventosPorDia>) query.list();
	}
	
	public Long contarSleepData(Long idUsuario, Calendar fechaIni, Calendar fechaFin){

		String queryStr =  "select count(*) from SleepData where 1 = 1 ";
		if(idUsuario!=null)
			queryStr += "and usuarioMovil.id = :idUsuario ";

		queryStr += "and starttime > :fechaIni ";
		queryStr += "and endtime < :fechaFin ";

		Query query = getSession().createQuery(queryStr);

		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);

		Long numero  = (Long) query.uniqueResult();

		return numero;
	}
	
	@SuppressWarnings("unchecked")
	public List<SleepDataDTO> obterSleepDataWithLimit(Long idUsuario, Calendar fechaIni, 
			Calendar fechaFin, int startIndex, Integer limit){

		List<SleepDataDTO> elementos = null;
		
		String queryStr = "select id as id, eventid as \"eventId\", "
				+ "awakenings as awakenings, minutesasleep as \"minutesAsleep\", "
				+ "minutesinbed as \"minutesInBed\", starttime as \"startTime\", "
				+ "endTime as \"endTime\", "
				+ "(select sourceid from usuario_movil where id = idUsuarioMovil) as \"userId\" "
				+ "from sleepdata where 1 = 1 ";
		if(idUsuario!=null)
			queryStr += "and idUsuarioMovil = :idUsuario ";

		queryStr += "and startTime > :fechaIni ";
		queryStr += "and endTime < :fechaFin ";


		SQLQuery query = getSession().createSQLQuery(queryStr);
		query.addScalar("id", LongType.INSTANCE);
		query.addScalar("eventId", StringType.INSTANCE);
		query.addScalar("awakenings", IntegerType.INSTANCE);
		query.addScalar("minutesAsleep", IntegerType.INSTANCE);
		query.addScalar("minutesInBed", IntegerType.INSTANCE);
		query.addScalar("startTime", CalendarType.INSTANCE);
		query.addScalar("endTime", CalendarType.INSTANCE);	
		query.addScalar("userId", StringType.INSTANCE);
				
		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);

		if(startIndex!=-1)
            query.setFirstResult(startIndex);
     
		if(limit!=-1)                                
            query.setMaxResults(limit);

		query.setResultTransformer(Transformers.aliasToBean(SleepDataDTO.class));
		elementos = query.list();
		
		return elementos;
	}
}
