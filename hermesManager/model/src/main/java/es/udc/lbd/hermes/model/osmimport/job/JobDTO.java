package es.udc.lbd.hermes.model.osmimport.job;

public class JobDTO {

	private Long id;
	private String name;
	private Double seLng;
	private Double seLat;
	private Double nwLng;
	private Double nwLat;
	
	public JobDTO(){}

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

	public Double getSeLng() {
		return seLng;
	}

	public void setSeLng(Double seLng) {
		this.seLng = seLng;
	}

	public Double getSeLat() {
		return seLat;
	}

	public void setSeLat(Double seLat) {
		this.seLat = seLat;
	}

	public Double getNwLng() {
		return nwLng;
	}

	public void setNwLng(Double nwLng) {
		this.nwLng = nwLng;
	}

	public Double getNwLat() {
		return nwLat;
	}

	public void setNwLat(Double nwLat) {
		this.nwLat = nwLat;
	}
	
	
}
