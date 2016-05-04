package es.udc.lbd.hermes.model.osmimport.osmfilter;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@SequenceGenerator(name = "xeradorId", sequenceName = "osmfilter_id_seq")
@Table(schema="\"osmimport\"", name = "osmfilter")
@SuppressWarnings("serial")
public class OsmFilter implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
	private Long id;
	
	private String name;
	
	@Enumerated(EnumType.STRING)
    private OsmFilterOperation operation;
	
	private String value;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idOsmConcept")
	private OsmConcept osmConcept;
	
	public OsmFilter(){}

	public OsmFilter(Long id, String name, OsmFilterOperation operation,
			String value, OsmConcept osmConcept) {
		super();
		this.id = id;
		this.name = name;
		this.operation = operation;
		this.value = value;
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

	public OsmFilterOperation getOperation() {
		return operation;
	}

	public void setOperation(OsmFilterOperation operation) {
		this.operation = operation;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public OsmConcept getOsmConcept() {
		return osmConcept;
	}

	public void setOsmConcept(OsmConcept osmConcept) {
		this.osmConcept = osmConcept;
	}

	
	
}
