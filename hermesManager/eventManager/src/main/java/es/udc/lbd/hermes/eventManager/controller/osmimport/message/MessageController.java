package es.udc.lbd.hermes.eventManager.controller.osmimport.message;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.eventManager.web.rest.MainResource;
import es.udc.lbd.hermes.model.osmimport.message.Message;
import es.udc.lbd.hermes.model.osmimport.message.MessageWithStatus;
import es.udc.lbd.hermes.model.osmimport.message.service.MessageService;

@RestController
@RequestMapping(value = "/api/message")
public class MessageController extends MainResource{

	static Logger logger = Logger.getLogger(MessageController.class);
	
	@Autowired private MessageService messageServicio;
	
	@RequestMapping(value="/json/messages", method = RequestMethod.GET)
	public List<Message> getMessages(@RequestParam(value = "idExecution", required = true) Long idExecution) { 

		return messageServicio.getAll(idExecution);

	}
	
	@RequestMapping(value="/json/messagesstatus", method = RequestMethod.GET)
	public MessageWithStatus getMessagesWithStatus(@RequestParam(value = "idExecution", required = true) Long idExecution) { 

		return messageServicio.getAllMessagesWithStatus(idExecution);

	}
}
