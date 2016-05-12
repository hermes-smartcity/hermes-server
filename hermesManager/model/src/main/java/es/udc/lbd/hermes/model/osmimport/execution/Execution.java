package es.udc.lbd.hermes.model.osmimport.execution;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import es.udc.lbd.hermes.model.osmimport.job.Job;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "osmimport.execution_id_seq")
@Table(schema="\"osmimport\"", name = "execution")
@SuppressWarnings("serial")
public class Execution implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private ExecutionStatus status;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar timestamp;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idJob")
	private Job job;
	
	public Execution(){}

	public Execution(Long id, ExecutionStatus status, Calendar timestamp,
			Job job) {
		super();
		this.id = id;
		this.status = status;
		this.timestamp = timestamp;
		this.job = job;
	}
	
	public Execution(ExecutionStatus status, Calendar timestamp,
			Job job) {
		super();
		this.status = status;
		this.timestamp = timestamp;
		this.job = job;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ExecutionStatus getStatus() {
		return status;
	}

	public void setStatus(ExecutionStatus status) {
		this.status = status;
	}

	public Calendar getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}
	
	
}
