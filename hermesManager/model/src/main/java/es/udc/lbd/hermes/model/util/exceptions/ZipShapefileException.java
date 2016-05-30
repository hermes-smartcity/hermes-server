package es.udc.lbd.hermes.model.util.exceptions;

@SuppressWarnings("serial")
public class ZipShapefileException extends Exception {

	private String nombre;

	public ZipShapefileException(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}
	

}
