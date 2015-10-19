package es.enxenio.smart.citydriver.web.rest.custom;

import java.util.List;

public class JSONMenu {
	private Long id;
	private String nombre;
	private List<JSONEntradaMenu> entradasMenu;
	private Long version;

	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<JSONEntradaMenu> getEntradasMenu() {
		return entradasMenu;
	}
	
	public void setEntradasMenu(List<JSONEntradaMenu> entradasMenu) {
		this.entradasMenu = entradasMenu;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "JSONMenu [nombre=" + nombre + ", entradasMenu="
				+ entradasMenu+"]";
	}

}
