package es.udc.lbd.hermes.model.events.driverFeatures.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.events.driverFeatures.DriverFeatures;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;



@Repository
public class DriverFeaturesDaoImpl extends GenericDaoHibernate<DriverFeatures, Long> implements
DriverFeaturesDao {
	
	@Override
	public List<DriverFeatures> obterDriverFeaturess() {
		return getSession().createCriteria(this.entityClass).list();
	}
	
	@Override
	public List<DriverFeatures> obterDriverFeaturessSegunUsuario(Long idUsuario) {
		try {
			return getSession().createCriteria(this.entityClass).add(Restrictions.eq("usuario.id", idUsuario)).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
		
	}
	
}
