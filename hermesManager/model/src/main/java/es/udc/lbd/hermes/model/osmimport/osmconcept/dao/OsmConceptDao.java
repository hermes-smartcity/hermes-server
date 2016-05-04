package es.udc.lbd.hermes.model.osmimport.osmconcept.dao;

import java.util.List;

import es.udc.lbd.hermes.model.osmimport.osmconcept.OsmConcept;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface OsmConceptDao extends GenericDao<OsmConcept, Long>{

	public List<OsmConcept> getAll();
}
