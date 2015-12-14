package es.udc.lbd.hermes.model.events.heartRateData.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.events.heartRateData.HeartRateData;
import es.udc.lbd.hermes.model.events.heartRateData.dao.HeartRateDataDao;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.dao.UsuarioMovilDao;



@Service("heartRateDataService")
@Transactional
public class HeartRateDataServiceImpl implements HeartRateDataService {
	
	@Autowired
	private HeartRateDataDao heartRateDataDao;
	
	@Autowired
	private UsuarioMovilDao usuarioMovilDao;
	
	@Override
	@Transactional(readOnly = true)
	public HeartRateData get(Long id) {
		return heartRateDataDao.get(id);
	}

	@Override
	public void create(HeartRateData heartRateData, String sourceId) {	
		UsuarioMovil usuarioMovil = usuarioMovilDao.findBySourceId(sourceId);
		if(usuarioMovil == null){
			usuarioMovil = new UsuarioMovil();
			usuarioMovil.setSourceId(sourceId);
			usuarioMovilDao.create(usuarioMovil);
		}		
		heartRateData.setUsuarioMovil(usuarioMovil);
		heartRateDataDao.create(heartRateData);
		
	}

	@Override
	public void update(HeartRateData heartRateData) {
		heartRateDataDao.update(heartRateData);
	}

	@Override
	public void delete(Long id) {
		HeartRateData heartRateData = heartRateDataDao.get(id);
		if (heartRateData != null) {
			heartRateDataDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<HeartRateData> obterHeartRateData() {
		List<HeartRateData> heartRateData = heartRateDataDao.obterHeartRateData();
		return heartRateData;
	}
	
	@Transactional(readOnly = true)
	public List<HeartRateData> obterHeartRateDataSegunUsuario(Long idUsuario) {
		 List<HeartRateData> heartRateData = heartRateDataDao.obterHeartRateDataSegunUsuario(idUsuario);
		return heartRateData;
	}
}
