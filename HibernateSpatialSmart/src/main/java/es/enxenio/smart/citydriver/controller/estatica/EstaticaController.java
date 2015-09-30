package es.enxenio.smart.citydriver.controller.estatica;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.enxenio.smart.citydriver.model.estatica.service.EstaticaService;


@Controller
public class EstaticaController {

	@Autowired
	private EstaticaService estaticaService;
	
	@RequestMapping("/estatica/listar")
	public void listar(ModelMap model) {
		//TODO decidir si va a ser con Angular o no
		model.addAttribute("estaticas",	estaticaService.obterEstaticas());
	}
	
	//TODO Decidir si va a ser por ajax!!
	@RequestMapping("/ajax/estatica/eliminar")
	public void eliminar(HttpServletRequest request,
			@RequestParam(value = "estatica", required = true) Long idEstatica) {
		estaticaService.delete(idEstatica);
	}

}
