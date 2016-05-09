package es.udc.lbd.hermes.model.osmimport.osmfilter;

public class OsmFilterDTO {

	private Long id;
	private String name;
	private OsmFilterOperation operation;
	private String value;
	private Long osmConcept;
	
	public OsmFilterDTO(){}

	public OsmFilterDTO(Long id, String name, OsmFilterOperation operation,
			String value, Long osmConcept) {
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

	public Long getOsmConcept() {
		return osmConcept;
	}

	public void setOsmConcept(Long osmConcept) {
		this.osmConcept = osmConcept;
	}
	
	
}
