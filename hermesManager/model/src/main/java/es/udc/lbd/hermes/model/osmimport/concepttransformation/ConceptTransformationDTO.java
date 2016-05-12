package es.udc.lbd.hermes.model.osmimport.concepttransformation;

import es.udc.lbd.hermes.model.osmimport.dbconcept.DBConcept;
import es.udc.lbd.hermes.model.osmimport.osmconcept.OsmConcept;

public class ConceptTransformationDTO {

	private Long id;
	private DBConcept dbConcept;
	private OsmConcept osmConcept;
	private Long job;
	private Double seLng;
	private Double seLat;
	private Double nwLng;
	private Double nwLat;
	
	public ConceptTransformationDTO(){}

	public ConceptTransformationDTO(Long id, DBConcept dbConcept,
			OsmConcept osmConcept, Long job, Double seLng, Double seLat, Double nwLng,
			Double nwLat) {
		super();
		this.id = id;
		this.dbConcept = dbConcept;
		this.osmConcept = osmConcept;
		this.job = job;
		this.seLng = seLng;
		this.seLat = seLat;
		this.nwLng = nwLng;
		this.nwLat = nwLat;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getJob() {
		return job;
	}

	public void setJob(Long job) {
		this.job = job;
	}

	public Double getSeLng() {
		return seLng;
	}

	public void setSeLng(Double seLng) {
		this.seLng = seLng;
	}

	public Double getSeLat() {
		return seLat;
	}

	public void setSeLat(Double seLat) {
		this.seLat = seLat;
	}

	public Double getNwLng() {
		return nwLng;
	}

	public void setNwLng(Double nwLng) {
		this.nwLng = nwLng;
	}

	public Double getNwLat() {
		return nwLat;
	}

	public void setNwLat(Double nwLat) {
		this.nwLat = nwLat;
	}
	
	
}
