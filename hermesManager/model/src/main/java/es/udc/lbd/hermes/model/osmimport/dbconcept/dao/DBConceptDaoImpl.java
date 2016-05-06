package es.udc.lbd.hermes.model.osmimport.dbconcept.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.osmimport.dbconcept.DBConcept;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

@Repository
public class DBConceptDaoImpl extends GenericDaoHibernate<DBConcept, Long> implements DBConceptDao{

	@SuppressWarnings("unchecked")
	public List<DBConcept> getAll(){
		return getSession().createCriteria(this.entityClass).list();
	}
}
