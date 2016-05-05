package es.udc.lbd.hermes.model.sensordata;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;

public class SensorDataDTO {
	private BigInteger id;
 	private BigInteger idUsuarioMovil;
	private String typesensor;
	private Calendar startime;
	private Calendar endtime;
	private BigDecimal[] values;
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public BigInteger getIdUsuarioMovil() {
		return idUsuarioMovil;
	}
	public void setIdUsuarioMovil(BigInteger idUsuarioMovil) {
		this.idUsuarioMovil = idUsuarioMovil;
	}
	public String getTypesensor() {
		return typesensor;
	}
	public void setTypesensor(String typesensor) {
		this.typesensor = typesensor;
	}
	public Calendar getStartime() {
		return startime;
	}
	public void setStartime(Calendar startime) {
		this.startime = startime;
	}
	public Calendar getEndtime() {
		return endtime;
	}
	public void setEndtime(Calendar endtime) {
		this.endtime = endtime;
	}
	public BigDecimal[] getValues() {
		return values;
	}
	public void setValues(BigDecimal[] values) {
		this.values = values;
	}
}
