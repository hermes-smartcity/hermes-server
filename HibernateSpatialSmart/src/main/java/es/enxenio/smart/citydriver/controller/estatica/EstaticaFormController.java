package es.enxenio.smart.citydriver.controller.estatica;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.enxenio.smart.citydriver.controller.estatica.form.EstaticaForm;
import es.enxenio.smart.citydriver.controller.estatica.validator.EstaticaFormValidator;
import es.enxenio.smart.citydriver.controller.session.MensaxePendente;
import es.enxenio.smart.citydriver.controller.session.MensaxePendente.TipoMensaxe;
import es.enxenio.smart.citydriver.controller.session.SessionManager;
import es.enxenio.smart.citydriver.model.estatica.Estatica;
import es.enxenio.smart.citydriver.model.estatica.service.EstaticaService;

@Controller
@RequestMapping("/estatica")
public class EstaticaFormController {

	@Autowired
	private EstaticaService estaticaService;

	//Para el editor. Contenido -> HTML
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@RequestMapping(value = "/crear", method = RequestMethod.GET)
	public String mostrarCrear(ModelMap model) {
			EstaticaForm estaticaForm = new EstaticaForm();
			model.addAttribute("estaticaForm", estaticaForm);
			return null;
	}

	@RequestMapping(value = "/crear", method = RequestMethod.POST)
	public String crear(HttpServletRequest request, ModelMap model,
			@ModelAttribute("estaticaForm") EstaticaForm estaticaForm,
			BindingResult result) {

			ValidationUtils.invokeValidator(new EstaticaFormValidator(),
					estaticaForm, result);
			if (result.hasErrors()) {
				model.addAttribute("estaticaForm", estaticaForm);
			
				return null;
			}

			Estatica estatica = estaticaForm.getEstatica();

			estaticaService.create(estatica);

			model.clear();
		
		return "redirect:/estatica/listar";
	}

	@RequestMapping(value = "/editar", method = RequestMethod.GET)
	public String mostrarEditar(ModelMap model,
			@ModelAttribute("estaticaForm") EstaticaForm formulario,
			@RequestParam(value = "estatica", required = true) long idEstatica) {
		
			Estatica estatica = estaticaService.get(idEstatica);

			formulario.setContenido(estatica.getContenido());
			formulario.setTitulo(estatica.getTitulo());
			model.addAttribute("estatica", estatica);
			return null;
		
	}

	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	public String editar(ModelMap model, HttpServletRequest request,
			@RequestParam(value = "estatica", required = true) long idEstatica,
			@ModelAttribute("estaticaForm") EstaticaForm formulario,
			BindingResult result, HttpSession session) {
			
			ValidationUtils.invokeValidator(new EstaticaFormValidator(),
					formulario, result);
			Estatica estatica = estaticaService.get(idEstatica);
			
			estatica = formulario.getEstatica(estatica);
			
			if (result.hasErrors()) {
				model.addAttribute("estatica", estatica);
				model.addAttribute("estaticaForm", formulario);
				return null;
			}


			estaticaService.update(estatica);

		
		SessionManager.addMensaxePendente(session,  
				new MensaxePendente("cityDriver.guardarEstatica.aviso.estaticaEditada", TipoMensaxe.CORRECTO));
		
		return "redirect:/estatica/editar?estatica="+idEstatica;
	}

}
