package es.udc.lbd.hermes.model.osmimport.dbconcept.service;

import java.util.List;

import es.udc.lbd.hermes.model.osmimport.dbconcept.DBConcept;

public interface DBConceptService {

	public List<DBConcept> getAll();
	
	public void delete(Long id); 
	
	public DBConcept register(DBConcept dbConcept);
	
	public DBConcept get(Long id);
	
	public DBConcept update(DBConcept dbConcept, Long id);
	
	public String recuperaGeojsonDdConcept(Long dbconceptId, Double nwLng, Double nwLat, Double seLng, Double seLat)  throws Exception;
}
