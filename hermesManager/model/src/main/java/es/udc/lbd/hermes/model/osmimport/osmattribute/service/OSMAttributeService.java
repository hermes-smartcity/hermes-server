package es.udc.lbd.hermes.model.osmimport.osmattribute.service;

import java.util.List;

import es.udc.lbd.hermes.model.osmimport.osmattribute.OsmAttribute;
import es.udc.lbd.hermes.model.osmimport.osmattribute.OsmAttributeDTO;

public interface OSMAttributeService {

	public List<OsmAttribute> getAll(Long idOsmConcept);
	
	public void delete(Long id); 
	
	public OsmAttribute register(OsmAttributeDTO osmAttributeDto);
	
	public OsmAttribute get(Long id);
	
	public OsmAttribute update(OsmAttributeDTO osmAttributeDto, Long id);
}
