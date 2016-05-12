package es.udc.lbd.hermes.model.osmimport.execution.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.osmimport.execution.Execution;
import es.udc.lbd.hermes.model.osmimport.execution.dao.ExecutionDao;

@Service("executionService")
@Transactional
public class ExecutionServiceImpl implements ExecutionService {

	@Autowired
	private ExecutionDao executionDao;
	
	@Transactional(readOnly = true)
	public List<Execution> getAll(){
		return executionDao.getAll();
	}
	
	public void delete(Long id){
		executionDao.delete(id);
	}
	
	public Execution register(Execution execution){
		
		executionDao.create(execution);
		
		return execution;
	}
	
	@Transactional(readOnly = true)
	public Execution get(Long id){
		return executionDao.get(id);
	}

	public Execution update(Execution execution, Long id){
		
		executionDao.update(execution);
		
		return execution;
	}
}
