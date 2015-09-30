package es.enxenio.smart.citydriver.web.rest.custom;

import java.util.List;

public class JSONMenu {
	private String nombre;
	private List<JSONEntradaMenu> entradasMenu;

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

	@Override
	public String toString() {
		return "JSONMenu [nombre=" + nombre + ", entradasMenu="
				+ entradasMenu+"]";
	}

}
