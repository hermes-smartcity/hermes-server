package es.udc.lbd.hermes.model.util.exceptions;

@SuppressWarnings("serial")
public class TablaExisteException extends Exception {

	private String esquema;
	private String nombre;

	public TablaExisteException(String esquema, String nombre) {
		this.esquema = esquema;
		this.nombre = nombre;
	}

	public String getEsquema() {
		return esquema;
	}

	public String getNombre() {
		return nombre;
	}
	

}
