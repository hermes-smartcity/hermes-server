package es.udc.lbd.hermes.model.osmimport.osmattribute.dao;

import java.util.List;

import es.udc.lbd.hermes.model.osmimport.osmattribute.OsmAttribute;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface OsmAttributeDao extends GenericDao<OsmAttribute, Long>{

	public List<OsmAttribute> getAll(Long idOsmConcept);
}
