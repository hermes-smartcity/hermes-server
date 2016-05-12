package es.udc.lbd.hermes.model.osmimport.attributemapping.service;

import java.util.List;

import es.udc.lbd.hermes.model.osmimport.attributemapping.AttributeMapping;
import es.udc.lbd.hermes.model.osmimport.attributemapping.AttributeMappingDTO;

public interface AttributeMappingService {

	public List<AttributeMapping> getAll(Long idConceptTransformation);
	public void delete(Long id);
	public void register(AttributeMappingDTO attributeMappingDto);
	public AttributeMapping get(Long id);
	public void update(AttributeMappingDTO attributeMappingDto, Long id);
}
