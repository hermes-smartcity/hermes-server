package es.udc.lbd.hermes.model.osmimport.job;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Polygon;

import es.udc.lbd.hermes.model.util.jackson.CustomGeometrySerializer;
import es.udc.lbd.hermes.model.util.jackson.CustomMultiPolygonDeserializer;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "osmimport.job_id_seq")
@Table(schema="\"osmimport\"", name = "job")
@SuppressWarnings("serial")
public class Job implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
	private Long id;
	
	private String name;
	
	@Type(type="org.hibernate.spatial.GeometryType")
	@JsonSerialize(using = CustomGeometrySerializer.class)
    @JsonDeserialize(using = CustomMultiPolygonDeserializer.class)		
    private Polygon bbox;
	
	public Job(){}

	public Job(Long id, String name, Polygon bbox) {
		super();
		this.id = id;
		this.name = name;
		this.bbox = bbox;
	}
	
	public Job(String name, Polygon bbox) {
		super();
		this.name = name;
		this.bbox = bbox;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Polygon getBbox() {
		return bbox;
	}

	public void setBbox(Polygon bbox) {
		this.bbox = bbox;
	}
	
	
}
