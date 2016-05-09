package es.udc.lbd.hermes.model.osmimport.osmconcept.service;

import java.util.List;

import es.udc.lbd.hermes.model.osmimport.osmconcept.OsmConcept;

public interface OSMConceptService {

	public List<OsmConcept> getAll();
	
	public void delete(Long id); 
	
	public OsmConcept register(OsmConcept osmConcept);
	
	public OsmConcept get(Long id);
	
	public OsmConcept update(OsmConcept osmConcept, Long id);
}
