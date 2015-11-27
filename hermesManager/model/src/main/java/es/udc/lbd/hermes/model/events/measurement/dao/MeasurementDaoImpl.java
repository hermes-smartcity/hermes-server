package es.udc.lbd.hermes.model.events.measurement.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.measurement.MeasurementType;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;
@Repository
public class MeasurementDaoImpl extends GenericDaoHibernate<Measurement, Long> implements
MeasurementDao {
	@Override
	public List<Measurement> obterMeasurementsSegunTipo(MeasurementType tipo, Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, Geometry bounds,
			int startIndex, int count) {
		
			List<Measurement> elementos = null;
			
			String queryStr =  "from Measurement where  within(position, :bounds) = true ";
			if(idUsuario!=null)
				queryStr += "and usuario.id = :idUsuario";
			
			queryStr += " and tipo LIKE :tipo ";
			
			if(fechaIni!=null)
				queryStr += "and timestamp > :fechaIni ";
			
			if(fechaFin!=null)
				queryStr += "and timestamp < :fechaFin";
			
			Query query = getSession().createQuery(queryStr);
	
			query.setParameter("bounds", bounds);
			
			if(idUsuario!=null)
				 query.setParameter("idUsuario", idUsuario);
			if(fechaIni!=null)
				 query.setCalendar("fechaIni", fechaIni);
			if(fechaFin!=null)
				query.setCalendar("fechaFin", fechaFin);
			
			if(startIndex!=-1 && count!=-1)
				query.setFirstResult(startIndex).setMaxResults(count);
			
			query.setString("tipo", tipo.getName());
			elementos = query.list();
			return elementos;
		
	}
	
	@Override
	public long contar(MeasurementType tipo) {
		return (Long) getSession()
				.createQuery("select count(*) from Measurement where tipo LIKE :tipo").setString("tipo", tipo.getName())
				.uniqueResult();
	}

}
