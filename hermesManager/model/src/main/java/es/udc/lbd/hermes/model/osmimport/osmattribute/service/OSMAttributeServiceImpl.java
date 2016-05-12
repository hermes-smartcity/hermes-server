package es.udc.lbd.hermes.model.osmimport.osmattribute.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.osmimport.osmattribute.OsmAttribute;
import es.udc.lbd.hermes.model.osmimport.osmattribute.OsmAttributeDTO;
import es.udc.lbd.hermes.model.osmimport.osmattribute.dao.OsmAttributeDao;
import es.udc.lbd.hermes.model.osmimport.osmconcept.OsmConcept;
import es.udc.lbd.hermes.model.osmimport.osmconcept.dao.OsmConceptDao;

@Service("osmAttributeService")
@Transactional
public class OSMAttributeServiceImpl implements OSMAttributeService{

	@Autowired
	private OsmAttributeDao osmAttributeDao;
	
	@Autowired
	private OsmConceptDao osmConceptDao;
	
	@Transactional(readOnly = true)
	public List<OsmAttribute> getAll(Long idDbConcept){
		return osmAttributeDao.getAll(idDbConcept);
	}
	
	public void delete(Long id){
		osmAttributeDao.delete(id);
	}
	
	public OsmAttribute register(OsmAttributeDTO osmAttributeDto){
		
		//Recuperamos el dbConcept
		OsmConcept osmConcept = osmConceptDao.get(osmAttributeDto.getOsmConcept());
		
		OsmAttribute osmAttribute = new OsmAttribute(osmAttributeDto.getId(), 
				osmAttributeDto.getName(), osmConcept);
		
		osmAttributeDao.create(osmAttribute);
		
		return osmAttribute;
	}
	
	@Transactional(readOnly = true)
	public OsmAttribute get(Long id){
		return osmAttributeDao.get(id);
	}

	public OsmAttribute update(OsmAttributeDTO osmAttributeDto, Long id){
		OsmConcept osmConcept = osmConceptDao.get(osmAttributeDto.getOsmConcept());
		
		OsmAttribute osmAttribute = new OsmAttribute(osmAttributeDto.getId(), 
				osmAttributeDto.getName(), osmConcept);
		
		osmAttributeDao.update(osmAttribute);
		
		return osmAttribute;
	}
}
