package es.udc.lbd.hermes.model.osmimport.job.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Overpass {

	@JsonProperty("version")
	private Double version;
	
	@JsonProperty("generator")
	private String generator;
	
	@JsonProperty("osm3s")
	private Osm3s osm3s;
	
	@JsonProperty("elements")
	private List<Element> elements;

	public Double getVersion() {
		return version;
	}

	public void setVersion(Double version) {
		this.version = version;
	}

	public String getGenerator() {
		return generator;
	}

	public void setGenerator(String generator) {
		this.generator = generator;
	}

	public Osm3s getOsm3s() {
		return osm3s;
	}

	public void setOsm3s(Osm3s osm3s) {
		this.osm3s = osm3s;
	}

	public List<Element> getElements() {
		return elements;
	}

	public void setElements(List<Element> elements) {
		this.elements = elements;
	}
	
	
}
