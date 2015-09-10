package es.enxenio.smart.citydriver.model.events.vehicleLocation.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.spatial.criterion.SpatialRestrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.json.simple.JSONObject;
import org.postgis.Geometry;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import com.vividsolutions.jts.io.WKTReader;


import es.enxenio.smart.citydriver.model.events.dataSection.DataSection;
import es.enxenio.smart.citydriver.model.events.measurement.Measurement;
import es.enxenio.smart.citydriver.model.events.measurement.MeasurementType;
import es.enxenio.smart.citydriver.model.events.vehicleLocation.VehicleLocation;
import es.enxenio.smart.citydriver.model.util.Parella;
import es.enxenio.smart.citydriver.model.util.dao.GenericDaoHibernate;;

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
