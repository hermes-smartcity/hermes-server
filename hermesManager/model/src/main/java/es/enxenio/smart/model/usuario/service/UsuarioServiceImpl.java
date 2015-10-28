package es.enxenio.smart.model.usuario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.enxenio.smart.model.usuario.Usuario;
import es.enxenio.smart.model.usuario.dao.UsuarioDao;




@Service("usuarioService")
@Transactional
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired
	private UsuarioDao usuarioDao;
	
	@Override
	@Transactional(readOnly = true)
	public Usuario get(Long id) {
		return usuarioDao.get(id);
	}

	@Override
	public void create(Usuario usuario) {
		usuarioDao.create(usuario);
		
	}

	@Override
	public void update(Usuario usuario) {
		usuarioDao.update(usuario);
	}

	@Override
	public void delete(Long id) {
		Usuario usuario = usuarioDao.get(id);
		if (usuario != null) {
			usuarioDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<Usuario> obterUsuarios() {
		List<Usuario> usuarios = usuarioDao.obterUsuarios();
		return usuarios;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Usuario getBySourceId(String sourceId) {
		return usuarioDao.findBySourceId(sourceId);
	}
}
