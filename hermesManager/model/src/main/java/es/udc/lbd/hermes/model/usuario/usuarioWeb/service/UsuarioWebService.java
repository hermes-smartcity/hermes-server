package es.udc.lbd.hermes.model.usuario.usuarioWeb.service;

import java.util.List;

import es.udc.lbd.hermes.model.usuario.usuarioWeb.UsuarioWeb;


public interface UsuarioWebService {

	public UsuarioWeb get(Long id);
	
	public void create(UsuarioWeb usuarioWeb);
	
	public void update(UsuarioWeb usuarioWeb);
	
	public void delete(Long id);
	
	public List<UsuarioWeb> obterUsuariosWeb();
	
	public UsuarioWeb getBySourceId(String sourceId);
}
