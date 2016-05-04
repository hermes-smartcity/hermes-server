package es.udc.lbd.hermes.model.osmimport.osmconcept.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.osmimport.osmconcept.OsmConcept;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

@Repository
public class OsmConceptDaoImpl extends GenericDaoHibernate<OsmConcept, Long> implements OsmConceptDao{

	@SuppressWarnings("unchecked")
	public List<OsmConcept> getAll(){
		return getSession().createCriteria(this.entityClass).list();
	}
}
