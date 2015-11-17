package es.udc.lbd.hermes.model.events.vehicleLocation.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;


@Repository
public class VehicleLocationDaoImpl extends GenericDaoHibernate<VehicleLocation, Long> implements
VehicleLocationDao {
		
	@Override
	@SuppressWarnings("unchecked")
	public List<VehicleLocation> obterVehicleLocations() {
		return getSession().createCriteria(this.entityClass).list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<VehicleLocation> obterVehicleLocationsByBounds(Geometry bounds) {
		List<VehicleLocation> elementos = null;

		String queryStr =  "from VehicleLocation where within(position, :bounds) = true";
		
		Query query = getSession().createQuery(queryStr);

		elementos = query.setParameter("bounds", bounds).list();
		return elementos;
	}
	
	
	@Override
	public List<VehicleLocation> obterVehicleLocationsSegunUsuario(Long idUsuario) {

		try {
			return getSession().createCriteria(this.entityClass).add(Restrictions.eq("usuario.id", idUsuario)).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
		
	}
	
}
