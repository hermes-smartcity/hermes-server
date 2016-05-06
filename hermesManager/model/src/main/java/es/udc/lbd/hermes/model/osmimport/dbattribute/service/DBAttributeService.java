package es.udc.lbd.hermes.model.osmimport.dbattribute.service;

import java.util.List;

import es.udc.lbd.hermes.model.osmimport.dbattribute.DBAttribute;
import es.udc.lbd.hermes.model.osmimport.dbattribute.DBAttributeDTO;

public interface DBAttributeService {

	public List<DBAttribute> getAll(Long idDbConcept);
	
	public void delete(Long id); 
	
	public DBAttribute register(DBAttributeDTO dbAttributeDto);
	
	public DBAttribute get(Long id);
	
	public DBAttribute update(DBAttributeDTO dbAttributeDto, Long id);
}
