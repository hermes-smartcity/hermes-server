package es.udc.lbd.hermes.model.sensordata.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.sensordata.SensorData;
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
}
