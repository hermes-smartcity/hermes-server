package es.udc.lbd.hermes.model.events.sleepData.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.events.sleepData.SleepData;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;


@Repository
public class SleepDataDaoImpl extends GenericDaoHibernate<SleepData, Long> implements SleepDataDao {
		
	@Override
	@SuppressWarnings("unchecked")
	public List<SleepData> obterSleepData() {
		return getSession().createCriteria(this.entityClass).list();
	}
	
	
	@Override
	public List<SleepData> obterSleepDataSegunUsuario(Long idUsuario) {

		try {
			return getSession().createCriteria(this.entityClass).add(Restrictions.eq("usuario.id", idUsuario)).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}		
	}	
	
	@Override
	public long contar() {
		return (Long) getSession()
				.createQuery("select count(*) from SleepData")
				.uniqueResult();
	}
}
