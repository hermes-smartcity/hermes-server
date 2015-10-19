package es.enxenio.smart.citydriver.model.menu.service;

import java.util.List;

import es.enxenio.smart.citydriver.model.entradaMenu.EntradaMenu;
import es.enxenio.smart.citydriver.model.menu.Menu;
import es.enxenio.smart.citydriver.model.util.exceptions.InstanceNotFoundException;
import es.enxenio.smart.citydriver.web.rest.custom.JSONEstatica;
import es.enxenio.smart.citydriver.web.rest.custom.JSONMenu;

public interface MenuService {

	public Menu get(Long id);
	
	public void create(Menu menu/*, List<EntradaMenu> entradasMenu*/);
	
	public void update(Menu menu);
	
	public void delete(Long id);

	public List<Menu> obterMenus();
		
	public void guardarMenu(JSONMenu menu) throws InstanceNotFoundException;
	
	public void actualizarNombresDeMenus(List<JSONMenu> menu);
	
	public void editarMenu(JSONMenu menuJson) throws InstanceNotFoundException;	


}
