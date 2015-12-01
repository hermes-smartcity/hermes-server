package es.udc.lbd.hermes.model.events;

import java.io.Serializable;

public class EventosPorDia implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer dia;
	private Integer mes;
	private Integer anio;
	
	private Long numeroEventos;
	
	public EventosPorDia(){}
	public EventosPorDia(Integer dia, Integer mes, Integer anio, Long numeroEventos) {
		super();
		this.dia = dia;
		this.mes = mes;
		this.anio = anio;
		this.numeroEventos = numeroEventos;
	}

	public Integer getDia() {
		return dia;
	}
	
	public void setDia(Integer dia) {
		this.dia = dia;
	}
	
	public Integer getMes() {
		return mes;
	}
	
	public void setMes(Integer mes) {
		this.mes = mes;
	}
	
	public Integer getAnio() {
		return anio;
	}
	
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	
	public Long getNumeroEventos() {
		return numeroEventos;
	}
	
	public void setNumeroEventos(Long numeroEventos) {
		this.numeroEventos = numeroEventos;
	}
	
	public String getFecha(){
		if (mes==null)
			return null;
		String diaString=String.valueOf(dia);
		String mesString=String.valueOf(mes);
		if (mes<10)
			mesString="0"+mesString;
		return diaString+"/"+mesString+"/"+anio;
	}
}
