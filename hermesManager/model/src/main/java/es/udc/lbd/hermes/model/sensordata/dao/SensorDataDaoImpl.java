package es.udc.lbd.hermes.model.sensordata.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigIntegerType;
import org.hibernate.type.CalendarType;
import org.hibernate.type.CustomType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.sensordata.SensorData;
import es.udc.lbd.hermes.model.sensordata.SensorDataDTO;
import es.udc.lbd.hermes.model.sensordata.SensorDataType;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;
import es.udc.lbd.hermes.model.util.hibernate.NumericArrayUserType;

@Repository
public class SensorDataDaoImpl extends GenericDaoHibernate<SensorData, Long> implements SensorDataDao{

	public SensorData findLast(Long userId, String type){
		
		String queryStr =  "from SensorData d where d.usuarioMovil.id = :idUsuario " +
							"and d.typesensor = :typesensor " +
							"ORDER BY d.startime DESC";
		
		
		Query query = getSession().createQuery(queryStr);
		query.setMaxResults(1);

		query.setParameter("idUsuario", userId);
		query.setParameter("typesensor", type);
			
		return (SensorData) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public Long countRows(SensorDataType tipo, Long idUsuario, Calendar fechaIni, Calendar fechaFin){
		
		String queryStr =  "select count(*)  from SensorData d " +
						"where d.startime > :fechaIni and d.endtime < :fechaFin " +
						"and d.typesensor LIKE :tipo";

		if(idUsuario!=null)
			queryStr += " and d.usuarioMovil.id = :idUsuario ";
		
		Query query = getSession().createQuery(queryStr);
				
		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);
		query.setParameter("tipo", tipo.getName());
		
		if (idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);
		
		return (Long) query.uniqueResult();
	}
	
//	@SuppressWarnings("unchecked")
//	public List<SensorData> informacionPorDia(SensorDataType tipo, Long idUsuario, Calendar fechaIni, Calendar fechaFin){
//		
//		String queryStr =  "from SensorData d " +
//						"where d.startime > :fechaIni and d.endtime < :fechaFin " +
//						"and d.typesensor LIKE :tipo order by d.startime";
//
//		if(idUsuario!=null)
//			queryStr += " and d.usuarioMovil.id = :idUsuario ";
//		
//		Query query = getSession().createQuery(queryStr);
//				
//		query.setCalendar("fechaIni", fechaIni);
//		query.setCalendar("fechaFin", fechaFin);
//		query.setParameter("tipo", tipo.getName());
//		
//		if (idUsuario!=null)
//			query.setParameter("idUsuario", idUsuario);
//		
//		return (List<SensorData>) query.list();
//	}
	
	@SuppressWarnings("unchecked")
	public List<SensorDataDTO> informacionPorDia(SensorDataType tipo, Long idUsuario, Calendar fechaIni, Calendar fechaFin){
		String queryStr =  "select id, typesensor, startime, endtime, values, idusuariomovil from sensordata d " +
						"where cast(extract (epoch from d.startime) as integer) % :cadacuantos = 0 "
						+ "and d.startime > :fechaIni and d.endtime < :fechaFin "
						+ "and d.typesensor LIKE :tipo ";
		if(idUsuario!=null) {
			queryStr += "and d.usuarioMovil.id = :idUsuario ";
		}
		
		queryStr += "order by d.startime";
		System.out.println("-  --- "+queryStr);
		SQLQuery query = getSession().createSQLQuery(queryStr);
		Long numeroDeFilas = countRows(tipo, idUsuario, fechaIni, fechaFin);
		Long cadaCuantos = numeroDeFilas / 1000;
		query.setParameter("cadacuantos", cadaCuantos);
		query.addScalar("id", BigIntegerType.INSTANCE);
		query.addScalar("typesensor", StringType.INSTANCE);		
		query.addScalar("startime", CalendarType.INSTANCE);
		query.addScalar("endtime", CalendarType.INSTANCE);
		query.addScalar("values", new CustomType(new NumericArrayUserType()));		
		query.addScalar("idusuariomovil", BigIntegerType.INSTANCE);
		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);
		query.setParameter("tipo", tipo.getName());
		
		if (idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);
		
		query.setResultTransformer(Transformers.aliasToBean(SensorDataDTO.class));
		return (List<SensorDataDTO>) query.list();
	}
}
