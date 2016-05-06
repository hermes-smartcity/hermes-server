package es.udc.lbd.hermes.model.osmimport.dbconcept.service;

import java.util.List;

import es.udc.lbd.hermes.model.osmimport.dbconcept.DBConcept;
import es.udc.lbd.hermes.model.osmimport.dbconcept.DBConceptCustom;

public interface DBConceptService {

	public List<DBConcept> getAll();
	
	public List<DBConceptCustom> getFromTableAll();
	
	public void delete(Long id); 
	
	public DBConcept register(DBConcept dbConcept);
	
	public DBConcept get(Long id);
	
	public DBConcept update(DBConcept dbConcept, Long id);
}
