package es.enxenio.smart.citydriver.controller.menu.form;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AutoPopulatingList;

import es.enxenio.smart.citydriver.model.entradaMenu.EntradaMenu;
import es.enxenio.smart.citydriver.model.menu.Menu;


public class MenuForm {
	
	// TODO habrá un formulario aparte, un paquete aparte de submenu? Osea este compuesto, y el otro Simple/Hijo¿? 
	// Que cuando se cree temga que hacerse desde un padre?¿ Luego lo de reordenar sería otra parte
//	private Long idCapitulo;

	private String nombre;
	
	private List<EntradaMenu> entradasMenu;
	
	//TODO prueba
	String texto;
	
	public MenuForm() {
		this.setEntradasMenu(new AutoPopulatingList(EntradaMenu.class));
	}
	
	public MenuForm(Menu menu) {
		this.nombre =menu.getNombre();	
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<EntradaMenu> getEntradasMenu() {
		return entradasMenu;
	}

	public void setEntradasMenu(List<EntradaMenu> entradasMenu) {
		this.entradasMenu = entradasMenu;
	}

	//TODO prueba
	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Menu getMenu(Menu menu) {
		menu.setNombre(nombre);
//		if (entradasMenu != null) {
//			menu.setEntradasMenu(entradasMenu);
//		} else {
//			menu.setEntradasMenu(Collections.<EntradaMenu> emptyList());
//		}
		
		return menu;
	}
	
	//TODO si va ser crear un hijo de un menu
	public Menu getMenu() {
		return getMenu(new Menu());
	}

}
