package es.udc.lbd.hermes.model.osmimport.attributemapping.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.osmimport.attributemapping.AttributeMapping;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

@Repository
public class AttributeMappingDaoImpl extends GenericDaoHibernate<AttributeMapping, Long> implements AttributeMappingDao{

	@SuppressWarnings("unchecked")
	public List<AttributeMapping> getAll(Long idConceptTransformation){
		try {
			return getSession().createCriteria(this.entityClass).add(Restrictions.eq("conceptTransformation.id", idConceptTransformation)).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}	
	}
}
