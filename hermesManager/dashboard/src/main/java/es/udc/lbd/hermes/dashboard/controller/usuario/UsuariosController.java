package es.udc.lbd.hermes.dashboard.controller.usuario;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.dashboard.web.rest.events.MainResource;
import es.udc.lbd.hermes.model.usuario.Usuario;
import es.udc.lbd.hermes.model.usuario.service.UsuarioService;



@RestController
@RequestMapping(value = "/usuario")
public class UsuariosController extends MainResource {
	private final Logger log = LoggerFactory
			.getLogger(UsuariosController.class);

	@Autowired
	private UsuarioService usuarioService;
	
	@RequestMapping(value="/json/usuarios", method = RequestMethod.GET)
	public List<Usuario> getUsuarios() {
		return usuarioService.obterUsuarios();

	}
	
}
