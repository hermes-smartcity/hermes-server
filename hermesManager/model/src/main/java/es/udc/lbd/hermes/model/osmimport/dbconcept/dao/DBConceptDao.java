package es.udc.lbd.hermes.model.osmimport.dbconcept.dao;

import java.util.List;

import es.udc.lbd.hermes.model.osmimport.dbconcept.DBConcept;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface DBConceptDao extends GenericDao< DBConcept, Long>{

	public List<DBConcept> getAll();
}
