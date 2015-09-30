package es.enxenio.smart.citydriver.model.entradaMenu.service;

import java.util.List;

import es.enxenio.smart.citydriver.model.entradaMenu.EntradaMenu;
import es.enxenio.smart.citydriver.model.menu.Menu;

public interface EntradaMenuService {

	public EntradaMenu get(Long id);	
	
	public void update(EntradaMenu menu);
	
	public void delete(Long id);

	public List<EntradaMenu> obterEntradaMenus();
	
	public List<EntradaMenu> obterEntradaMenusByMenuId(Long menuId);

	public Long obtenerSiguienteId();
}
