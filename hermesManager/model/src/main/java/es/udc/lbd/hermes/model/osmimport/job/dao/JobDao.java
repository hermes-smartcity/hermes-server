package es.udc.lbd.hermes.model.osmimport.job.dao;

import java.util.List;

import es.udc.lbd.hermes.model.osmimport.job.Job;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface JobDao extends GenericDao<Job, Long>{

	public List<Job> getAll();
	
}
