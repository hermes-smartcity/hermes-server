package es.udc.lbd.hermes.model.usuario.usuarioWeb.service;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.mail.EmailService;
import es.udc.lbd.hermes.model.usuario.exceptions.ActivarCuentaException;
import es.udc.lbd.hermes.model.usuario.exceptions.EnlaceCaducadoException;
import es.udc.lbd.hermes.model.usuario.exceptions.NoEsPosibleBorrarseASiMismoException;
import es.udc.lbd.hermes.model.usuario.exceptions.NoExiteNingunUsuarioMovilConSourceIdException;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.dao.UsuarioMovilDao;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.PasswordJSON;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.Rol;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.UserJSON;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.UsuarioWeb;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.dao.UsuarioWebDao;
import es.udc.lbd.hermes.model.util.HashUtil;
import es.udc.lbd.hermes.model.util.ReadPropertiesFile;
import es.udc.lbd.hermes.model.util.UserUtils;
import es.udc.lbd.hermes.model.util.VerificationToken;
import es.udc.lbd.hermes.model.util.exceptions.DuplicateEmailException;


@Service("usuarioWebService")
@Transactional
public class UsuarioWebServiceImpl implements UsuarioWebService {
	
	@Autowired
	private UsuarioWebDao usuarioWebDao;
	
	@Autowired
	private UsuarioMovilDao usuarioMovilDao;
	
	@Autowired 
	private EmailService emailService;
	
	@Autowired
	public MessageSource messageSource;
	
	public static final long MILISEGUNDOS_EXPIRACION_ENLACE_ACTIVACION = 1000 * 60 * 30 ;
	private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();
	
	@Override
	@Transactional(readOnly = true)
	public UsuarioWeb get(Long id) {
		return usuarioWebDao.get(id);
	}

	@Override
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public void create(UsuarioWeb usuarioWeb) {
		usuarioWebDao.create(usuarioWeb);
		
	}

	@Override
	@Secured({ "ROLE_ADMIN" })
	public void update(UsuarioWeb usuarioWeb) {
		usuarioWebDao.update(usuarioWeb);
	}

	@Override
	@Secured({ "ROLE_ADMIN" })
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
	

	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public UsuarioWeb registerUser(UserJSON userJSON, Locale locale, boolean isAdmin) throws NoExiteNingunUsuarioMovilConSourceIdException, DuplicateEmailException{
		UsuarioWeb usuario = new UsuarioWeb();
		UsuarioMovil usuarioMovil = recuperarUsarioMovilExistente(userJSON.getEmail());
		if (usuarioWebDao.findByEmail(userJSON.getEmail()) != null) {
			throw new DuplicateEmailException(userJSON.getEmail());
		}
		// Existe un usuario movil con ese email, podemos crear el usuario web y asociarlo
		if(usuarioMovil!=null){			
			usuario.setEmail(userJSON.getEmail());
			usuario.setPassword(HashUtil.generarHashPassword(userJSON.getPassword()));
			
			if(isAdmin)
				usuario.setRol(Rol.ROLE_ADMIN);
			else usuario.setRol(Rol.ROLE_CONSULTA);
			// Hasta que el propio usuario confirme el registro con el enlace de activación permacerá disabled
			usuario.setActivado(false);
			create(usuario);
			usuario.setUsuarioMovil(usuarioMovil);
			// Enviamos un mail para activar el usuario
			enviarMail(usuario, locale);
		} else throw new NoExiteNingunUsuarioMovilConSourceIdException();
		
		
		return usuario;
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public long contar(){
		return usuarioWebDao.contar();
	}
	
	@Secured({ "ROLE_ADMIN" })
	public UsuarioWeb updateUser(UserJSON userJSON, Long id){
		UsuarioWeb usuarioWeb = usuarioWebDao.get(id);
		// Mantenemos los valores iniciales del usuario aunque los cambien con inspect del navegador
		usuarioWeb.setEmail(usuarioWeb.getEmail());
		usuarioWeb.setRol(usuarioWeb.getRol());
		usuarioWeb.setActivado(true);
		
		// Modificamos la contraseña
		if(userJSON.getPassword()!=null && !userJSON.getPassword().isEmpty())
			usuarioWeb.setPassword(HashUtil.generarHashPassword(userJSON.getPassword()));
		
		// Modificamos id_usuario_movil
		if(userJSON.getSourceIdUsuarioMovilNuevo()!=null && !userJSON.getSourceIdUsuarioMovilNuevo().isEmpty()){
			UsuarioMovil usuarioMovil = usuarioMovilDao.findBySourceId(userJSON.getSourceIdUsuarioMovilNuevo());			
			usuarioWeb.setUsuarioMovil(usuarioMovil);		
		}
		
		usuarioWebDao.update(usuarioWeb);
		
		return usuarioWeb;
	}
	

	@Override
	public void activarCuenta(String email, String codigo) throws ActivarCuentaException, EnlaceCaducadoException {

		String emailNormalizado = UserUtils.normalizarLogin(email);

		UsuarioWeb usuario = usuarioWebDao.findByEmail(email);

		VerificationToken verificationToken = new VerificationToken(codigo);
		if (usuario == null || !emailNormalizado.equals(verificationToken.getEmail())) {
			throw new ActivarCuentaException();
		}

		if (verificationToken.getFechaExpiracion().before(Calendar.getInstance())) {
			throw new EnlaceCaducadoException();
		}

		usuario.setActivado(true);
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public void changePassword(PasswordJSON passwordJSON, UsuarioWeb usuario){
		//Comprobamos que la contrasena vieja indicada corresponda con la de base de datos
		usuario.setPassword(HashUtil.generarHashPassword(passwordJSON.getPasswordNew1()));
		usuarioWebDao.update(usuario);
	}
		
	private UsuarioMovil recuperarUsarioMovilExistente(String email){
		String emailCifrado = HashUtil.generarHash(email);
		return usuarioMovilDao.findBySourceId(emailCifrado);
		
	}
		
	private void enviarMail(UsuarioWeb usuarioWeb, Locale locale){
		//TODO falta normalizar login
//		String emailNormalizado = UserUtils.normalizarLogin(usuarioWeb.getEmail());
		
		String urlActivacion = ReadPropertiesFile.getUrlViewLayer()+"activarCuenta/email/"+
		 
			 usuarioWeb.getEmail() +"/hash/"
				+ generarCodigoActivacion(usuarioWeb.getEmail(), MILISEGUNDOS_EXPIRACION_ENLACE_ACTIVACION);

		Object [] parametros = new Object[] {usuarioWeb.getEmail(), "Hermes Manager ", urlActivacion};

		String mensaje = messageSource.getMessage(
				"eventManager.usuario.pagina.registrar.activacionEmail.texto", parametros, locale);
		String mensajeHTML = messageSource.getMessage(
				"eventManager.usuario.pagina.registrar.activacionEmail.html", parametros, locale);
			String asunto = "Activación cuenta Hermes Manager";
		
		
		emailService.enviarCorreo(usuarioWeb.getEmail(), asunto, mensaje, mensajeHTML);
	}
	

	private String generarCodigoActivacion(String email, long milisegundosExpiracion) {
		VerificationToken verificationToken = new VerificationToken(email, milisegundosExpiracion);
		return verificationToken.getCodigo();
	}

	
}
