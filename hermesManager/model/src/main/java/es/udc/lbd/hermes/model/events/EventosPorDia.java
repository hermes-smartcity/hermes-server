package es.udc.lbd.hermes.model.events;

import java.io.Serializable;
import java.math.BigInteger;

public class EventosPorDia implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Double dia;
	private Double mes;
	private Double anio;
	
	private BigInteger neventos;
	
	public EventosPorDia(){}
	public EventosPorDia(Double dia, Double mes, Double anio, BigInteger neventos) {
		super();
		this.dia = dia;
		this.mes = mes;
		this.anio = anio;
		this.neventos = neventos;
	}

	public Double getDia() {
		return dia;
	}
	
	public void setDia(Double dia) {
		this.dia = dia;
	}
	
	public Double getMes() {
		return mes;
	}
	
	public void setMes(Double mes) {
		this.mes = mes;
	}
	
	public Double getAnio() {
		return anio;
	}
	
	public void setAnio(Double anio) {
		this.anio = anio;
	}
	
	public BigInteger getNeventos() {
		return neventos;
	}
	
	public void setNeventos(BigInteger neventos) {
		this.neventos = neventos;
	}

	public String getFecha(){
		if (mes==null)
			return null;
		String diaString=String.valueOf(dia.intValue());
		String mesString=String.valueOf(mes.intValue());
		if (mes<10)
			mesString="0"+mesString;
		return diaString+"/"+mesString+"/"+anio.intValue();
	}
}
