package es.udc.lbd.hermes.model.smartdriver;

public class OriginDestinyPoint {

	private Integer source;
	private Double x1;
	private Double y1;
	
	public OriginDestinyPoint(){}

	public OriginDestinyPoint(Integer source, Double x1, Double y1) {
		super();
		this.source = source;
		this.x1 = x1;
		this.y1 = y1;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Double getX1() {
		return x1;
	}

	public void setX1(Double x1) {
		this.x1 = x1;
	}

	public Double getY1() {
		return y1;
	}

	public void setY1(Double y1) {
		this.y1 = y1;
	}
	
	
}
