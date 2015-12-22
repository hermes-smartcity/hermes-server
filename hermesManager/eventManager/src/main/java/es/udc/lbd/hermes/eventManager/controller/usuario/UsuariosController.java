package es.udc.lbd.hermes.eventManager.controller.usuario;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.eventManager.web.rest.MainResource;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.service.UsuarioMovilService;



@RestController
@RequestMapping(value = "/usuario")
public class UsuariosController extends MainResource {

	static Logger logger = Logger.getLogger(UsuariosController.class);
	@Autowired
	private UsuarioMovilService usuarioMovilService;
	
	@RequestMapping(value="/json/usuarios", method = RequestMethod.GET)
	public List<UsuarioMovil> getUsuarios() {
		return usuarioMovilService.obterUsuariosMovil();

	}
	
}
