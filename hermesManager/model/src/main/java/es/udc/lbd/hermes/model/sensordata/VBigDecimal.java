package es.udc.lbd.hermes.model.sensordata;

import java.math.BigDecimal;

public class VBigDecimal extends V {
	// TODO Cris Lo lógico es que haya herencia, que sea Date o Double, como de momento solo se recupera en el lado cliente 
	private BigDecimal v;
	
	public VBigDecimal(){}

	public VBigDecimal(BigDecimal value) {		
		super();
		this.v = value;
	}

	public BigDecimal getV() {
		return v;
	}

	public void setV(BigDecimal v) {
		this.v = v;
	}

}
