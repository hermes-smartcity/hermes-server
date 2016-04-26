package es.udc.lbd.hermes.model.gps;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GpssJson {

	@JsonProperty("userID")
	private String userid;
	
	@JsonProperty("provider")
	private String provider;
	
	@JsonProperty("gps")
	private List<GpsJson> gps;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public List<GpsJson> getGps() {
		return gps;
	}

	public void setGps(List<GpsJson> gps) {
		this.gps = gps;
	}

	
	
}