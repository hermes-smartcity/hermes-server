package es.udc.lbd.hermes.model.usuario.usuarioMovil.dao;

import java.util.List;

import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface UsuarioMovilDao extends GenericDao<UsuarioMovil, Long> {
	
	public List<UsuarioMovil> obterUsuariosMovil();
	public UsuarioMovil findBySourceId(String sourceId);

}
