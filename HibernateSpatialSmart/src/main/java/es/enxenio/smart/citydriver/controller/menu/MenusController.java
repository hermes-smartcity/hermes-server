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
	
	@RequestMapping(value = "/guardarMenu", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void guardarMenus(
			@RequestBody(required = true) JSONMenu menu,
			HttpServletRequest request) throws InstanceNotFoundException {

		try {
			menuService.guardarMenu(menu);
		} catch (Exception e) {
			log.error("Error al guardar menu: ", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/actualizarNombresDeMenus", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void actualizarNombresDeMenus(
			@RequestBody(required = false) List<JSONMenu> menus,
			HttpServletRequest request) throws InstanceNotFoundException {

		try {			
			menuService.actualizarNombresDeMenus(menus);
		} catch (Exception e) {
			log.error("Error al actualizar nombres de menus : ", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/borrarMenu", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void borrarMenu(
			@RequestBody(required = true) Long id,
			HttpServletRequest request) throws InstanceNotFoundException {

		try {
			menuService.delete(id);
		} catch (Exception e) {
			log.error("Error al borrar menu: ", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/editarMenu", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void editarMenu(
			@RequestBody(required = true)JSONMenu menu,
			HttpServletRequest request) throws InstanceNotFoundException {

		try {			
			menuService.editarMenu(menu);
		} catch (Exception e) {
			log.error("Error al editar menu : ", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/borrarEntradaMenu", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void borrarEntradaMenu(
			@RequestBody(required = true) Long id,
			HttpServletRequest request) throws InstanceNotFoundException {

		try {
			entradaMenuService.delete(id);
		} catch (Exception e) {
			log.error("Error al borrar entrada menu: ", e);
			throw e;
		}
	}

}
