package es.udc.lbd.hermes.model.osmimport.osmconcept.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.osmimport.osmconcept.OsmConcept;
import es.udc.lbd.hermes.model.osmimport.osmconcept.dao.OsmConceptDao;

@Service("osmConceptService")
@Transactional
public class OSMConceptServiceImpl implements OSMConceptService{

	@Autowired
	private OsmConceptDao osmConceptDao;
	
	@Transactional(readOnly = true)
	public List<OsmConcept> getAll(){
		return osmConceptDao.getAll();
	}
	
	public void delete(Long id){
		osmConceptDao.delete(id);
	}
	
	public OsmConcept register(OsmConcept osmConcept){
		osmConceptDao.create(osmConcept);
		
		return osmConcept;
	}
	
	@Transactional(readOnly = true)
	public OsmConcept get(Long id){
		return osmConceptDao.get(id);
	}

	public OsmConcept update(OsmConcept osmConcept, Long id){
		osmConceptDao.update(osmConcept);
		
		return osmConcept;
	}
}
