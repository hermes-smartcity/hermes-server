package es.udc.lbd.hermes.model.osmimport.job.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

import es.udc.lbd.hermes.model.osmimport.execution.Execution;
import es.udc.lbd.hermes.model.osmimport.execution.ExecutionStatus;
import es.udc.lbd.hermes.model.osmimport.execution.dao.ExecutionDao;
import es.udc.lbd.hermes.model.osmimport.job.Job;
import es.udc.lbd.hermes.model.osmimport.job.JobDTO;
import es.udc.lbd.hermes.model.osmimport.job.dao.JobDao;
import es.udc.lbd.hermes.model.osmimport.message.Message;
import es.udc.lbd.hermes.model.osmimport.message.dao.MessageDao;

@Service("jobService")
@Transactional
public class JobServiceImpl implements JobService{

	@Autowired
	private JobDao jobDao;
	
	@Autowired
	private ExecutionDao executionDao;
	
	@Autowired
	private MessageDao messageDao;
	
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
	
	public Execution createExecution(Long idJob){
		//Recuperamos el job
		Job job = jobDao.get(idJob);
		
		//Creamos el objeto execution
		Calendar timestamp = Calendar.getInstance();
		Execution execution =  new Execution(ExecutionStatus.RUNNING, timestamp, job);
		
		executionDao.create(execution);
		
		//Creamos el mensaje inicial asociado a la ejecucion
		Message message = new Message("Initiating execution of the job", timestamp, execution);
		messageDao.create(message);
		
		return execution;
	}
}
