package es.udc.lbd.hermes.model.osmimport.attributemapping;

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

import es.udc.lbd.hermes.model.osmimport.concepttransformation.ConceptTransformation;
import es.udc.lbd.hermes.model.osmimport.dbattribute.DBAttribute;
import es.udc.lbd.hermes.model.osmimport.osmattribute.OsmAttribute;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "attributemapping_id_seq")
@Table(schema="\"osmimport\"", name = "attributemapping")
@SuppressWarnings("serial")
public class AttributeMapping implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idConceptTransformation")
	private ConceptTransformation conceptTransformation;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idDbAttribute")
	private DBAttribute dbAttribute;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idOsmAttribute")
	private OsmAttribute osmAttribute;
	
	public AttributeMapping(){}

	public AttributeMapping(Long id,
			ConceptTransformation conceptTransformation,
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

	public ConceptTransformation getConceptTransformation() {
		return conceptTransformation;
	}

	public void setConceptTransformation(ConceptTransformation conceptTransformation) {
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
