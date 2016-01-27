package es.udc.lbd.hermes.eventManager.controller.usuario;

import org.apache.log4j.Logger;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import es.udc.lbd.hermes.model.usuario.usuarioMovil.service.UsuarioMovilService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioMovilService usuarioMovilService;
	
//	private Logger logger = LoggerFactory.getLogger(getClass());
	static Logger logger = Logger.getLogger(UsuarioController.class);
	
	@RequestMapping("/listar")
	public void listar(
			ModelMap model) {
		model.addAttribute("usuarios", usuarioMovilService.obterUsuariosMovil());

	}
}
