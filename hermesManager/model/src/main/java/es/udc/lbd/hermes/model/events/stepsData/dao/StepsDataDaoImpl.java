package es.udc.lbd.hermes.model.events.stepsData.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.events.stepsData.StepsData;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;


@Repository
public class StepsDataDaoImpl extends GenericDaoHibernate<StepsData, Long> implements StepsDataDao {
		
	@Override
	@SuppressWarnings("unchecked")
	public List<StepsData> obterStepsData() {
		return getSession().createCriteria(this.entityClass).list();
	}
	
	
	@Override
	public List<StepsData> obterStepsDataSegunUsuario(Long idUsuario) {

		try {
			return getSession().createCriteria(this.entityClass).add(Restrictions.eq("usuarioMovil.id", idUsuario)).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}		
	}	
	
	@Override
	public long contar() {
		return (Long) getSession()
				.createQuery("select count(*) from StepsData")
				.uniqueResult();
	}
}
