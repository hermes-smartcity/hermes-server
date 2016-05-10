package es.udc.lbd.hermes.model.osmimport.job.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

import es.udc.lbd.hermes.model.osmimport.job.Job;
import es.udc.lbd.hermes.model.osmimport.job.JobDTO;
import es.udc.lbd.hermes.model.osmimport.job.dao.JobDao;

@Service("jobService")
@Transactional
public class JobServiceImpl implements JobService{

	@Autowired
	private JobDao jobDao;
	
	@Transactional(readOnly = true)
	public List<Job> getAll(){
		return jobDao.getAll();
	}
	
	public void delete(Long id){
		jobDao.delete(id);
	}
	
	public void register(JobDTO jobDto){
		
		GeometryFactory fact = new GeometryFactory(new PrecisionModel(), 4326);
		Polygon polygon = (Polygon)fact.toGeometry(new Envelope(jobDto.getSeLng(), jobDto.getNwLng(), jobDto.getSeLat(), jobDto.getNwLat()));
		
		Job job = new Job(jobDto.getName(), polygon);
		
		jobDao.create(job);
		
	}
	
	@Transactional(readOnly = true)
	public Job get(Long id){
		return jobDao.get(id);
	}

	public void update(JobDTO jobDto, Long id){
		
		GeometryFactory fact = new GeometryFactory(new PrecisionModel(), 4326);
		Polygon polygon = (Polygon)fact.toGeometry(new Envelope(jobDto.getSeLng(), jobDto.getNwLng(), jobDto.getSeLat(), jobDto.getNwLat()));
		
		Job job = new Job(jobDto.getId(), jobDto.getName(), polygon);
		
		jobDao.update(job);
		
	}
}
