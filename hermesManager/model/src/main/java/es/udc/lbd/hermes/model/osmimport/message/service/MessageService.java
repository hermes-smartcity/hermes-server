package es.udc.lbd.hermes.model.osmimport.message.service;

import java.util.List;

import es.udc.lbd.hermes.model.osmimport.message.Message;

public interface MessageService {

	public List<Message> getAll(Long idExecution);
	
	public void delete(Long id); 
	
	public Message register(Message message);
	
	public Message get(Long id);
	
	public Message update(Message message, Long id);
}
