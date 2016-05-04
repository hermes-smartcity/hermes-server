package es.udc.lbd.hermes.model.osmimport.dbattribute.dao;

import java.util.List;

import es.udc.lbd.hermes.model.osmimport.dbattribute.DBAttribute;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface DBAttributeDao extends GenericDao<DBAttribute, Long>{

	public List<DBAttribute> getAll(Long idDbConcept);
}
