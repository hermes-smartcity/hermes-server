package es.udc.lbd.hermes.model.usuario.usuarioWeb.service;

import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.usuario.usuarioWeb.UsuarioWeb;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.dao.UsuarioWebDao;


@Service("usuarioWebService")
@Transactional
public class UsuarioWebServiceImpl implements UsuarioWebService {
	
	@Autowired
	private UsuarioWebDao usuarioWebDao;
	
	@Override
	@Transactional(readOnly = true)
	public UsuarioWeb get(Long id) {
		return usuarioWebDao.get(id);
	}

	@Override
	public void create(UsuarioWeb usuarioWeb) {
		usuarioWebDao.create(usuarioWeb);
		
	}

	@Override
	public void update(UsuarioWeb usuarioWeb) {
		usuarioWebDao.update(usuarioWeb);
	}

	@Override
	public void delete(Long id) {
		UsuarioWeb usuarioWeb = usuarioWebDao.get(id);
		if (usuarioWeb != null) {
			usuarioWebDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<UsuarioWeb> obterUsuariosWeb() {
		List<UsuarioWeb> usuarioWebs = usuarioWebDao.obterUsuariosWeb();
		return usuarioWebs;
	}
	
	@Override
	@Transactional(readOnly = true)
	public UsuarioWeb getBySourceId(String sourceId) {
		return usuarioWebDao.findBySourceId(sourceId);
	}
	
	private String generarHash(String cadena){
	
			cadena = "cristinacmp1988";
			String hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
			return hash;
	}
	
}
