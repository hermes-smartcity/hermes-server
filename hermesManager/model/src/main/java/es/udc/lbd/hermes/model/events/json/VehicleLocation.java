package es.udc.lbd.hermes.model.events.json;

public class VehicleLocation extends EventData {

	private Double latitude;
	private Double longitude;

	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
}
