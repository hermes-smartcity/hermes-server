package es.udc.lbd.hermes.model.gps;

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
@SequenceGenerator(name = "xeradorId", sequenceName = "gps_id_seq")
@Table(name = "gps")
@SuppressWarnings("serial")
public class Gps implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
	private Long id;
	
	 @ManyToOne(fetch = FetchType.EAGER)
 	@JoinColumn(name = "idUsuarioMovil")
 	private UsuarioMovil usuarioMovil;
	 
	private String provider;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar time;
	
	@Type(type="org.hibernate.spatial.GeometryType")
	@JsonSerialize(using = CustomGeometrySerializer.class)
    @JsonDeserialize(using = CustomPointDeserializer.class)	
    private Point position;
	
	private Double altitude;
	
	private Double speed;
	
	private Double bearing;
	
	private Double accuracy;
	
	public Gps(){}

	public Gps(Long id, UsuarioMovil usuarioMovil, String provider,
			Calendar time, Point position, Double altitude, Double speed,
			Double bearing, Double accuracy) {
		super();
		this.id = id;
		this.usuarioMovil = usuarioMovil;
		this.provider = provider;
		this.time = time;
		this.position = position;
		this.altitude = altitude;
		this.speed = speed;
		this.bearing = bearing;
		this.accuracy = accuracy;
	}
	
	public Gps(String provider,
			Calendar time, Point position, Double altitude, Double speed,
			Double bearing, Double accuracy) {
		super();
		this.provider = provider;
		this.time = time;
		this.position = position;
		this.altitude = altitude;
		this.speed = speed;
		this.bearing = bearing;
		this.accuracy = accuracy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UsuarioMovil getUsuarioMovil() {
		return usuarioMovil;
	}

	public void setUsuarioMovil(UsuarioMovil usuarioMovil) {
		this.usuarioMovil = usuarioMovil;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public Calendar getTime() {
		return time;
	}

	public void setTime(Calendar time) {
		this.time = time;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public Double getAltitude() {
		return altitude;
	}

	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public Double getBearing() {
		return bearing;
	}

	public void setBearing(Double bearing) {
		this.bearing = bearing;
	}

	public Double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Double accuracy) {
		this.accuracy = accuracy;
	}
	
	
}
