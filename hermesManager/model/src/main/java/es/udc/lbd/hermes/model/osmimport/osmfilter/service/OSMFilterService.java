package es.udc.lbd.hermes.model.osmimport.osmfilter.service;

import java.util.List;

import es.udc.lbd.hermes.model.osmimport.osmfilter.OsmFilter;
import es.udc.lbd.hermes.model.osmimport.osmfilter.OsmFilterDTO;

public interface OSMFilterService {

	public List<OsmFilter> getAll(Long idOsmConcept);
	
	public void delete(Long id); 
	
	public OsmFilter register(OsmFilterDTO osmFilterDto);
	
	public OsmFilter get(Long id);
	
	public OsmFilter update(OsmFilterDTO osmFilterDto, Long id);
}
