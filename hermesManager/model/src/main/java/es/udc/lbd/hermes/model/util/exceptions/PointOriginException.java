package es.udc.lbd.hermes.model.util.exceptions;

@SuppressWarnings("serial")
public class PointOriginException extends Exception {

	private Double fromLat;
	private Double fromLng;

	public PointOriginException(Double fromLat, Double fromLng) {
		this.fromLat = fromLat;
		this.fromLng = fromLng;
	}

	public Double getFromLat() {
		return fromLat;
	}

	public Double getFromLng() {
		return fromLng;
	}

}
