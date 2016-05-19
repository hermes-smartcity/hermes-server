package es.udc.lbd.hermes.model.util.exceptions;

@SuppressWarnings("serial")
public class OsmAttributeBooleanException extends Exception {

	private String tagName;
	
	public OsmAttributeBooleanException(String tagName) {
		this.tagName = tagName;
	}

	public String getTagName() {
		return tagName;
	}

}
