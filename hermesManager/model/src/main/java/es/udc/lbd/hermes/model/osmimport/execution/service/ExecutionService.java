package es.udc.lbd.hermes.model.osmimport.execution.service;

import java.util.List;

import es.udc.lbd.hermes.model.osmimport.execution.Execution;

public interface ExecutionService {

	public List<Execution> getAll();
	public void delete(Long id); 
	public Execution register(Execution execution);
	public Execution get(Long id);
	public Execution update(Execution execution, Long id);
}
