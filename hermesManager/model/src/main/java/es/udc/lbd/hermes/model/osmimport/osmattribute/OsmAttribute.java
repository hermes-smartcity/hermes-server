package es.udc.lbd.hermes.model.osmimport.osmattribute;

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

import es.udc.lbd.hermes.model.osmimport.osmconcept.OsmConcept;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "osmattribute_id_seq")
@Table(schema="\"osmimport\"", name = "osmattribute")
@SuppressWarnings("serial")
public class OsmAttribute implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
	private Long id;
	
	private String name;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idOsmConcept")
	private OsmConcept osmConcept;
	
	public OsmAttribute(){}

	public OsmAttribute(Long id, String name, OsmConcept osmConcept) {
		super();
		this.id = id;
		this.name = name;
		this.osmConcept = osmConcept;
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

	public OsmConcept getOsmConcept() {
		return osmConcept;
	}

	public void setOsmConcept(OsmConcept osmConcept) {
		this.osmConcept = osmConcept;
	}
	
}
