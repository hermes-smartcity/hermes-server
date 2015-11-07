package es.udc.lbd.hermes.model.usuario.dao;

import java.util.List;

import es.udc.lbd.hermes.model.usuario.Usuario;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface UsuarioDao extends GenericDao<Usuario, Long> {
	
	public List<Usuario> obterUsuarios();
	public Usuario findBySourceId(String sourceId);

}
