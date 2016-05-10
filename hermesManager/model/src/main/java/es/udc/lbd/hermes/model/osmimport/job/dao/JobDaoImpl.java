package es.udc.lbd.hermes.model.osmimport.job.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.osmimport.job.Job;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

@Repository
public class JobDaoImpl extends GenericDaoHibernate<Job, Long> implements JobDao{

	@SuppressWarnings("unchecked")
	public List<Job> getAll(){
		return getSession().createCriteria(this.entityClass).list();
	}
	
	
}
