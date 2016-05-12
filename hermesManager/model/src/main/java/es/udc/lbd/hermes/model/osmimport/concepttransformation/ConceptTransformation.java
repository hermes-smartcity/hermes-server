package es.udc.lbd.hermes.model.osmimport.concepttransformation;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Polygon;

import es.udc.lbd.hermes.model.osmimport.dbconcept.DBConcept;
import es.udc.lbd.hermes.model.osmimport.job.Job;
import es.udc.lbd.hermes.model.osmimport.osmconcept.OsmConcept;
import es.udc.lbd.hermes.model.util.jackson.CustomGeometrySerializer;
import es.udc.lbd.hermes.model.util.jackson.CustomMultiPolygonDeserializer;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "osmimport.concepttransformation_id_seq")
@Table(schema="\"osmimport\"", name = "concepttransformation")
@SuppressWarnings("serial")
public class ConceptTransformation implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
	private Long id;
	
	@Type(type="org.hibernate.spatial.GeometryType")
	@JsonSerialize(using = CustomGeometrySerializer.class)
    @JsonDeserialize(using = CustomMultiPolygonDeserializer.class)		
    private Polygon bbox;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idJob")
	private Job job;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idDbConcept")
	private DBConcept dbConcept;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idOsmConcept")
	private OsmConcept osmConcept;
	
	public ConceptTransformation(){}

	public ConceptTransformation(Long id, Polygon bbox, Job job,
			DBConcept dbConcept, OsmConcept osmConcept) {
		super();
		this.id = id;
		this.bbox = bbox;
		this.job = job;
		this.dbConcept = dbConcept;
		this.osmConcept = osmConcept;
	}

	public ConceptTransformation(Polygon bbox, Job job,
			DBConcept dbConcept, OsmConcept osmConcept) {
		super();
		this.bbox = bbox;
		this.job = job;
		this.dbConcept = dbConcept;
		this.osmConcept = osmConcept;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Polygon getBbox() {
		return bbox;
	}

	public void setBbox(Polygon bbox) {
		this.bbox = bbox;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public DBConcept getDbConcept() {
		return dbConcept;
	}

	public void setDbConcept(DBConcept dbConcept) {
		this.dbConcept = dbConcept;
	}

	public OsmConcept getOsmConcept() {
		return osmConcept;
	}

	public void setOsmConcept(OsmConcept osmConcept) {
		this.osmConcept = osmConcept;
	}
	
}
