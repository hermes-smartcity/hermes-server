package es.udc.lbd.hermes.model.osmimport.message.dao;

import java.util.List;

import es.udc.lbd.hermes.model.osmimport.message.Message;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface MessageDao extends GenericDao<Message, Long>{

	public List<Message> getAll(Long idExecution);
	
}
