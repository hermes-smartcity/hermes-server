package es.udc.lbd.hermes.model.osmimport.dbattribute.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.osmimport.dbattribute.DBAttribute;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

@Repository
public class DBAttributeDaoImpl extends GenericDaoHibernate<DBAttribute, Long> implements DBAttributeDao{

	@SuppressWarnings("unchecked")
	public List<DBAttribute> getAll(Long idDbConcept){
		try {
			return getSession().createCriteria(this.entityClass).add(Restrictions.eq("dbConcept.id", idDbConcept)).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}	
	}
}
