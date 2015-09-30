package es.enxenio.smart.citydriver.model.entradaMenu.dao;

import java.util.List;

import es.enxenio.smart.citydriver.model.entradaMenu.EntradaMenu;
import es.enxenio.smart.citydriver.model.events.dataSection.DataSection;
import es.enxenio.smart.citydriver.model.menu.Menu;
import es.enxenio.smart.citydriver.model.util.dao.GenericDao;

public interface EntradaMenuDao extends GenericDao<EntradaMenu, Long> {	
	public List<EntradaMenu> obterEntradaMenus();
	public List<EntradaMenu> obterEntradaMenusByMenuId(Long idMenu);
	public void create(EntradaMenu entradaMenu, Menu menu, EntradaMenu entradaMenuPadre);
	public Long obtenerSiguienteId();
}
