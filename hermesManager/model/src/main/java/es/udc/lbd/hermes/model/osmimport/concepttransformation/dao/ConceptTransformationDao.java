package es.udc.lbd.hermes.model.osmimport.concepttransformation.dao;

import java.util.List;

import es.udc.lbd.hermes.model.osmimport.concepttransformation.ConceptTransformation;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface ConceptTransformationDao extends GenericDao<ConceptTransformation, Long>{

	public List<ConceptTransformation> getAll(Long idJob);
}
