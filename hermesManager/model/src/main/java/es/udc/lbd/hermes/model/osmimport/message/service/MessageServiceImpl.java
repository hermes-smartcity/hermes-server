package es.udc.lbd.hermes.model.osmimport.message.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.osmimport.message.Message;
import es.udc.lbd.hermes.model.osmimport.message.dao.MessageDao;

@Service("messageService")
@Transactional
public class MessageServiceImpl implements MessageService{

	@Autowired
	private MessageDao messageDao;
	
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
}
