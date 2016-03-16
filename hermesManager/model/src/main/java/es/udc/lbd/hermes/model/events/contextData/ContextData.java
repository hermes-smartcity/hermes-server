package es.udc.lbd.hermes.model.events.contextData;

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

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Point;

import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.util.jackson.CustomGeometrySerializer;
import es.udc.lbd.hermes.model.util.jackson.CustomPointDeserializer;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "contextdata_id_seq")
@Table(name = "contextdata")
@SuppressWarnings("serial")
public class ContextData implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
	private Long id;
	private String eventId;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar timeLog;
	
	private String detectedActivity;
	
	private Integer accuracy;

	@Type(type="org.hibernate.spatial.GeometryType")
	@JsonSerialize(using = CustomGeometrySerializer.class)
    @JsonDeserialize(using = CustomPointDeserializer.class)	
    private Point position;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idUsuarioMovil")
	private UsuarioMovil usuarioMovil;

	public ContextData() {
    }
	
	public ContextData(Calendar timeLog, String eventId, Point position) {
    	this.timeLog= timeLog;
    	this.eventId= eventId;
    	this.position= position;
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

	public Calendar getTimeLog() {
		return timeLog;
	}

	public void setTimeLog(Calendar timeLog) {
		this.timeLog = timeLog;
	}
		
	public String getDetectedActivity() {
		return detectedActivity;
	}

	public void setDetectedActivity(String detectedActivity) {
		this.detectedActivity = detectedActivity;
	}

	public Integer getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Integer accuracy) {
		this.accuracy = accuracy;
	}

	public UsuarioMovil getUsuarioMovil() {
		return usuarioMovil;
	}

	public void setUsuarioMovil(UsuarioMovil usuarioMovil) {
		this.usuarioMovil = usuarioMovil;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

}