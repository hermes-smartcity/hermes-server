package es.udc.lbd.hermes.model.util.exceptions;

@SuppressWarnings("serial")
public class OsmAttributeDateException extends Exception {

	private String tagName;
	
	public OsmAttributeDateException(String tagName) {
		this.tagName = tagName;
	}

	public String getTagName() {
		return tagName;
	}

}
