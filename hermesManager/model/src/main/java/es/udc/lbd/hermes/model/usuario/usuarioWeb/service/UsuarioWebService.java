package es.udc.lbd.hermes.model.usuario.usuarioWeb.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import es.udc.lbd.hermes.model.usuario.usuarioWeb.UsuarioWeb;


public interface UsuarioWebService extends UserDetailsService {

	public UsuarioWeb get(Long id);
	
	public void create(UsuarioWeb usuarioWeb);
	
	public void update(UsuarioWeb usuarioWeb);
	
	public void delete(Long id);
	
	public List<UsuarioWeb> obterUsuariosWeb();
	
	public UsuarioWeb getBySourceId(String sourceId);
	
	public UsuarioWeb getUser(String email, String passwordEncr);
	
	public UserDetails loadUserByUsername(String username);
	
	public UsuarioWeb findByName(String name);
	
}
