package es.enxenio.smart.citydriver.controller.menu;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.enxenio.smart.citydriver.controller.menu.form.MenuForm;
import es.enxenio.smart.citydriver.controller.menu.validator.MenuFormValidator;
import es.enxenio.smart.citydriver.controller.session.MensaxePendente;
import es.enxenio.smart.citydriver.controller.session.MensaxePendente.TipoMensaxe;
import es.enxenio.smart.citydriver.controller.session.SessionManager;
import es.enxenio.smart.citydriver.model.entradaMenu.EntradaMenu;
import es.enxenio.smart.citydriver.model.entradaMenu.service.EntradaMenuService;
import es.enxenio.smart.citydriver.model.menu.Menu;
import es.enxenio.smart.citydriver.model.menu.service.MenuService;
import es.enxenio.smart.citydriver.util.JsonUtil;

import com.fasterxml.jackson.databind.ObjectWriter;

@Controller
@RequestMapping("/menu")
public class MenuFormController {

	@Autowired
	private MenuService menuService;

	@Autowired
	private EntradaMenuService entradaMenuService;

	@RequestMapping(value = "/crear.htm", method = RequestMethod.GET)
	public String mostrarCrear(ModelMap model) {

		MenuForm menuForm = new MenuForm();
		model.addAttribute("menuForm", menuForm);		
		return null;

	}

	@RequestMapping(value = "/crear.htm", method = RequestMethod.POST)
	public String crear(HttpServletRequest request, ModelMap model,
			@ModelAttribute("menuForm") MenuForm menuForm,
			BindingResult result) {

		ValidationUtils.invokeValidator(new MenuFormValidator(),
				menuForm, result);
		if (result.hasErrors()) {
			model.addAttribute("menuForm", menuForm);
			return null;
		}

		Menu menu = menuForm.getMenu();
		List<EntradaMenu> entradasMenu = menuForm.getEntradasMenu();
		
		// Se le pasan las entradas de menú del menu a crear para crearlas y asociarlas al menú al que pertenecen
		menuService.create(menu);

		model.clear();


		return "redirect:/menu/editar?menu="+menu.getId();
	}

	@RequestMapping(value = "/editar", method = RequestMethod.GET)
	public String mostrarEditar(ModelMap model,
			@RequestParam(value = "menu", required = true) long idMenu) {

		List<EntradaMenu> entradasMenu = entradaMenuService.obterEntradaMenusByMenuId(idMenu);
		Menu menu = menuService.get(idMenu);
		MenuForm formulario = new MenuForm(menu);
		formulario.setEntradasMenu(entradasMenu);

		model.addAttribute("menuForm", formulario);
		return null;

	}

	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	public String editar(ModelMap model, HttpServletRequest request,
			@RequestParam(value = "menu", required = true) long idMenu,
			@ModelAttribute("menuForm") MenuForm formulario,
			BindingResult result, HttpSession session) {

		ValidationUtils.invokeValidator(new MenuFormValidator(),
				formulario, result);
		Menu menu = menuService.get(idMenu);
		menu = formulario.getMenu(menu);
		if (result.hasErrors()) {
			model.addAttribute("menu", menu);
			model.addAttribute("menuForm", formulario);

			//				model.addAttribute("capitulo",
			//						menuService.getCapitulo(formulario.getIdCapitulo()));
			return null;
		}
		menuService.update(menu);

		SessionManager.addMensaxePendente(session,  
				new MensaxePendente("cityDriver.gardarMenu.avkiso.menuEditado", TipoMensaxe.CORRECTO));

		return "redirect:/menu/editar?menu="+idMenu;
	}

}
