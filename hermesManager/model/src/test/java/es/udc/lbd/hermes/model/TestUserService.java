package es.udc.lbd.hermes.model;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.usuario.Usuario;
import es.udc.lbd.hermes.model.usuario.service.UsuarioService;

@ContextConfiguration(locations = "classpath:application-context-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestUserService {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Test
    @Transactional
    @Rollback(true)
    public void testCreateStepsData() {
		
		String sourceId = "-1";
		Usuario usuario = new Usuario();
		usuario.setSourceId(sourceId);
		usuarioService.create(usuario);

		List<Usuario> users = usuarioService.obterUsuarios();		
	}
}
