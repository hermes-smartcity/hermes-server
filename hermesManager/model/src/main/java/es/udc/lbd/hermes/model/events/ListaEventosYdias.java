package es.udc.lbd.hermes.model.events;

import java.io.Serializable;
import java.util.List;

public class ListaEventosYdias implements Serializable{

	private static final long serialVersionUID = 1L;
	
	List<String> fechas;
	List<Long> nEventos;

	
	public ListaEventosYdias(){}
	public ListaEventosYdias(List<String> fechas, List<Long> nEventos) {
		super();
		this.fechas = fechas;
		this.nEventos =nEventos;
	}
	
	public List<String> getFechas() {
		return fechas;
	}
	
	public void setFechas(List<String> fechas) {
		this.fechas = fechas;
	}
	
	public List<Long> getnEventos() {
		return nEventos;
	}
	
	public void setnEventos(List<Long> nEventos) {
		this.nEventos = nEventos;
	}
}
