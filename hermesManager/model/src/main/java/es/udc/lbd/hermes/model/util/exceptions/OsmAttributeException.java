package es.udc.lbd.hermes.model.util.exceptions;

@SuppressWarnings("serial")
public class OsmAttributeException extends Exception {

	private String tagName;
	
	public OsmAttributeException(String tagName) {
		this.tagName = tagName;
	}

	public String getTagName() {
		return tagName;
	}

}
