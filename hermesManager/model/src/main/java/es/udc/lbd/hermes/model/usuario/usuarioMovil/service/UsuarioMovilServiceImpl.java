package es.udc.lbd.hermes.model.usuario.usuarioMovil.service;

import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.dao.UsuarioMovilDao;


@Service("usuarioMovilService")
@Transactional
public class UsuarioMovilServiceImpl implements UsuarioMovilService {
	
	@Autowired
	private UsuarioMovilDao usuarioMovilDao;
	
	@Override
	@Transactional(readOnly = true)
	public UsuarioMovil get(Long id) {
		return usuarioMovilDao.get(id);
	}

	@Override
	public void create(UsuarioMovil usuarioMovil) {
		usuarioMovilDao.create(usuarioMovil);
		
	}

	@Override
	public void update(UsuarioMovil usuarioMovil) {
		usuarioMovilDao.update(usuarioMovil);
	}

	@Override
	public void delete(Long id) {
		UsuarioMovil usuarioMovil = usuarioMovilDao.get(id);
		if (usuarioMovil != null) {
			usuarioMovilDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<UsuarioMovil> obterUsuariosMovil() {
		List<UsuarioMovil> usuarioMovils = usuarioMovilDao.obterUsuariosMovil();
		return usuarioMovils;
	}
	
	@Override
	@Transactional(readOnly = true)
	public UsuarioMovil getBySourceId(String sourceId) {
		return usuarioMovilDao.findBySourceId(sourceId);
	}
	

	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public long contar(){
		return usuarioMovilDao.contar();
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public long getNumberActiveUsers(){
		return usuarioMovilDao.getNumberActiveUsers();
	}
	
	
	private String generarHash(String cadena){	
			String hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
			return hash;
	}
	
}
