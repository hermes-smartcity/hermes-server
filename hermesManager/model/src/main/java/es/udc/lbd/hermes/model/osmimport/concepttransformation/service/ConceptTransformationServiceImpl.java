package es.udc.lbd.hermes.model.osmimport.concepttransformation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

import es.udc.lbd.hermes.model.osmimport.concepttransformation.ConceptTransformation;
import es.udc.lbd.hermes.model.osmimport.concepttransformation.ConceptTransformationDTO;
import es.udc.lbd.hermes.model.osmimport.concepttransformation.dao.ConceptTransformationDao;
import es.udc.lbd.hermes.model.osmimport.job.Job;
import es.udc.lbd.hermes.model.osmimport.job.dao.JobDao;

@Service("conceptTransformationService")
@Transactional
public class ConceptTransformationServiceImpl implements ConceptTransformationService{

	@Autowired
	private ConceptTransformationDao conceptTransformationDao;
	
	@Autowired
	private JobDao jobDao;
	
	@Transactional(readOnly = true)
	public List<ConceptTransformation> getAll(Long idJob){
		return conceptTransformationDao.getAll(idJob);
	}
	
	public void delete(Long id){
		conceptTransformationDao.delete(id);
	}
	
	public void register(ConceptTransformationDTO conceptTranformationDto){
		
		Polygon polygon = null;
		if (conceptTranformationDto.getSeLng() != null && conceptTranformationDto.getNwLng()!= null && 
				conceptTranformationDto.getSeLat()!= null && conceptTranformationDto.getNwLat()!=null){
		
			GeometryFactory fact = new GeometryFactory(new PrecisionModel(), 4326);
			polygon = (Polygon)fact.toGeometry(new Envelope(conceptTranformationDto.getSeLng(), conceptTranformationDto.getNwLng(), conceptTranformationDto.getSeLat(), conceptTranformationDto.getNwLat()));
			
		}
		
		Job job = jobDao.get(conceptTranformationDto.getJob());
		
		ConceptTransformation conceptTransformation = new ConceptTransformation(polygon,
				job, conceptTranformationDto.getDbConcept(),
				conceptTranformationDto.getOsmConcept());
		
		conceptTransformationDao.create(conceptTransformation);
		
	}
	
	@Transactional(readOnly = true)
	public ConceptTransformation get(Long id){
		return conceptTransformationDao.get(id);
	}

	public void update(ConceptTransformationDTO conceptTranformationDto, Long id){
		
		Polygon polygon = null;
		if (conceptTranformationDto.getSeLng() != null && conceptTranformationDto.getNwLng()!= null && 
				conceptTranformationDto.getSeLat()!= null && conceptTranformationDto.getNwLat()!=null){
		
			GeometryFactory fact = new GeometryFactory(new PrecisionModel(), 4326);
			polygon = (Polygon)fact.toGeometry(new Envelope(conceptTranformationDto.getSeLng(), conceptTranformationDto.getNwLng(), conceptTranformationDto.getSeLat(), conceptTranformationDto.getNwLat()));
			
		}
		
		Job job = jobDao.get(conceptTranformationDto.getJob());
		
		ConceptTransformation conceptTransformation = new ConceptTransformation(conceptTranformationDto.getId(), polygon,
				job, conceptTranformationDto.getDbConcept(),
				conceptTranformationDto.getOsmConcept());
		
		conceptTransformationDao.update(conceptTransformation);
		
	}
}
