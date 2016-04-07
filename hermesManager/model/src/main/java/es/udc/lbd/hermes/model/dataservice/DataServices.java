package es.udc.lbd.hermes.model.dataservice;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "dataservices_id_seq")
@Table(name = "dataservices")
@SuppressWarnings("serial")
public class DataServices implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
	private Long id;
	
	private String service;
	private String method;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar timelog;
	
	public DataServices(){}

	public DataServices(Long id, String service, String method, Calendar timelog) {
		super();
		this.id = id;
		this.service = service;
		this.method = method;
		this.timelog = timelog;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Calendar getTimelog() {
		return timelog;
	}

	public void setTimelog(Calendar timelog) {
		this.timelog = timelog;
	}
	
	
}
