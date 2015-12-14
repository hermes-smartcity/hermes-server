package es.udc.lbd.hermes.model.events.sleepData.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import es.udc.lbd.hermes.model.events.sleepData.SleepData;
import es.udc.lbd.hermes.model.events.sleepData.dao.SleepDataDao;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.dao.UsuarioMovilDao;



@Service("sleepDataService")
@Transactional
public class SleepDataServiceImpl implements SleepDataService {
	
	@Autowired
	private SleepDataDao sleepDataDao;

	@Autowired
	private UsuarioMovilDao usuarioMovilDao;
	
	@Override
	@Transactional(readOnly = true)
	public SleepData get(Long id) {
		return sleepDataDao.get(id);
	}

	@Override
	public void create(SleepData sleepData, String sourceId) {	

		UsuarioMovil usuarioMovil = usuarioMovilDao.findBySourceId(sourceId);
		if(usuarioMovil == null){
			usuarioMovil = new UsuarioMovil();
			usuarioMovil.setSourceId(sourceId);
			usuarioMovilDao.create(usuarioMovil);
		}
		sleepData.setUsuarioMovil(usuarioMovil);
		sleepDataDao.create(sleepData);
		
	}

	@Override
	public void update(SleepData sleepData) {
		sleepDataDao.update(sleepData);
	}

	@Override
	public void delete(Long id) {
		SleepData sleepData = sleepDataDao.get(id);
		if (sleepData != null) {
			sleepDataDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<SleepData> obterSleepData() {
		List<SleepData> sleepData = sleepDataDao.obterSleepData();
		return sleepData;
	}
	
	@Transactional(readOnly = true)
	public List<SleepData> obterSleepDataSegunUsuario(Long idUsuario) {
		 List<SleepData> sleepData = sleepDataDao.obterSleepDataSegunUsuario(idUsuario);
		return sleepData;
	}
}
