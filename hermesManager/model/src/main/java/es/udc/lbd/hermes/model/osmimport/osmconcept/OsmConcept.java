package es.udc.lbd.hermes.model.osmimport.osmconcept;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@SequenceGenerator(name = "xeradorId", sequenceName = "osmimport.osmconcept_id_seq")
@Table(schema="\"osmimport\"", name = "osmconcept")
@SuppressWarnings("serial")
public class OsmConcept implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "xeradorId")
	private Long id;
	
	private String name;
	
	public OsmConcept(){}

	public OsmConcept(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
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
	
	
}
