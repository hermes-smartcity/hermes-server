package es.udc.lbd.hermes.model.osmimport.osmfilter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.osmimport.osmconcept.OsmConcept;
import es.udc.lbd.hermes.model.osmimport.osmconcept.dao.OsmConceptDao;
import es.udc.lbd.hermes.model.osmimport.osmfilter.OsmFilter;
import es.udc.lbd.hermes.model.osmimport.osmfilter.OsmFilterDTO;
import es.udc.lbd.hermes.model.osmimport.osmfilter.dao.OsmFilterDao;

@Service("osmFilterService")
@Transactional
public class OSMFilterServiceImpl implements OSMFilterService{

	@Autowired
	private OsmFilterDao osmFilterDao;
	
	@Autowired
	private OsmConceptDao osmConceptDao;
	
	@Transactional(readOnly = true)
	public List<OsmFilter> getAll(Long idOsmConcept){
		return osmFilterDao.getAll(idOsmConcept);
	}
	
	public void delete(Long id){
		osmFilterDao.delete(id);
	}
	
	public OsmFilter register(OsmFilterDTO osmFilterDto){
		
		//Recuperamos el dbConcept
		OsmConcept osmConcept = osmConceptDao.get(osmFilterDto.getOsmConcept());
		
		OsmFilter dbAttribute = new OsmFilter(osmFilterDto.getId(), 
				osmFilterDto.getName(), osmFilterDto.getOperation(), osmFilterDto.getValue(), osmConcept);
		
		osmFilterDao.create(dbAttribute);
		
		return dbAttribute;
	}
	
	@Transactional(readOnly = true)
	public OsmFilter get(Long id){
		return osmFilterDao.get(id);
	}

	public OsmFilter update(OsmFilterDTO osmFilterDto, Long id){
		OsmConcept osmConcept = osmConceptDao.get(osmFilterDto.getOsmConcept());
		
		OsmFilter osmFilter = new OsmFilter(osmFilterDto.getId(), 
				osmFilterDto.getName(), osmFilterDto.getOperation(), osmFilterDto.getValue(), osmConcept);
		
		osmFilterDao.update(osmFilter);
		
		return osmFilter;
	}
}
