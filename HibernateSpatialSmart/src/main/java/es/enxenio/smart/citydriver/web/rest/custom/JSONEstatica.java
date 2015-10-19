package es.enxenio.smart.citydriver.web.rest.custom;

public class JSONEstatica {
	private Long id;
	private String titulo;
	private String contenido;
	private Long version;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "JSONEstatica [titulo=" + titulo + ", contenido="
				+ contenido+"]";
	}

}
