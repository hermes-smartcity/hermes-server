package es.udc.lbd.hermes.model.sensordata;

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

import org.hibernate.annotations.Type;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "sensordata_id_seq")
@Table(name = "sensordata")
@SuppressWarnings("serial")
public class SensorData implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
	private Long id;
	
	private String userid;
	
	private String typesensor;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar startime;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar enditme;
	
	@Type(type = "es.udc.lbd.hermes.model.util.hibernate.NumericArrayUserType")
	private Double[] values;
	
	public SensorData(){}

	public SensorData(Long id, String userid, String typesensor,
			Calendar startime, Calendar enditme, Double[] values) {
		super();
		this.id = id;
		this.userid = userid;
		this.typesensor = typesensor;
		this.startime = startime;
		this.enditme = enditme;
		this.values = values;
	}
	
	public SensorData(String userid, String typesensor, Calendar startime, Calendar enditme, Double[] values) {
		super();
		this.userid = userid;
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

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
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

	public Double[] getValues() {
		return values;
	}

	public void setValues(Double[] values) {
		this.values = values;
	}

	
}
