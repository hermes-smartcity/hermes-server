package es.udc.lbd.hermes.model.osmimport.job.json;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Osm3s {

	@JsonProperty("timestamp_osm_base")
	private Calendar timestamp_osm_base;
	
	@JsonProperty("copyright")
	private String copyright;

	public Calendar getTimestamp_osm_base() {
		return timestamp_osm_base;
	}

	public void setTimestamp_osm_base(Calendar timestamp_osm_base) {
		this.timestamp_osm_base = timestamp_osm_base;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}
	
	
}
