package es.udc.lbd.hermes.model.usuario.usuarioWeb.service;

import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.usuario.usuarioWeb.UsuarioWeb;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.dao.UsuarioWebDao;


@Service("usuarioWebService")
@Transactional
public class UsuarioWebServiceImpl implements UsuarioWebService {
	
	@Autowired
	private UsuarioWebDao usuarioWebDao;
	
	private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();
	
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
	
	// ToDO Prueba para login - Chapuza mientras no esté configurado spring/angular
	@Override
	@Transactional(readOnly = true)
	public UsuarioWeb getUser(String email, String passwordEncr) {
		return usuarioWebDao.findUser(email, passwordEncr);
	}
	
	@Override
	@Transactional(readOnly = true)
	public final UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO falta comprobaciones
		final UsuarioWeb usuario = usuarioWebDao.findByEmail(email);
		return checkUser(usuario);
	}

	private UsuarioWeb checkUser(UsuarioWeb usuario) {
		if (usuario == null) {
			throw new UsernameNotFoundException("user not found");
		}
		detailsChecker.check(usuario);
		return usuario;
	}

	@Override
	@Transactional(readOnly = true)
	public final UsuarioWeb findByName(String name) throws UsernameNotFoundException {
		// TODO falta comprobaciones
		return usuarioWebDao.findByEmail(name);
	}
	
	private String generarHash(String cadena){
	
			cadena = "cristinacmp1988";
			String hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
			return hash;
	}
	
}
