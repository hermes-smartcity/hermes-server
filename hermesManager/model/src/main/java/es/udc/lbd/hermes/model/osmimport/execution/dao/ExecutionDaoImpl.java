package es.udc.lbd.hermes.model.osmimport.execution.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.osmimport.execution.Execution;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

@Repository
public class ExecutionDaoImpl extends GenericDaoHibernate<Execution, Long> implements ExecutionDao{

	@SuppressWarnings("unchecked")
	public List<Execution> getAll(){
		try {
			return getSession().createCriteria(this.entityClass).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}	
	}
}
