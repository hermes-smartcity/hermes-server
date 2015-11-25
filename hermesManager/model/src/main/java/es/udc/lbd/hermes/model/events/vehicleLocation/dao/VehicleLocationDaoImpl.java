package es.udc.lbd.hermes.model.events.vehicleLocation.dao;

import java.util.Calendar;
import java.util.List;


import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;


@Repository
public class VehicleLocationDaoImpl extends GenericDaoHibernate<VehicleLocation, Long> implements
VehicleLocationDao {
		
	@Override
	@SuppressWarnings("unchecked")
	public List<VehicleLocation> obterVehicleLocations(Long idUsuario, Calendar fechaIni, Calendar fechaFin, Geometry bounds){
			List<VehicleLocation> elementos = null;
		
				String queryStr =  "from VehicleLocation where within(position, :bounds) = true ";
				if(idUsuario!=null)
					queryStr += "and usuario.id = :idUsuario ";
				
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
				
				elementos = query.list();
				return elementos;
	}
	
//	@Override
//	@SuppressWarnings("unchecked")
//	public List<VehicleLocation> obterVehicleLocationsByBounds(Geometry bounds) {
//		List<VehicleLocation> elementos = null;
//
//		String queryStr =  "from VehicleLocation where within(position, :bounds) = true";
//		
//		Query query = getSession().createQuery(queryStr);
//
//		elementos = query.setParameter("bounds", bounds).list();
//		return elementos;
//	}
	
	
//	@Override
//	public List<VehicleLocation> obterVehicleLocationsSegunUsuario(Long idUsuario) {
//
//		try {
//			return getSession().createCriteria(this.entityClass).add(Restrictions.eq("usuario.id", idUsuario)).list();
//		} catch (HibernateException e) {
//			throw SessionFactoryUtils.convertHibernateAccessException(e);
//		}
//		
//	}
	
}
