package es.udc.lbd.hermes.model.events.measurement.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.measurement.MeasurementType;
import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;
@Repository
public class MeasurementDaoImpl extends GenericDaoHibernate<Measurement, Long> implements
MeasurementDao {
	@Override
	public List<Measurement> obterMeasurementsSegunTipo(MeasurementType tipo) {

		try {
			return getSession().createCriteria(this.entityClass).add(Restrictions.eq("tipo", tipo)).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
	
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Measurement> obterMeasurementsSegunTipoByBounds(MeasurementType tipo, Geometry bounds) {
		List<Measurement> elementos = null;

		String queryStr =  "from Measurement where  within(position, :bounds) = true"
				+ " and tipo LIKE :tipo ";
		
		Query query = getSession().createQuery(queryStr);

		elementos = query.setParameter("bounds", bounds).setString("tipo", tipo.getName()).list();
		return elementos;
	}
	
	
	@Override
	public List<Measurement> obterMeasurementsSegunTipoEusuario(MeasurementType tipo, Long idUsuario) {

		try {
			return getSession().createCriteria(this.entityClass).add(Restrictions.eq("tipo", tipo)).add(Restrictions.eq("usuario.id", idUsuario)).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
		
	}
}
