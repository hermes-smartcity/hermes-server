package es.udc.lbd.hermes.model.osmimport.message;

import java.util.List;

import es.udc.lbd.hermes.model.osmimport.execution.ExecutionStatus;

public class MessageWithStatus {

	private ExecutionStatus status;
	private List<Message> messages;
	
	public MessageWithStatus(){}

	public MessageWithStatus(ExecutionStatus status, List<Message> messages) {
		super();
		this.status = status;
		this.messages = messages;
	}

	public ExecutionStatus getStatus() {
		return status;
	}

	public void setStatus(ExecutionStatus status) {
		this.status = status;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	
	
}
