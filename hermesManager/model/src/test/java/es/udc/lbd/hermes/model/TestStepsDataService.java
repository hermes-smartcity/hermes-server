package es.udc.lbd.hermes.model;

import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.events.stepsData.StepsData;
import es.udc.lbd.hermes.model.events.stepsData.service.StepsDataService;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.dao.UsuarioMovilDao;

@ContextConfiguration(locations = "classpath:application-context-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestStepsDataService {
	
	@Autowired
	private StepsDataService stepsDataService;
	
	@Autowired
	private UsuarioMovilDao usuarioMovilDao;
	
	@Test
    @Transactional
    @Rollback(true)
    public void testCreateStepsData() {
		
		String sourceId = "-1";
		UsuarioMovil usuario = new UsuarioMovil();
		usuario.setSourceId(sourceId);
		usuarioMovilDao.create(usuario);
		
		StepsData stepsData = new StepsData();
		stepsData.setTimeLog(Calendar.getInstance());
		stepsData.setSteps(-1);
		stepsData.setEventId("-1");

		stepsDataService.create(stepsData, sourceId);
		List<StepsData> resultStepsData = stepsDataService.obterStepsDataSegunUsuario(usuario.getId());
		Assert.assertTrue(resultStepsData.size() == 1);
	}

}
