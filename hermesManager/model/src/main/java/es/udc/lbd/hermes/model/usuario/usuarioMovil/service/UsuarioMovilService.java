package es.udc.lbd.hermes.model.usuario.usuarioMovil.service;

import java.util.List;

import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;



public interface UsuarioMovilService {

	public UsuarioMovil get(Long id);
	
	public void create(UsuarioMovil usuarioMovil);
	
	public void update(UsuarioMovil usuarioMovil);
	
	public void delete(Long id);
	
	public List<UsuarioMovil> obterUsuariosMovil();
	
	public UsuarioMovil getBySourceId(String sourceId);
	
	public long contar();
	
	public long getNumberActiveUsers();
}
