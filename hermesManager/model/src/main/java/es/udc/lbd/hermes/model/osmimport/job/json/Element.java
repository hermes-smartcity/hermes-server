package es.udc.lbd.hermes.model.osmimport.job.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Element {

	@JsonProperty("type")
	private String type;
	
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("lat")
	private Double lat;
	
	@JsonProperty("lon")
	private Double lon;
	
	@JsonProperty("tags")
	private Tags tags;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public Tags getTags() {
		return tags;
	}

	public void setTags(Tags tags) {
		this.tags = tags;
	}
	
	
}
