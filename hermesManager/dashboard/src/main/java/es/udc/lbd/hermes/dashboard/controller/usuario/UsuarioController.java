package es.udc.lbd.hermes.dashboard.controller.usuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import es.udc.lbd.hermes.model.usuario.service.UsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	
	@RequestMapping("/listar")
	public void listar(
			ModelMap model) {
		model.addAttribute("usuarios", usuarioService.obterUsuarios());

	}
}
