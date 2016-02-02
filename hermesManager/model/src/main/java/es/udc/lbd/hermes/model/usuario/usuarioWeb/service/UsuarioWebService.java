package es.udc.lbd.hermes.model.usuario.usuarioWeb.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import es.udc.lbd.hermes.model.usuario.exceptions.NoEsPosibleBorrarseASiMismoException;
import es.udc.lbd.hermes.model.usuario.exceptions.NoExiteNingunUsuarioMovilConSourceIdException;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.Rol;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.UserJSON;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.UsuarioWeb;


public interface UsuarioWebService extends UserDetailsService {

	public UsuarioWeb get(Long id);
	
	public void create(UsuarioWeb usuarioWeb);
	
	public void update(UsuarioWeb usuarioWeb);
	
	public void delete(Long id);
	
	public void eliminar(Long usuarioId, String email) throws NoEsPosibleBorrarseASiMismoException;
	
	public List<UsuarioWeb> obterUsuariosWeb();
	
	public List<UsuarioWeb> obterUsuariosWebSegunRol(Rol rol);
	
	public UsuarioWeb getBySourceId(String sourceId);
	
	public UserDetails loadUserByUsername(String username);
	
	public UsuarioWeb registerUser(UserJSON userJSON) throws NoExiteNingunUsuarioMovilConSourceIdException;
	
	public UsuarioWeb updateUser(UserJSON userJSON, Long id);

}
