package es.udc.lbd.hermes.model.usuario.usuarioWeb.dao;

import java.util.List;

import es.udc.lbd.hermes.model.usuario.usuarioWeb.Rol;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.UsuarioWeb;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface UsuarioWebDao extends GenericDao<UsuarioWeb, Long> {
	
	public List<UsuarioWeb> obterUsuariosWeb();
	public List<UsuarioWeb> obterUsuariosWebSegunRol(Rol rol);
	public UsuarioWeb findBySourceId(String sourceId);
	public UsuarioWeb findByEmail(String email);
}
