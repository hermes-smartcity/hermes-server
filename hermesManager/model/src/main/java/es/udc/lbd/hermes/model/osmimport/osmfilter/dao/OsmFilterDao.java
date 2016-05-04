package es.udc.lbd.hermes.model.osmimport.osmfilter.dao;

import java.util.List;

import es.udc.lbd.hermes.model.osmimport.osmfilter.OsmFilter;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface OsmFilterDao extends GenericDao<OsmFilter, Long>{

	public List<OsmFilter> getAll(Long idOsmConcept);
}
