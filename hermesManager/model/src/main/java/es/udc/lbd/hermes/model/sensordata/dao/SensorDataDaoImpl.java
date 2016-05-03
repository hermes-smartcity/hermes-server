package es.udc.lbd.hermes.model.sensordata.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.sensordata.Row;
import es.udc.lbd.hermes.model.sensordata.SensorData;
import es.udc.lbd.hermes.model.sensordata.SensorDataType;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

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
	public Long getCadaCuantos(SensorDataType tipo, Long idUsuario, Calendar fechaIni, Calendar fechaFin){
		
		String queryStr =  "select count(*)  from SensorData d " +
						"where d.startime > :fechaIni and d.endtime < :fechaFin " +
						"and d.typesensor LIKE :tipo order by d.startime";

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
	
	@SuppressWarnings("unchecked")
	public List<SensorData> informacionPorDia(SensorDataType tipo, Long idUsuario, Calendar fechaIni, Calendar fechaFin){
		
		String queryStr =  "from SensorData d " +
						"where d.startime > :fechaIni and d.endtime < :fechaFin " +
						"and d.typesensor LIKE :tipo order by d.startime";

		if(idUsuario!=null)
			queryStr += " and d.usuarioMovil.id = :idUsuario ";
		
		Query query = getSession().createQuery(queryStr);
				
		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);
		query.setParameter("tipo", tipo.getName());
		
		if (idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);
		
		return (List<SensorData>) query.list();
	}
	
//	@SuppressWarnings("unchecked")
//	public List<SensorData> informacionPorDia(SensorDataType tipo, Long idUsuario, Calendar fechaIni, Calendar fechaFin){
//		// Como no funcionaba borre lo de arriba para simplificar. Incluir! 
//		String queryStr =  "from SensorData d " +
//						"where cast(extract (epoch from d.startime) as integer) % 37 = 0"
//						+ " order by d.startime";
//
//		System.out.println("-  --- "+queryStr);
//		if(idUsuario!=null)
//			queryStr += " and d.usuarioMovil.id = :idUsuario ";
//		
////		Query query = getSession().createQuery(queryStr);
//		SQLQuery query = getSession().createSQLQuery(queryStr);		
////		query.setCalendar("fechaIni", fechaIni);
////		query.setCalendar("fechaFin", fechaFin);
////		query.setParameter("tipo", tipo.getName());
//		
//		if (idUsuario!=null)
//			query.setParameter("idUsuario", idUsuario);
//		
//		return (List<SensorData>) query.list();
//	}
}
