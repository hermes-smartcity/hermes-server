package es.enxenio.smart.citydriver.controller.menu;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.enxenio.smart.citydriver.model.entradaMenu.EntradaMenu;
import es.enxenio.smart.citydriver.model.entradaMenu.service.EntradaMenuService;
import es.enxenio.smart.citydriver.model.menu.Menu;
import es.enxenio.smart.citydriver.model.menu.service.MenuService;
import es.enxenio.smart.citydriver.model.util.exceptions.InstanceNotFoundException;
import es.enxenio.smart.citydriver.web.rest.custom.JSONEntradaMenu;
import es.enxenio.smart.citydriver.web.rest.custom.JSONMenu;
import es.enxenio.smart.citydriver.web.rest.events.MainResource;


@RestController
@RequestMapping(value = "/menu")
public class MenusController extends MainResource {
	private final Logger log = LoggerFactory
			.getLogger(MenusController.class);

	@Autowired
	private MenuService menuService;
	
	@Autowired
	private EntradaMenuService entradaMenuService;
	
	@RequestMapping(value="/json/menus", method = RequestMethod.GET)
	public List<Menu> getMenus() {
		System.out.println("------------ "+menuService.obterMenus().size());
		return menuService.obterMenus();

	}
	
	@RequestMapping(value="/json/getMenu", method = RequestMethod.GET)
	public Menu getMenu(@RequestParam(required = true) Long idMenu) {		
		return menuService.get(idMenu);

	}
	
	//TODO IMPORTANTE cambiar a q se liste por menu¿?
	@RequestMapping(value="/json/entradasMenu", method = RequestMethod.GET)
	public List< EntradaMenu> getEntradasMenu(	@RequestParam(required = true) Long idMenu) {
		return entradaMenuService.obterEntradaMenusByMenuId(idMenu);

	}
	
	@RequestMapping(value = "/menus/siguienteid", method = RequestMethod.GET)
	public Long obtenerSiguienteId() {
		return menuService.obtenerSiguienteId();
	}
	
	@RequestMapping(value = "/guardarMenu", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void guardarMenus(
			@RequestBody(required = false) JSONMenu menu,
			HttpServletRequest request) throws InstanceNotFoundException {

		try {
			menuService.guardarMenu(menu);
		} catch (Exception e) {
			log.error("Error al guardar menu: ", e);
//			log.error("JSONMenu = {}", menu.toString());
			throw e;
		}
	}

}
