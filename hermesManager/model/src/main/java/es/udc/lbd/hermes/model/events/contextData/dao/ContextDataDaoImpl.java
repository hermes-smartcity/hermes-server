package es.udc.lbd.hermes.model.events.contextData.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.events.contextData.ContextData;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;


@Repository
public class ContextDataDaoImpl extends GenericDaoHibernate<ContextData, Long> implements ContextDataDao {
		
	@Override
	@SuppressWarnings("unchecked")
	public List<ContextData> obterContextData() {
		return getSession().createCriteria(this.entityClass).list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ContextData> obterContextDataSegunUsuario(Long idUsuario) {

		try {
			return getSession().createCriteria(this.entityClass).add(Restrictions.eq("usuarioMovil.id", idUsuario)).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}		
	}	
}
