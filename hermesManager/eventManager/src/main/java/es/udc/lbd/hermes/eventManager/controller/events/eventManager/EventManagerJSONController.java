package es.udc.lbd.hermes.eventManager.controller.events.eventManager;


import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.WebApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.eventManager.EventManager;
import es.udc.lbd.hermes.eventManager.controller.util.JSONData;
import es.udc.lbd.hermes.eventManager.transfer.TokenTransfer;
import es.udc.lbd.hermes.eventManager.transfer.UserTransfer;
import es.udc.lbd.hermes.eventManager.util.TokenUtils;
import es.udc.lbd.hermes.eventManager.web.rest.MainResource;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.service.UsuarioWebService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/eventManager")
public class EventManagerJSONController extends MainResource {
//	private final Logger log = LoggerFactory
//			.getLogger(EventManager.class);

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authManager;
	
	@Autowired private EventManager eventManager;
	
	@Autowired private UsuarioWebService usuarioWebService;
	
	@RequestMapping(value = "/arrancar", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void arrancar(){		
		eventManager.startEventProcessor();
	}
	
	@RequestMapping(value = "/parar", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void parar(){		
		eventManager.stopEventProcessor();
	}
	
	@RequestMapping(value="/json/stateEventManager", method = RequestMethod.GET)
	public JSONData getStateEventManager() {
		JSONData jsonData = new JSONData();
		// Parado
		if (eventManager.getEventProcessor() == null)			
			jsonData.setValue("Stopped");			
		else jsonData.setValue("Running");
		
		return jsonData;
	}

	@RequestMapping(value = "json/user", method = RequestMethod.GET)
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

	@RequestMapping(value = "json/authenticate", method = RequestMethod.POST)
	public TokenTransfer authenticate(@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "password", required = false) String password)
	{
		
		
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(username, password);
		Authentication authentication = this.authManager.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		UserDetails userDetails = this.usuarioWebService.loadUserByUsername(username);
		TokenTransfer tk = new TokenTransfer(TokenUtils.createToken(userDetails));
		System.out.println("-- tk "+tk.getToken());
		return new TokenTransfer(TokenUtils.createToken(userDetails));

	}

	private Map<String, Boolean> createRoleMap(UserDetails userDetails)
	{
		Map<String, Boolean> roles = new HashMap<String, Boolean>();
		for (GrantedAuthority authority : userDetails.getAuthorities()) {
			roles.put(authority.getAuthority(), Boolean.TRUE);
		}

		return roles;
	}

	
}
