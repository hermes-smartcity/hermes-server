package es.udc.lbd.hermes.model.events.stepsData.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.events.stepsData.StepsData;
import es.udc.lbd.hermes.model.events.stepsData.dao.StepsDataDao;
import es.udc.lbd.hermes.model.usuario.Usuario;
import es.udc.lbd.hermes.model.usuario.dao.UsuarioDao;
import es.udc.lbd.hermes.model.usuario.service.UsuarioService;

@Service("stepsDataService")
@Transactional
public class StepsDataServiceImpl implements StepsDataService {
	
	@Autowired
	private StepsDataDao stepsDataDao;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioDao usuarioDao;
	
	@Override
	@Transactional(readOnly = true)
	public StepsData get(Long id) {
		return stepsDataDao.get(id);
	}

	@Override
	public void create(StepsData stepsData, String sourceId) {	

		Usuario usuario = usuarioDao.findBySourceId(sourceId);
		//TODO prueba para comprobar hash 256. Luego borrar
		usuario = usuarioService.getBySourceId(sourceId);
		if(usuario == null){
			usuario = new Usuario();
			usuario.setSourceId(sourceId);
			usuarioDao.create(usuario);
		}
		stepsData.setUsuario(usuario);
		stepsDataDao.create(stepsData);
		
	}

	@Override
	public void update(StepsData stepsData) {
		stepsDataDao.update(stepsData);
	}

	@Override
	public void delete(Long id) {
		StepsData stepsData = stepsDataDao.get(id);
		if (stepsData != null) {
			stepsDataDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<StepsData> obterStepsData() {
		List<StepsData> stepsData = stepsDataDao.obterStepsData();
		return stepsData;
	}
	
	@Transactional(readOnly = true)
	public List<StepsData> obterStepsDataSegunUsuario(Long idUsuario) {
		 List<StepsData> stepsData = stepsDataDao.obterStepsDataSegunUsuario(idUsuario);
		return stepsData;
	}
}
