package es.udc.lbd.hermes.model.osmimport.concepttransformation.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.osmimport.concepttransformation.ConceptTransformation;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

@Repository
public class ConceptTransformationDaoImpl extends GenericDaoHibernate<ConceptTransformation, Long> implements ConceptTransformationDao{

	@SuppressWarnings("unchecked")
	public List<ConceptTransformation> getAll(Long idJob){
		try {
			return getSession().createCriteria(this.entityClass).add(Restrictions.eq("job.id", idJob)).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}	
	}
}
