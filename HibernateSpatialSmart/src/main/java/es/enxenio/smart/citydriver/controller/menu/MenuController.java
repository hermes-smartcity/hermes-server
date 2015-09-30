package es.enxenio.smart.citydriver.controller.menu;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.enxenio.smart.citydriver.model.menu.service.MenuService;
import es.enxenio.smart.citydriver.util.ConstantesController;


@Controller
public class MenuController {

	@Autowired
	private MenuService menuService;
	
	@RequestMapping("/menu/listar")
	public void listar(ModelMap model) {
		model.addAttribute("menus",
				menuService.obterMenus());
	}
	
	//TODO decidir si va a ser por ajax!!
	@RequestMapping("/ajax/menu/eliminar")
	public void eliminar(HttpServletRequest request,
			@RequestParam(value = "menu", required = true) Long idMenu) {
		menuService.delete(idMenu);
	}
	
}
