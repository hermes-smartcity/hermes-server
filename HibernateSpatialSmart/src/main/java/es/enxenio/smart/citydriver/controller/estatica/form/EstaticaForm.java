package es.enxenio.smart.citydriver.controller.estatica.form;


import es.enxenio.smart.citydriver.model.estatica.Estatica;

public class EstaticaForm {
	
	private String titulo;
	
	private String contenido;
		
	public EstaticaForm() { }
	
	public EstaticaForm(Estatica estatica) {
		this.titulo =estatica.getTitulo();
		this.contenido = estatica.getContenido();
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

	public Estatica getEstatica(Estatica estatica) {
		System.out.println("form -------"+titulo);
		estatica.setTitulo(titulo);
		estatica.setContenido(contenido);
		return estatica;
	}
	
	
	public Estatica getEstatica() {
		return getEstatica(new Estatica());
	}

}
