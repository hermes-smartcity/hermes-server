package es.enxenio.smart.citydriver.model.menu.dao;

import java.util.List;

import es.enxenio.smart.citydriver.model.events.dataSection.DataSection;
import es.enxenio.smart.citydriver.model.menu.Menu;
import es.enxenio.smart.citydriver.model.util.dao.GenericDao;

public interface MenuDao extends GenericDao<Menu, Long> {	
	public List<Menu> obterMenus();
	public Long obtenerSiguienteId();
}
