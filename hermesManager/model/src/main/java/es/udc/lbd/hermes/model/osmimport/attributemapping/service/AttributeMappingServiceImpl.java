package es.udc.lbd.hermes.model.osmimport.attributemapping.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.osmimport.attributemapping.AttributeMapping;
import es.udc.lbd.hermes.model.osmimport.attributemapping.AttributeMappingDTO;
import es.udc.lbd.hermes.model.osmimport.attributemapping.dao.AttributeMappingDao;
import es.udc.lbd.hermes.model.osmimport.concepttransformation.ConceptTransformation;
import es.udc.lbd.hermes.model.osmimport.concepttransformation.dao.ConceptTransformationDao;

@Service("attributeMappingService")
@Transactional
public class AttributeMappingServiceImpl implements AttributeMappingService{

	@Autowired
	private AttributeMappingDao attributeMappingDao;
	
	@Autowired
	private ConceptTransformationDao conceptTransformationDao;
	
	@Transactional(readOnly = true)
	public List<AttributeMapping> getAll(Long idConceptTransformation){
		return attributeMappingDao.getAll(idConceptTransformation);
	}
	
	public void delete(Long id){
		attributeMappingDao.delete(id);
	}
	
	public void register(AttributeMappingDTO attributeMappingDto){
		
		ConceptTransformation conceptTransformation = conceptTransformationDao.get(attributeMappingDto.getConceptTransformation());
		
		AttributeMapping attributeMapping = new AttributeMapping(conceptTransformation, attributeMappingDto.getDbAttribute(), attributeMappingDto.getOsmAttribute());
		
		attributeMappingDao.create(attributeMapping);
		
	}
	
	@Transactional(readOnly = true)
	public AttributeMapping get(Long id){
		return attributeMappingDao.get(id);
	}

	public void update(AttributeMappingDTO attributeMappingDto, Long id){
		
		ConceptTransformation conceptTransformation = conceptTransformationDao.get(attributeMappingDto.getConceptTransformation());
		
		AttributeMapping attributeMapping = new AttributeMapping(attributeMappingDto.getId(), conceptTransformation, attributeMappingDto.getDbAttribute(), attributeMappingDto.getOsmAttribute());
				
		attributeMappingDao.update(attributeMapping);
		
	}
}
