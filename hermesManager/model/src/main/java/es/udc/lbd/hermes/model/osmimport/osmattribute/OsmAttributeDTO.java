package es.udc.lbd.hermes.model.osmimport.osmattribute;

public class OsmAttributeDTO {

	private Long id;
	private String name;
	private Long osmConcept;
	
	public OsmAttributeDTO(){}

	public OsmAttributeDTO(Long id, String name, Long osmConcept) {
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

	public Long getOsmConcept() {
		return osmConcept;
	}

	public void setOsmConcept(Long osmConcept) {
		this.osmConcept = osmConcept;
	}
	
	
}
