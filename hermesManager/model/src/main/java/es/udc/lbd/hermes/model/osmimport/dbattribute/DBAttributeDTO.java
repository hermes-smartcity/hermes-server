package es.udc.lbd.hermes.model.osmimport.dbattribute;

public class DBAttributeDTO {

	private Long id;
	private String attributeName;
	private DBAttributeType attributeType;
	private Long dbConcept;
	
	public DBAttributeDTO(){}

	public DBAttributeDTO(Long id, String attributeName,
			DBAttributeType attributeType, Long dbConcept) {
		super();
		this.id = id;
		this.attributeName = attributeName;
		this.attributeType = attributeType;
		this.dbConcept = dbConcept;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public DBAttributeType getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(DBAttributeType attributeType) {
		this.attributeType = attributeType;
	}

	public Long getDbConcept() {
		return dbConcept;
	}

	public void setDbConcept(Long dbConcept) {
		this.dbConcept = dbConcept;
	}
	
	
	
}
