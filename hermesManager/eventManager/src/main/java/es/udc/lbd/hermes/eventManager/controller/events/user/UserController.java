package es.udc.lbd.hermes.eventManager.controller.events.user;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.eventManager.transfer.TokenTransfer;
import es.udc.lbd.hermes.eventManager.transfer.UserTransfer;
import es.udc.lbd.hermes.eventManager.util.TokenUtils;
import es.udc.lbd.hermes.eventManager.web.rest.MainResource;
import es.udc.lbd.hermes.model.events.driverFeatures.DriverFeatures;
import es.udc.lbd.hermes.model.usuario.exceptions.NoEsPosibleBorrarseASiMismoException;
import es.udc.lbd.hermes.model.usuario.exceptions.NoExiteNingunUsuarioMovilConSourceIdException;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.Rol;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.UserJSON;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.UsuarioWeb;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.service.UsuarioWebService;

@CrossOrigin
@RestController
//@RequestMapping(value = "/api/user")
public class UserController extends MainResource {

	static Logger logger = Logger.getLogger(UserController.class);
	
	@Autowired private UsuarioWebService usuarioWebService;

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authManager;

	// Recuperar usuario logueado
	@RequestMapping(value = "/api/user", method = RequestMethod.GET)
	public UserTransfer getUser()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof String && ((String) principal).equals("anonymousUser")) {
			throw new WebApplicationException(401);
		}
		UserDetails userDetails = (UserDetails) principal;

		return new UserTransfer(userDetails.getUsername(), this.createRoleMap(userDetails));
	}

	// Eliminar usuario - Sólo administradores
	@RequestMapping(value = "/api/user" + "/{id}", method = RequestMethod.DELETE)
	public void eliminar(@PathVariable(value = "id") Long usuarioId) throws NoEsPosibleBorrarseASiMismoException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String login = auth.getName();
		usuarioWebService.eliminar(usuarioId, login);
	}
	
	// Registrar usuario - Sólo administradores
	@RequestMapping(value = "/api/user", method = RequestMethod.POST)
	public void registerUser(@RequestBody UserJSON userJSON)
	{
		try {
			usuarioWebService.registerUser(userJSON);
		} catch (NoExiteNingunUsuarioMovilConSourceIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Editar usuario - Sólo administradores -> Pensada para modificar contraseña y usuario de movil asociado. TODO luego dividir. PROVISIONAL
	@RequestMapping(value =  "/api/user/{id}", method = RequestMethod.PUT)
	public void updateUser(@PathVariable Long id, @RequestBody UserJSON userJSON) {
		usuarioWebService.updateUser(userJSON, id);
	}
	
	// Autenticar usuario
	@RequestMapping(value = "/api/authenticate", method = RequestMethod.POST)
	public TokenTransfer authenticate(@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "password", required = false) String password)
	{
		
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(username, password);
		Authentication authentication = this.authManager.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		UserDetails userDetails = this.usuarioWebService.loadUserByUsername(username);

		return new TokenTransfer(TokenUtils.createToken(userDetails));

	}
	
	// Listar usuarios consulta - Sólo administradores
	@RequestMapping(value="/api/user/json/users", method = RequestMethod.GET)
	public List<UsuarioWeb> getUsers() {
		return usuarioWebService.obterUsuariosWebSegunRol(Rol.ROLE_CONSULTA);
	}
	
	// Listar usuarios administradores - Sólo administradores
	@RequestMapping(value="/api/user/json/admins", method = RequestMethod.GET)
	public List<UsuarioWeb> getAdmins() {
		return usuarioWebService.obterUsuariosWebSegunRol(Rol.ROLE_ADMIN);
	}
	
	// Tipos de usuarios
	@RequestMapping(value="/api/user/json/roles", method = RequestMethod.GET)
	public List<Rol> roles() {
		return Arrays.asList(Rol.values());
	}
	
	@RequestMapping(value="/api/user/json/userToModify", method = RequestMethod.GET)
	public UsuarioWeb getUserToModify(@RequestParam(value = "id", required = true) Long id) {
		return usuarioWebService.get(id);

	}
	
	//TODO no sé porque falla
//	@RequestMapping(value = "/api/user/json/userToModify", method = RequestMethod.GET)
//	public UsuarioWeb getUserToModify(@PathVariable Long id)
//	{
//		return usuarioWebService.get(id);
//	}

	private Map<String, Boolean> createRoleMap(UserDetails userDetails)
	{
		Map<String, Boolean> roles = new HashMap<String, Boolean>();
		for (GrantedAuthority authority : userDetails.getAuthorities()) {
			roles.put(authority.getAuthority(), Boolean.TRUE);
		}

		return roles;
	}
	
}
