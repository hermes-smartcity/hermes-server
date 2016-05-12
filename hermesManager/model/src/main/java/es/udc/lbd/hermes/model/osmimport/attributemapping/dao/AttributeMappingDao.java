package es.udc.lbd.hermes.model.osmimport.attributemapping.dao;

import java.util.List;

import es.udc.lbd.hermes.model.osmimport.attributemapping.AttributeMapping;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface AttributeMappingDao extends GenericDao<AttributeMapping, Long>{

	public List<AttributeMapping> getAll(Long idConceptTransformation);
}
