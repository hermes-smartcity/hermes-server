package es.udc.lbd.hermes.model.events.vehicleLocation.dao;

import java.util.Calendar;
import java.util.List;


import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;


@Repository
public class VehicleLocationDaoImpl extends GenericDaoHibernate<VehicleLocation, Long> implements
VehicleLocationDao {
		
	@Override
	@SuppressWarnings("unchecked")
	public List<VehicleLocation> obterVehicleLocations(Long idUsuario, Calendar fechaIni, Calendar fechaFin, Geometry bounds,
			int startIndex, int count){
    	
			List<VehicleLocation> elementos = null;
		
				String queryStr =  "from VehicleLocation where within(position, :bounds) = true ";
				if(idUsuario!=null)
					queryStr += "and usuarioMovil.id = :idUsuario ";
				
				queryStr += "and timestamp > :fechaIni ";
				queryStr += "and timestamp < :fechaFin";
				
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
	
	@Override
	public long contar() {
		return (Long) getSession()
				.createQuery("select count(*) from VehicleLocation")
				.uniqueResult();
	}
	
	@Override
	public List<EventosPorDia> eventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin) {
		
		String queryStr="select extract(day from v.timestamp) as dia, extract(month from v.timestamp) as mes, extract(year from v.timestamp) as anio, count(*) as numeroEventos" +
				" from VehicleLocation v where v.timestamp > :fechaIni and v.timestamp < :fechaFin ";
	
		if(idUsuario!=null)
			queryStr += "and v.usuarioMovil.id = :idUsuario ";
		
		queryStr+="group by extract(day from v.timestamp), extract(month from v.timestamp), extract (year from v.timestamp) order by anio, mes, dia";
		
		Query query = getSession().createQuery(queryStr);
		
		if(idUsuario!=null)
			 query.setParameter("idUsuario", idUsuario);
		
		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);
		
		query.setResultTransformer(Transformers.aliasToBean(EventosPorDia.class));
		return (List<EventosPorDia>) query.list();
	}
}
