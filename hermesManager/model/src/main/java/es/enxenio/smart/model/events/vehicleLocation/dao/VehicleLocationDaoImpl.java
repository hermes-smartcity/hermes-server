package es.enxenio.smart.model.events.vehicleLocation.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import es.enxenio.smart.model.events.vehicleLocation.VehicleLocation;
import es.enxenio.smart.model.util.dao.GenericDaoHibernate;



@Repository
public class VehicleLocationDaoImpl extends GenericDaoHibernate<VehicleLocation, Long> implements
VehicleLocationDao {
		
	@Override
	@SuppressWarnings("unchecked")
	public List<VehicleLocation> obterVehicleLocations() {
		return getSession().createCriteria(this.entityClass).list();
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
