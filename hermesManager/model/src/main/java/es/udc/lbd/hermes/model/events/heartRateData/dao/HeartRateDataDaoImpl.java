package es.udc.lbd.hermes.model.events.heartRateData.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.events.heartRateData.HeartRateData;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;


@Repository
public class HeartRateDataDaoImpl extends GenericDaoHibernate<HeartRateData, Long> implements HeartRateDataDao {
		
	@Override
	@SuppressWarnings("unchecked")
	public List<HeartRateData> obterHeartRateData() {
		return getSession().createCriteria(this.entityClass).list();
	}
	
	
	@Override
	public List<HeartRateData> obterHeartRateDataSegunUsuario(Long idUsuario) {

		try {
			return getSession().createCriteria(this.entityClass).add(Restrictions.eq("usuario.id", idUsuario)).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}		
	}	
}
