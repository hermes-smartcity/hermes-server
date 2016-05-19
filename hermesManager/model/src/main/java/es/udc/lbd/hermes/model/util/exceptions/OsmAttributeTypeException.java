package es.udc.lbd.hermes.model.util.exceptions;

@SuppressWarnings("serial")
public class OsmAttributeTypeException extends Exception {

	private String tagName;
	private String typeName;
	
	public OsmAttributeTypeException(String tagName, String typeName) {
		this.tagName = tagName;
		this.typeName = typeName;
	}

	public String getTagName() {
		return tagName;
	}

	public String getTypeName() {
		return typeName;
	}
	

}
