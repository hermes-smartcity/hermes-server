package es.udc.lbd.hermes.model.osmimport.concepttransformation.service;

import java.util.List;

import es.udc.lbd.hermes.model.osmimport.concepttransformation.ConceptTransformation;
import es.udc.lbd.hermes.model.osmimport.concepttransformation.ConceptTransformationDTO;

public interface ConceptTransformationService {

	public List<ConceptTransformation> getAll(Long idJob);
	public void delete(Long id);
	public void register(ConceptTransformationDTO conceptTranformationDto);
	public ConceptTransformation get(Long id);
	public void update(ConceptTransformationDTO conceptTranformationDto, Long id);
}
