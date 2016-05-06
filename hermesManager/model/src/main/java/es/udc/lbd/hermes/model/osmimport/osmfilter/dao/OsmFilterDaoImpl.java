package es.udc.lbd.hermes.model.osmimport.osmfilter.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.osmimport.osmfilter.OsmFilter;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

@Repository
public class OsmFilterDaoImpl extends GenericDaoHibernate<OsmFilter, Long> implements OsmFilterDao{

	@SuppressWarnings("unchecked")
	public List<OsmFilter> getAll(Long idOsmConcept){
		try {
			return getSession().createCriteria(this.entityClass).add(Restrictions.eq("osmConcept.id", idOsmConcept)).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}	
	}
}
