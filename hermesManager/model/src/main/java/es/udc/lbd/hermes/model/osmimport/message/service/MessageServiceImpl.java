package es.udc.lbd.hermes.model.osmimport.message.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.osmimport.execution.Execution;
import es.udc.lbd.hermes.model.osmimport.execution.dao.ExecutionDao;
import es.udc.lbd.hermes.model.osmimport.message.Message;
import es.udc.lbd.hermes.model.osmimport.message.MessageWithStatus;
import es.udc.lbd.hermes.model.osmimport.message.dao.MessageDao;

@Service("messageService")
@Transactional
public class MessageServiceImpl implements MessageService{

	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private ExecutionDao executionDao;
	
	@Transactional(readOnly = true)
	public List<Message> getAll(Long idExecution){
		return messageDao.getAll(idExecution);
	}
	
	public void delete(Long id){
		messageDao.delete(id);
	}
	
	public Message register(Message message){
			
		messageDao.create(message);
		
		return message;
	}
	
	@Transactional(readOnly = true)
	public Message get(Long id){
		return messageDao.get(id);
	}

	public Message update(Message message, Long id){
				
		messageDao.update(message);
		
		return message;
	}
	
	@Transactional(readOnly = true)
	public MessageWithStatus getAllMessagesWithStatus(Long idExecution){
		Execution execution = executionDao.get(idExecution);
		List<Message> messages = messageDao.getAll(idExecution);
		
		MessageWithStatus mws = null;
		//Por si se ha eliminado la ejecucion mientras se hacia la peticion (para evitar el nulo)
		if (execution!=null){ 
			mws = new MessageWithStatus(execution.getStatus(), messages);
		}
		
		return mws;
	}
}
