package es.enxenio.smart.citydriver.model.usuario.service;

import java.util.List;

import es.enxenio.smart.citydriver.model.usuario.Usuario;

public interface UsuarioService {

	public Usuario get(Long id);
	
	public void create(Usuario usuario);
	
	public void update(Usuario usuario);
	
	public void delete(Long id);
	
	public List<Usuario> obterUsuarios();
	
	public Usuario getBySourceId(String sourceId);
}
