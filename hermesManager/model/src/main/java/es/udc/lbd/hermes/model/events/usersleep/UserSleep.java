package es.udc.lbd.hermes.model.events.usersleep;

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
@SequenceGenerator(name = "xeradorId", sequenceName = "usersleep_id_seq")
@Table(name = "usersleep")
@SuppressWarnings("serial")
public class UserSleep implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
	private Long id;
	private String eventId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar endTime;
	
	private String name;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar startTime;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idUsuarioMovil")
	private UsuarioMovil usuarioMovil;
	
	public UserSleep(){}

	public UserSleep(Long id, String eventId, Calendar endTime,
			String name, Calendar startTime, UsuarioMovil usuarioMovil) {
		super();
		this.id = id;
		this.eventId = eventId;
		this.endTime = endTime;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
