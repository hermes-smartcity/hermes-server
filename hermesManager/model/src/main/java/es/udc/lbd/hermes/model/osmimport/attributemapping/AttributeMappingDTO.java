package es.udc.lbd.hermes.model.osmimport.attributemapping;

import es.udc.lbd.hermes.model.osmimport.dbattribute.DBAttribute;
import es.udc.lbd.hermes.model.osmimport.osmattribute.OsmAttribute;

public class AttributeMappingDTO {

	private Long id;
	private Long conceptTransformation;
	private DBAttribute dbAttribute;
	private OsmAttribute osmAttribute;
	
	public AttributeMappingDTO(){}

	public AttributeMappingDTO(Long id, Long conceptTransformation,
			DBAttribute dbAttribute, OsmAttribute osmAttribute) {
		super();
		this.id = id;
		this.conceptTransformation = conceptTransformation;
		this.dbAttribute = dbAttribute;
		this.osmAttribute = osmAttribute;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getConceptTransformation() {
		return conceptTransformation;
	}

	public void setConceptTransformation(Long conceptTransformation) {
		this.conceptTransformation = conceptTransformation;
	}

	public DBAttribute getDbAttribute() {
		return dbAttribute;
	}

	public void setDbAttribute(DBAttribute dbAttribute) {
		this.dbAttribute = dbAttribute;
	}

	public OsmAttribute getOsmAttribute() {
		return osmAttribute;
	}

	public void setOsmAttribute(OsmAttribute osmAttribute) {
		this.osmAttribute = osmAttribute;
	}
	
	
}
