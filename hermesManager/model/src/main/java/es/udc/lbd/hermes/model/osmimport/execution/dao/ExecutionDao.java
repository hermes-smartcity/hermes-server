package es.udc.lbd.hermes.model.osmimport.execution.dao;

import java.util.List;

import es.udc.lbd.hermes.model.osmimport.execution.Execution;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface ExecutionDao extends GenericDao<Execution, Long>{

	public List<Execution> getAll();
}
