package es.udc.lbd.hermes.model.events.heartRateData.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.events.heartRateData.HeartRateData;
import es.udc.lbd.hermes.model.events.heartRateData.dao.HeartRateDataDao;
import es.udc.lbd.hermes.model.usuario.Usuario;
import es.udc.lbd.hermes.model.usuario.dao.UsuarioDao;
import es.udc.lbd.hermes.model.usuario.service.UsuarioService;



@Service("heartRateDataService")
@Transactional
public class HeartRateDataServiceImpl implements HeartRateDataService {
	
	@Autowired
	private HeartRateDataDao heartRateDataDao;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioDao usuarioDao;
	
	@Override
	@Transactional(readOnly = true)
	public HeartRateData get(Long id) {
		return heartRateDataDao.get(id);
	}

	@Override
	public void create(HeartRateData heartRateData, String sourceId) {	

		Usuario usuario = usuarioDao.findBySourceId(sourceId);
		//TODO prueba para comprobar hash 256. Luego borrar
		usuario = usuarioService.getBySourceId(sourceId);
		if(usuario == null){
			usuario = new Usuario();
			usuario.setSourceId(sourceId);
			usuarioDao.create(usuario);
		}
		heartRateData.setUsuario(usuario);
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
