package es.udc.lbd.hermes.model.events.usercaloriesexpended;

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
@SequenceGenerator(name = "xeradorId", sequenceName = "usercaloriesexpended_id_seq")
@Table(name = "usercaloriesexpended")
@SuppressWarnings("serial")
public class UserCaloriesExpended implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
	private Long id;
	private String eventId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar endTime;
	
	private Float calories;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar startTime;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idUsuarioMovil")
	private UsuarioMovil usuarioMovil;
	
	public UserCaloriesExpended(){}

	public UserCaloriesExpended(Long id, String eventId, Calendar endTime,
			Float calories, Calendar startTime, UsuarioMovil usuarioMovil) {
		super();
		this.id = id;
		this.eventId = eventId;
		this.endTime = endTime;
		this.calories = calories;
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

	public Float getCalories() {
		return calories;
	}

	public void setCalories(Float calories) {
		this.calories = calories;
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
