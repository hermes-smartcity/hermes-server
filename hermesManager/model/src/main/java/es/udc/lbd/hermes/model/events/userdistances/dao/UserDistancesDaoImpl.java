package es.udc.lbd.hermes.model.events.userdistances.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.CalendarType;
import org.hibernate.type.FloatType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.userdistances.UserDistances;
import es.udc.lbd.hermes.model.events.userdistances.UserDistancesDTO;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

@Repository
public class UserDistancesDaoImpl extends GenericDaoHibernate<UserDistances, Long> implements UserDistancesDao{

	@SuppressWarnings("unchecked")
	public List<UserDistances> obterUserDistances(){
		return getSession().createCriteria(this.entityClass).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<UserDistances> obterUserDistancesSegunUsuario(Long idUsuario){
		try {
			return getSession().createCriteria(this.entityClass).add(Restrictions.eq("usuarioMovil.id", idUsuario)).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}	
	}
	
	
	public long contar(){
		return (Long) getSession()
				.createQuery("select count(*) from UserDistances")
				.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EventosPorDia> eventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin) {
		String queryStr = "select extract(day from generateddate) as dia, extract(month from generateddate) as mes, extract (year from generateddate) as anio, "
				+ "greatest(neventos, 0)  as neventos from "
				+ " (select cast(v.startTime as date), count(*) as neventos "
				+ " from UserDistances v "
				+ "	where (v.startTime > :fechaIni "; 

		if(idUsuario!=null)
			queryStr += " and v.idusuariomovil = :idUsuario ";

		queryStr += " and v.endTime < :fechaFin ) "
				+ "  group by cast(v.startTime as date)) as eventos right join "
				+ "(select cast(:fechaIni as date) + s AS generateddate from generate_series(0,(cast(:fechaFin as date) - cast(:fechaIni as date)),1) as s) as todoslosdias "
				+ " on cast(eventos.startTime as date) = generateddate order by anio, mes, dia";


		SQLQuery query = getSession().createSQLQuery(queryStr);

		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);

		query.setResultTransformer(Transformers.aliasToBean(EventosPorDia.class));
		return (List<EventosPorDia>) query.list();
	}
	
	public Long contarUserDistances(Long idUsuario, Calendar fechaIni, Calendar fechaFin){

		String queryStr =  "select count(*) from UserDistances where 1 = 1 ";
		if(idUsuario!=null)
			queryStr += "and usuarioMovil.id = :idUsuario ";

		queryStr += "and startTime > :fechaIni ";
		queryStr += "and endTime < :fechaFin ";

		Query query = getSession().createQuery(queryStr);

		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);

		Long numero  = (Long) query.uniqueResult();

		return numero;
	}
	
	@SuppressWarnings("unchecked")
	public List<UserDistancesDTO> obterUserDistancesWithLimit(Long idUsuario, Calendar fechaIni, 
			Calendar fechaFin, int startIndex, Integer limit){

		List<UserDistancesDTO> elementos = null;
		
		String queryStr = "select id as id, startTime as \"startTime\", "
				+ "endTime as \"endTime\", "
				+ "(select sourceid from usuario_movil where id = idUsuarioMovil) as \"userId\", "
				+ "distance as distance "
				+ "from userDistances where 1 = 1 ";
		if(idUsuario!=null)
			queryStr += "and idUsuarioMovil = :idUsuario ";

		queryStr += "and startTime > :fechaIni ";
		queryStr += "and endTime < :fechaFin ";


		SQLQuery query = getSession().createSQLQuery(queryStr);
		query.addScalar("id", LongType.INSTANCE);
		query.addScalar("startTime", CalendarType.INSTANCE);
		query.addScalar("endTime", CalendarType.INSTANCE);	
		query.addScalar("userId", StringType.INSTANCE);
		query.addScalar("distance", FloatType.INSTANCE);
		
		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);

		if(startIndex!=-1)
            query.setFirstResult(startIndex);
     
		if(limit!=-1)                                
            query.setMaxResults(limit);

		query.setResultTransformer(Transformers.aliasToBean(UserDistancesDTO.class));
		elementos = query.list();
		
		return elementos;
	}
	
	@SuppressWarnings("unchecked")
	public List<UserDistances> obterUserDistances(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, int startIndex, int count){
		
		List<UserDistances> elementos = null;

		String queryStr =  "from UserDistances d where 1=1 ";
		if(idUsuario!=null)
			queryStr += "and d.usuarioMovil.id = :idUsuario ";

		if(fechaIni!=null)
			queryStr += "and d.startTime > :fechaIni ";

		if(fechaFin!=null)
			queryStr += "and d.endTime < :fechaFin";

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
		
	public void delete(Long idUsuario, Calendar starttime){
		String queryStr =  "DELETE from UserDistances WHERE usuarioMovil.id = :idUsuario ";
		queryStr += "and extract(day from cast(starttime as date)) = extract(day from cast(:starttime as date)) ";
		
		Query query = getSession().createQuery(queryStr);

		query.setParameter("idUsuario", idUsuario);
		query.setCalendar("starttime", starttime);
		
		query.executeUpdate();
	}
}
