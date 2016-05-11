package es.udc.lbd.hermes.model.osmimport.message;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import es.udc.lbd.hermes.model.osmimport.execution.Execution;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "osmimport.message_id_seq")
@Table(schema="\"osmimport\"", name = "message")
@SuppressWarnings("serial")
public class Message implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
	private Long id;
		
	private String text;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar timestamp;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idExecution")
	private Execution execution;
	
	public Message(){}

	public Message(Long id, String text, Calendar timestamp, Execution execution) {
		super();
		this.id = id;
		this.text = text;
		this.timestamp = timestamp;
		this.execution = execution;
	}
	
	public Message(String text, Calendar timestamp, Execution execution) {
		super();
		this.text = text;
		this.timestamp = timestamp;
		this.execution = execution;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Calendar getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}

	public Execution getExecution() {
		return execution;
	}

	public void setExecution(Execution execution) {
		this.execution = execution;
	}
	
}
