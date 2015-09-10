package es.enxenio.smart.citydriver.model.events.measurement.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import es.enxenio.smart.citydriver.model.events.measurement.Measurement;
import es.enxenio.smart.citydriver.model.events.measurement.MeasurementType;
import es.enxenio.smart.citydriver.model.util.dao.GenericDaoHibernate;;

@Repository
public class MeasurementDaoImpl extends GenericDaoHibernate<Measurement, Long> implements
MeasurementDao {
	@Override
	public List<Measurement> obterMeasurementsSegunTipo(MeasurementType tipo) {

		try {
			return getSession().createCriteria(this.entityClass).add(Restrictions.eq("tipo", tipo.getName())).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
	
	}
	
	@Override
	public List<Measurement> obterMeasurementsSegunTipoEusuario(MeasurementType tipo, Long idUsuario) {

		try {
			return getSession().createCriteria(this.entityClass).add(Restrictions.eq("tipo", tipo.getName())).add(Restrictions.eq("usuario.id", idUsuario)).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
		
	}
}
