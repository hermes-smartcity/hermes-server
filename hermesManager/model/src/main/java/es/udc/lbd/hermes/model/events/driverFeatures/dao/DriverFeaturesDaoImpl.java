package es.udc.lbd.hermes.model.events.driverFeatures.dao;

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
import es.udc.lbd.hermes.model.events.driverFeatures.DriverFeatures;
import es.udc.lbd.hermes.model.events.driverFeatures.DriverFeaturesDTO;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;



@Repository
public class DriverFeaturesDaoImpl extends GenericDaoHibernate<DriverFeatures, Long> implements
DriverFeaturesDao {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DriverFeatures> obterDriverFeaturess() {
		return getSession().createCriteria(this.entityClass).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DriverFeatures> obterDriverFeaturesSegunUsuario(Long idUsuario) {
		try {
			return getSession().createCriteria(this.entityClass).add(Restrictions.eq("usuario.id", idUsuario)).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
		
	}
	
	@Override
	public long contar() {
		return (Long) getSession()
				.createQuery("select count(*) from DriverFeatures")
				.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EventosPorDia> eventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin) {
		String queryStr = "select extract(day from generateddate) as dia, extract(month from generateddate) as mes, extract (year from generateddate) as anio, "
				+ "greatest(neventos, 0)  as neventos from "
				+ " (select cast(v.timestamp as date), count(*) as neventos "
				+ " from DriverFeatures v "
				+ "	where (v.timestamp > :fechaIni "; 

		if(idUsuario!=null)
			queryStr += " and v.idusuariomovil = :idUsuario ";

		queryStr += " and v.endTime < :fechaFin ) "
				+ "  group by cast(v.timestamp as date)) as eventos right join "
				+ "(select cast(:fechaIni as date) + s AS generateddate from generate_series(0,(cast(:fechaFin as date) - cast(:fechaIni as date)),1) as s) as todoslosdias "
				+ " on cast(eventos.timestamp as date) = generateddate order by anio, mes, dia";


		SQLQuery query = getSession().createSQLQuery(queryStr);

		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);

		query.setResultTransformer(Transformers.aliasToBean(EventosPorDia.class));
		return (List<EventosPorDia>) query.list();
	}
	
	public Long contarDriverFeatures(Long idUsuario, Calendar fechaIni, Calendar fechaFin){

		String queryStr =  "select count(*) from DriverFeatures where 1 = 1 ";
		if(idUsuario!=null)
			queryStr += "and usuarioMovil.id = :idUsuario ";

		queryStr += "and timestamp > :fechaIni ";
		queryStr += "and timestamp < :fechaFin ";

		Query query = getSession().createQuery(queryStr);

		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);

		Long numero  = (Long) query.uniqueResult();

		return numero;
	}
	
	@SuppressWarnings("unchecked")
	public List<DriverFeaturesDTO> obterDriverFeaturesWithLimit(Long idUsuario, Calendar fechaIni, 
			Calendar fechaFin, int startIndex, Integer limit){

		List<DriverFeaturesDTO> elementos = null;
		
		String queryStr = "select id as id, awakefor as \"awakeFor\", inbed as \"inBed\", "
				+ "workingtime as \"workingTime\", lightsleep as \"lightSleep\", "
				+ "deepsleep as \"deepSleep\", previousstress as \"previousStress\", " 
				+ "timestamp as \"timeStamp\", "
				+ "(select sourceid from usuario_movil where id = idUsuarioMovil) as \"userId\" "
				+ "from driverfeatures where 1 = 1 ";
		if(idUsuario!=null)
			queryStr += "and idUsuarioMovil = :idUsuario ";

		queryStr += "and timestamp > :fechaIni ";
		queryStr += "and timestamp < :fechaFin ";


		SQLQuery query = getSession().createSQLQuery(queryStr);
		query.addScalar("id", LongType.INSTANCE);
		query.addScalar("awakeFor", IntegerType.INSTANCE);
		query.addScalar("inBed", IntegerType.INSTANCE);
		query.addScalar("workingTime", IntegerType.INSTANCE);
		query.addScalar("lightSleep", IntegerType.INSTANCE);
		query.addScalar("deepSleep", IntegerType.INSTANCE);
		query.addScalar("previousStress", IntegerType.INSTANCE);
		query.addScalar("timeStamp", CalendarType.INSTANCE);
		query.addScalar("userId", StringType.INSTANCE);
				
		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);

		if(startIndex!=-1)
            query.setFirstResult(startIndex);
     
		if(limit!=-1)                                
            query.setMaxResults(limit);

		query.setResultTransformer(Transformers.aliasToBean(DriverFeaturesDTO.class));
		elementos = query.list();
		
		return elementos;
	}

	
}
