package es.udc.lbd.hermes.model.events.contextData.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.events.contextData.ContextData;
import es.udc.lbd.hermes.model.events.contextData.dao.ContextDataDao;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.dao.UsuarioMovilDao;

@Service("contextDataService")
@Transactional
public class ContextDataServiceImpl implements ContextDataService {
	
	@Autowired
	private ContextDataDao contextDataDao;
	
	@Autowired
	private UsuarioMovilDao usuarioMovilDao;
	
	@Override
	@Transactional(readOnly = true)
	public ContextData get(Long id) {
		return contextDataDao.get(id);
	}

	@Override
	public void create(ContextData contextData, String sourceId) {	

		UsuarioMovil usuarioMovil = usuarioMovilDao.findBySourceId(sourceId);
		if(usuarioMovil == null){
			usuarioMovil = new UsuarioMovil();
			usuarioMovil.setSourceId(sourceId);
			usuarioMovilDao.create(usuarioMovil);
		}	
		contextData.setUsuarioMovil(usuarioMovil);
		contextDataDao.create(contextData);
		
	}

	@Override
	public void update(ContextData contextData) {
		contextDataDao.update(contextData);
	}

	@Override
	public void delete(Long id) {
		ContextData contextData = contextDataDao.get(id);
		if (contextData != null) {
			contextDataDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<ContextData> obterContextData() {
		List<ContextData> contextData = contextDataDao.obterContextData();
		return contextData;
	}
	
	@Transactional(readOnly = true)
	public List<ContextData> obterContextDataSegunUsuario(Long idUsuario) {
		 List<ContextData> contextData = contextDataDao.obterContextDataSegunUsuario(idUsuario);
		return contextData;
	}

}
