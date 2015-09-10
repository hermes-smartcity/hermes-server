package es.enxenio.smart.citydriver.model.usuario.dao;

import java.util.List;

import es.enxenio.smart.citydriver.model.usuario.Usuario;
import es.enxenio.smart.citydriver.model.util.dao.GenericDao;

public interface UsuarioDao extends GenericDao<Usuario, Long> {
	
	public List<Usuario> obterUsuarios();
	public Usuario findBySourceId(String sourceId);

}
