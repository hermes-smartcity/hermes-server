package es.udc.lbd.hermes.model.usuario.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.usuario.Usuario;
import es.udc.lbd.hermes.model.usuario.dao.UsuarioDao;


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
		System.out.println(" --------------------------- " +generarHash("cristinacmp1988@gmail.com"));
		return usuarioDao.findBySourceId(sourceId);
	}
	
	private String generarHash(String cadena){
		try {
			cadena = "cristinacmp1988@gmail.com";
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(cadena.getBytes("UTF-8"));

			String sret = "";
			for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xFF & hash[i]);
                if (hex.length() == 1) {
                    sret += "0";
                }
                sret += hex;
            }			
			return sret;
		} catch (Exception e) {
//			logger.error("Excepción xerarHash ",e);
			return null;
		}
	}
	
}
