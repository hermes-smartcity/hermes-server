package es.udc.lbd.hermes.model.sensordata.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

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
	public List<SensorData> informacionPorDia(SensorDataType tipo, Long idUsuario, Calendar fechaIni, Calendar fechaFin){
		
		String queryStr =  "from SensorData d " +
						"where d.startime > :fechaIni and d.endtime < :fechaFin " +
						"and d.typesensor LIKE :tipo ";

		if(idUsuario!=null)
			queryStr += " and d.idusuariomovil = :idUsuario ";
		
		Query query = getSession().createQuery(queryStr);
				
		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);
		query.setParameter("tipo", tipo.getName());
		
		if (idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);
		
		return (List<SensorData>) query.list();
	}
}
