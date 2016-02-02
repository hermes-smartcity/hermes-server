package es.udc.lbd.hermes.model.usuario.usuarioWeb.service;

import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.usuario.exceptions.NoEsPosibleBorrarseASiMismoException;
import es.udc.lbd.hermes.model.usuario.exceptions.NoExiteNingunUsuarioMovilConSourceIdException;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.dao.UsuarioMovilDao;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.Rol;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.UserJSON;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.UsuarioWeb;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.dao.UsuarioWebDao;


@Service("usuarioWebService")
@Transactional
public class UsuarioWebServiceImpl implements UsuarioWebService {
	
	@Autowired
	private UsuarioWebDao usuarioWebDao;
	
	@Autowired
	private UsuarioMovilDao usuarioMovilDao;
	
	
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
	
	@Override
	@Secured({ "ROLE_ADMIN" })
	public void eliminar(Long usuarioId, String email) throws NoEsPosibleBorrarseASiMismoException {

		UsuarioWeb usuario = usuarioWebDao.get(usuarioId);

		if (usuario.getUsername().equals(email)) {
			throw new NoEsPosibleBorrarseASiMismoException();
		}

		usuarioWebDao.delete(usuarioId);
	}

	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN" })
	public List<UsuarioWeb> obterUsuariosWeb() {
		List<UsuarioWeb> usuarioWebs = usuarioWebDao.obterUsuariosWeb();
		return usuarioWebs;
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN" })
	public List<UsuarioWeb> obterUsuariosWebSegunRol(Rol rol){
		List<UsuarioWeb> usuarioWebs = usuarioWebDao.obterUsuariosWebSegunRol(rol);
		return usuarioWebs;
	}
	
	@Override
	@Transactional(readOnly = true)
	public UsuarioWeb getBySourceId(String sourceId) {
		return usuarioWebDao.findBySourceId(sourceId);
	}
	
	@Override
	@Transactional(readOnly = true)
	public final UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
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
	
	@Secured({ "ROLE_ADMIN" })
	public UsuarioWeb registerUser(UserJSON userJSON) throws NoExiteNingunUsuarioMovilConSourceIdException{
		UsuarioWeb usuario = new UsuarioWeb();
		UsuarioMovil usuarioMovil = recuperarUsarioMovilExistente(userJSON.getEmail());
		// Existe un usuario movil con ese email, podemos crear el usuario web y asociarlo
		if(usuarioMovil!=null){			
			usuario.setEmail(userJSON.getEmail());
			usuario.setPassword(generarHashPassword(userJSON.getPassword()));
			usuario.setRol(userJSON.getRol());
			usuario.setActivado(true);
			create(usuario);
			usuario.setUsuarioMovil(usuarioMovil);
		} else throw new NoExiteNingunUsuarioMovilConSourceIdException();
		
		
		return usuario;
	}
	
	@Secured({ "ROLE_ADMIN" })
	public UsuarioWeb updateUser(UserJSON userJSON, Long id){
		UsuarioWeb usuarioWeb = usuarioWebDao.get(id);
		// Mantenemos los valores iniciales del usuario aunque los cambien con inspect del navegador
		usuarioWeb.setEmail(usuarioWeb.getEmail());
		usuarioWeb.setRol(usuarioWeb.getRol());
		usuarioWeb.setActivado(true);
		
		//Modificamos la contraseña
		if(userJSON.getPassword()!=null && !userJSON.getPassword().isEmpty())
			usuarioWeb.setPassword(generarHashPassword(userJSON.getPassword()));
		
		//TODO falta hacer comprobaciones
		// Modificamos id_usuario_movil
		if(userJSON.getSourceIdUsuarioMovilNuevo()!=null && !userJSON.getSourceIdUsuarioMovilNuevo().isEmpty()){
			UsuarioMovil usuarioMovil = usuarioMovilDao.findBySourceId(userJSON.getSourceIdUsuarioMovilNuevo());			
			usuarioWeb.setUsuarioMovil(usuarioMovil);		
		}
		
		usuarioWebDao.update(usuarioWeb);
		
		return usuarioWeb;
	}
	
	
	private String generarHash(String cadena){
			String hash = new String(Hex.encodeHex(DigestUtils.sha256(cadena)));
			return hash;
	}
	
	private UsuarioMovil recuperarUsarioMovilExistente(String email){
		String emailCifrado = generarHash(email);
		return usuarioMovilDao.findBySourceId(emailCifrado);
		
	}
	
	private String generarHashPassword(String cadena){
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); 
		return passwordEncoder.encode(cadena.toString());  
}
	
}
