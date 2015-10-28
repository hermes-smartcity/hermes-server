package es.enxenio.smart.model.usuario.dao;

import java.util.List;

import es.enxenio.smart.model.usuario.Usuario;
import es.enxenio.smart.model.util.dao.GenericDao;


public interface UsuarioDao extends GenericDao<Usuario, Long> {
	
	public List<Usuario> obterUsuarios();
	public Usuario findBySourceId(String sourceId);

}
