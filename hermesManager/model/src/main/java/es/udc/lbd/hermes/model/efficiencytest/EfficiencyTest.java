package es.udc.lbd.hermes.model.efficiencytest;

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
@SequenceGenerator(name = "idgenerator", sequenceName = "efficiencytest_id_seq")
@Table(name = "efficiencytest")
@SuppressWarnings("serial")
public class EfficiencyTest implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "idgenerator")
	private Long id;
	private String eventType;
	private Long eventSize;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar time;
	private Long parseTime;
	private Long totalTime;
	private Boolean result;
	
	public EfficiencyTest() {
		super();
	}

	public EfficiencyTest(String eventType, Long eventSize, Calendar time, Long parseTime, Long totalTime, Boolean result) {
		super();
		this.id = null;
		this.eventType = eventType;
		this.eventSize = eventSize;
		this.time = time;
		this.parseTime = parseTime;
		this.totalTime = totalTime;
		this.result = result;
	}
	
	public EfficiencyTest(Long id, String eventType, Long eventSize, Calendar time, Long parseTime, Long totalTime, Boolean result) {
		super();
		this.id = id;
		this.eventType = eventType;
		this.eventSize = eventSize;
		this.time = time;
		this.parseTime = parseTime;
		this.totalTime = totalTime;
		this.result = result;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Calendar getTime() {
		return time;
	}
	public void setTime(Calendar time) {
		this.time = time;
	}
	public Long getParseTime() {
		return parseTime;
	}
	public void setParseTime(Long parseTime) {
		this.parseTime = parseTime;
	}
	public Long getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(Long totalTime) {
		this.totalTime = totalTime;
	}
	public Boolean getResult() {
		return result;
	}
	public void setResult(Boolean result) {
		this.result = result;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public Long getEventSize() {
		return eventSize;
	}
	public void setEventSize(Long eventSize) {
		this.eventSize = eventSize;
	}	
}
