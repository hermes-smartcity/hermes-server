package es.udc.lbd.hermes.model.osmimport.job.service;

import java.util.List;

import es.udc.lbd.hermes.model.osmimport.job.Job;
import es.udc.lbd.hermes.model.osmimport.job.JobDTO;

public interface JobService {

	public List<Job> getAll();
	
	public void delete(Long id); 

	public void register(JobDTO jobDto);
	
	public Job get(Long id);
	
	public void update(JobDTO jobDto, Long id);
}
