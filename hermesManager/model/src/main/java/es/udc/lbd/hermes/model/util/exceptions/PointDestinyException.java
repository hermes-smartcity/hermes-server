package es.udc.lbd.hermes.model.util.exceptions;

@SuppressWarnings("serial")
public class PointDestinyException extends Exception {

	private Double toLat;
	private Double toLng;

	public PointDestinyException(Double toLat, Double toLng) {
		this.toLat = toLat;
		this.toLng = toLng;
	}

	public Double getToLat() {
		return toLat;
	}

	public Double getToLng() {
		return toLng;
	}

}
