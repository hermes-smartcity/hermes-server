package es.udc.lbd.hermes.model.osmimport.osmattribute.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.osmimport.osmattribute.OsmAttribute;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

@Repository
public class OsmAttributeDaoImpl extends GenericDaoHibernate<OsmAttribute, Long> implements OsmAttributeDao{

	@SuppressWarnings("unchecked")
	public List<OsmAttribute> getAll(Long idOsmConcept){
		try {
			return getSession().createCriteria(this.entityClass).add(Restrictions.eq("osmConcept.id", idOsmConcept)).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}	
	}
}
