package es.udc.lbd.hermes.eventManager.controller.events.user;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.WebApplicationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.eventManager.controller.util.JSONData;
import es.udc.lbd.hermes.eventManager.controller.util.JSONDataType;
import es.udc.lbd.hermes.eventManager.transfer.TokenTransfer;
import es.udc.lbd.hermes.eventManager.transfer.UserTransfer;
import es.udc.lbd.hermes.eventManager.util.TokenUtils;
import es.udc.lbd.hermes.eventManager.web.rest.MainResource;
import es.udc.lbd.hermes.model.usuario.exceptions.ActivarCuentaException;
import es.udc.lbd.hermes.model.usuario.exceptions.EnlaceCaducadoException;
import es.udc.lbd.hermes.model.usuario.exceptions.NoEsPosibleBorrarseASiMismoException;
import es.udc.lbd.hermes.model.usuario.exceptions.NoExiteNingunUsuarioMovilConSourceIdException;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.PasswordJSON;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.Rol;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.UserJSON;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.UsuarioWeb;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.service.UsuarioWebService;
import es.udc.lbd.hermes.model.util.exceptions.DuplicateEmailException;

@RestController
@RequestMapping(value = "/api/user")
public class UserController extends MainResource {

	static Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	private UsuarioWebService usuarioWebService;

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authManager;

	// Autenticar usuario
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<TokenTransfer> authenticate(@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "password", required = false) String password) {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);
		try {
			Authentication authentication = this.authManager.authenticate(authenticationToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
	
			UserDetails userDetails = this.usuarioWebService.loadUserByUsername(username);
			 return new ResponseEntity<>(new TokenTransfer(TokenUtils.createToken(userDetails)), HttpStatus.OK);
//			return new TokenTransfer(TokenUtils.createToken(userDetails));
		} catch (BadCredentialsException e) {				
			logger.info("Bad credentials");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
	}

	// Recuperar usuario logueado
	@RequestMapping(value = "/getUser", method = RequestMethod.GET)
	public UserTransfer getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof String && ((String) principal).equals("anonymousUser")) {
			throw new WebApplicationException(401);
		}
		UserDetails userDetails = (UserDetails) principal;

		return new UserTransfer(userDetails.getUsername(), this.createRoleMap(userDetails));
	}

	// Registrar usuario
	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public JSONData registerUser(@RequestBody UserJSON userJSON) {
		JSONData jsonD = new JSONData();
		try {			
			// TODO paso por parametro el locale?
			// new Locale("es")
			usuarioWebService.registerUser(userJSON, Locale.getDefault(), false);
			jsonD.setValue("Usuario/a registrado/a correctamente. En unos instantes recibirá un correo para completar su registro en Dashboard.");
		} catch (NoExiteNingunUsuarioMovilConSourceIdException e) {
			jsonD.setValue("No exite ningún usuario movil con el email indicado. Primero debe instalar la aplicación Smart Driver en su teléfono móvil");
			logger.error("No exite ningún usuarioMovil con ese sourceId");
		} catch (DuplicateEmailException e) {
			jsonD.setValue("DuplicateEmailException");
			logger.error("DuplicateEmailException");
		}
		return jsonD;
	}

	// Controlador al que redirigimos desde un enlace de activación
	@RequestMapping(value = "/activarCuenta")
	public JSONData activarConta(@RequestParam(required = true) String email,
			@RequestParam(required = true) String hash) {

		JSONData jsonD = new JSONData();
		try {
			usuarioWebService.activarCuenta(email, hash);
			jsonD.setValue("Su usuario se ha registrado correctamente");
		} catch (ActivarCuentaException | EnlaceCaducadoException e) {
			jsonD.setValue("Ha surgido un problema al activar su cuenta. Si el problema persiste contacte con el administrador del sistema");
			logger.error("ActivarCuentaException "+e);
		}

		return jsonD;
	}
	
	// Registrar admin - Sólo administradores
	@RequestMapping(value = "/registerAdmin", method = RequestMethod.POST)
	public void registerAdmin(@RequestBody UserJSON userJSON) {
		JSONData jsonD = new JSONData();
		try {
			// TODO paso por parametro el locale?
			// new Locale("es")
			usuarioWebService.registerUser(userJSON, Locale.getDefault(), true);
			jsonD.setValue("Usuario/a registrado/a correctamente.En unos instantes el administrador/a dado/a de alta recibirá un correo para completar su registro en Dashboard.");
		} catch (NoExiteNingunUsuarioMovilConSourceIdException | DuplicateEmailException e) {
			jsonD.setValue("No exite ningún usuario movil con el email indicado. Comunique al usuario administrador que primero debe instalar la aplicación Smart Driver en su teléfono móvil");
			logger.error("No exite ningún usuarioMovil con ese sourceId");
		}

	}

	// Editar usuario - Sólo administradores 
	@RequestMapping(value = "/editUser/{id}", method = RequestMethod.PUT)
	public JSONData updateUser(@PathVariable Long id, @RequestBody UserJSON userJSON) {
		JSONData jsonD = new JSONData();
		usuarioWebService.updateUser(userJSON, id);
		jsonD.setValue("Usuario/a modificado/a correctamente");
		return jsonD;
	}

	// Eliminar usuario - Sólo administradores
	@RequestMapping(value = "/deleteUser" + "/{id}", method = RequestMethod.DELETE)
	public JSONData eliminar(@PathVariable(value = "id") Long usuarioId) throws NoEsPosibleBorrarseASiMismoException {
		JSONData jsonD = new JSONData();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String login = auth.getName();
		usuarioWebService.eliminar(usuarioId, login);
		jsonD.setValue("Usuario/a eliminado/a correctamente");
		return jsonD;
	}

	// Listar usuarios consulta - Sólo administradores
	@RequestMapping(value = "/json/users", method = RequestMethod.GET)
	public List<UsuarioWeb> getUsers() {
		return usuarioWebService.obterUsuariosWebSegunRol(Rol.ROLE_CONSULTA);
	}

	// Listar usuarios administradores - Sólo administradores
	@RequestMapping(value = "/json/admins", method = RequestMethod.GET)
	public List<UsuarioWeb> getAdmins() {
		return usuarioWebService.obterUsuariosWebSegunRol(Rol.ROLE_ADMIN);
	}

	// Tipos de usuarios
	@RequestMapping(value = "/json/roles", method = RequestMethod.GET)
	public List<Rol> roles() {
		return Arrays.asList(Rol.values());
	}

	@RequestMapping(value = "/json/userToModify", method = RequestMethod.GET)
	public UsuarioWeb getUserToModify(@RequestParam(value = "id", required = true) Long id) {
		return usuarioWebService.get(id);

	}

	// Generar nuevo token
	@RequestMapping(value = "/renewToken", method = RequestMethod.POST)
	public ResponseEntity<TokenTransfer> renewToken(@RequestBody String oldToken) {
		
		String userName = TokenUtils.getUserNameFromToken(oldToken);

		if (userName != null) {

			UserDetails userDetails = this.usuarioWebService.loadUserByUsername(userName);
			
			String[] parts = oldToken.split(":");
			long expires = Long.parseLong(parts[1]);
			String signature = parts[2];
			
			boolean tokenValido = TokenUtils.compareTokenWithCredentials(signature, userDetails, expires);
			
			if (tokenValido){
				//Como la firma es valida, generamos un nuevo token
				return new ResponseEntity<>(new TokenTransfer(TokenUtils.createToken(userDetails)), HttpStatus.OK);
			}else{
				logger.info("Token no Valido");
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
			}
		}else{
			logger.info("Bad credentials");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
	}
	
	// Registrar usuario
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public JSONDataType changePassword(@RequestBody PasswordJSON passwordJSON) {
		JSONDataType jsonD = new JSONDataType();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UsuarioWeb usuario = (UsuarioWeb) usuarioWebService.loadUserByUsername(auth.getName());
		String passwordBD = usuario.getPassword();

		//Si la contrasena antigua no coincide
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		Boolean sonIguales = passwordEncoder.matches(passwordJSON.getPasswordOld(), passwordBD);
		
		if (!sonIguales){
			jsonD.setKey("user.oldPasswordNotCorrect"); //corresponde con el translation_xx.js
			jsonD.setType("error");
			logger.error("Contraseña vieja no coincide");
		}else{
			//Si las nuevas contrasenas no son iguales avisamos
			if (!passwordJSON.getPasswordNew1().equals(passwordJSON.getPasswordNew2())){
				jsonD.setKey("user.passwordsNotSame"); //corresponde con el translation_xx.js
				jsonD.setType("error");
				logger.error("Las contraseñas no son iguales");
			}else{
				usuarioWebService.changePassword(passwordJSON, usuario);
				jsonD.setKey("user.passwordOk"); //corresponde con el translation_xx.js
				jsonD.setType("info");
				logger.info("Contraseña cambiada correctamente");
			}

		}
		
		return jsonD;
	}
	
	// Listar usuarios consulta - Sólo administradores
	@RequestMapping(value = "/userProfile", method = RequestMethod.GET)
	public UsuarioWeb userProfile() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UsuarioWeb usuario = (UsuarioWeb) usuarioWebService.loadUserByUsername(auth.getName());
		
		return usuario;
	}
		
	private Map<String, Boolean> createRoleMap(UserDetails userDetails) {
		Map<String, Boolean> roles = new HashMap<String, Boolean>();
		for (GrantedAuthority authority : userDetails.getAuthorities()) {
			roles.put(authority.getAuthority(), Boolean.TRUE);
		}

		return roles;
	}

}
