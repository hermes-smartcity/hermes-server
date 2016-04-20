package es.udc.lbd.hermes.model.sensordata;

import java.io.Serializable;
import java.math.BigDecimal;
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

import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "sensordata_id_seq")
@Table(name = "sensordata")
@SuppressWarnings("serial")
public class SensorData implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
	private Long id;
	
	 @ManyToOne(fetch = FetchType.EAGER)
 	@JoinColumn(name = "idUsuarioMovil")
 	private UsuarioMovil usuarioMovil;
	
	private String typesensor;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar startime;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar enditme;
	
	@Type(type = "es.udc.lbd.hermes.model.util.hibernate.NumericArrayUserType")
	private BigDecimal[] values;
	
	public SensorData(){}

	public SensorData(Long id, UsuarioMovil usuarioMovil, String typesensor,
			Calendar startime, Calendar enditme, BigDecimal[] values) {
		super();
		this.id = id;
		this.usuarioMovil = usuarioMovil;
		this.typesensor = typesensor;
		this.startime = startime;
		this.enditme = enditme;
		this.values = values;
	}
	
	public SensorData(String typesensor, Calendar startime, Calendar enditme, BigDecimal[] values) {
		super();
		this.typesensor = typesensor;
		this.startime = startime;
		this.enditme = enditme;
		this.values = values;
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

	public String getTypesensor() {
		return typesensor;
	}

	public void setTypesensor(String typesensor) {
		this.typesensor = typesensor;
	}

	public Calendar getStartime() {
		return startime;
	}

	public void setStartime(Calendar startime) {
		this.startime = startime;
	}

	public Calendar getEnditme() {
		return enditme;
	}

	public void setEnditme(Calendar enditme) {
		this.enditme = enditme;
	}

	public BigDecimal[] getValues() {
		return values;
	}

	public void setValues(BigDecimal[] values) {
		this.values = values;
	}

	
}
