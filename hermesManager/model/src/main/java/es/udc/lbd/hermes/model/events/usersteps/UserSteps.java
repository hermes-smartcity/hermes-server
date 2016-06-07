package es.udc.lbd.hermes.model.events.usersteps;

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

import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "usersteps_id_seq")
@Table(name = "usersteps")
@SuppressWarnings("serial")
public class UserSteps implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
	private Long id;
	private String eventId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar endTime;
	
	private Integer steps;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar startTime;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idUsuarioMovil")
	private UsuarioMovil usuarioMovil;
	
	public UserSteps(){}

	public UserSteps(Long id, String eventId, Calendar endTime,
			Integer steps, Calendar startTime, UsuarioMovil usuarioMovil) {
		super();
		this.id = id;
		this.eventId = eventId;
		this.endTime = endTime;
		this.steps = steps;
		this.startTime = startTime;
		this.usuarioMovil = usuarioMovil;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public Calendar getEndTime() {
		return endTime;
	}

	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}

	public Integer getSteps() {
		return steps;
	}

	public void setSteps(Integer steps) {
		this.steps = steps;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}

	public UsuarioMovil getUsuarioMovil() {
		return usuarioMovil;
	}

	public void setUsuarioMovil(UsuarioMovil usuarioMovil) {
		this.usuarioMovil = usuarioMovil;
	}
	
	
}
