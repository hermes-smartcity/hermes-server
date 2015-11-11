package es.udc.lbd.hermes.model.events.driverFeatures.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.events.driverFeatures.DriverFeatures;
import es.udc.lbd.hermes.model.events.driverFeatures.dao.DriverFeaturesDao;
import es.udc.lbd.hermes.model.usuario.Usuario;
import es.udc.lbd.hermes.model.usuario.dao.UsuarioDao;


@Service("driverFeaturesService")
@Transactional
public class DriverFeaturesServiceImpl implements DriverFeaturesService {
	
	@Autowired
	private DriverFeaturesDao driverFeaturesDao;
	
	@Autowired
	private UsuarioDao usuarioDao;
	
	@Override
	@Transactional(readOnly = true)
	public DriverFeatures get(Long id) {
		return driverFeaturesDao.get(id);
	}

	@Override
	public void create(DriverFeatures driverFeatures, String sourceId) {	
		Usuario usuario = usuarioDao.findBySourceId(sourceId);
		if(usuario == null){
			usuario = new Usuario();
			usuario.setSourceId(sourceId);
			usuarioDao.create(usuario);
		}		
		driverFeaturesDao.create(driverFeatures);		
	}

	@Override
	public void update(DriverFeatures driverFeatures) {
		driverFeaturesDao.update(driverFeatures);
	}

	@Override
	public void delete(Long id) {
		DriverFeatures driverFeatures = driverFeaturesDao.get(id);
		if (driverFeatures != null) {
			driverFeaturesDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<DriverFeatures> obterDriverFeaturess() {
		List<DriverFeatures> driverFeaturess = driverFeaturesDao.obterDriverFeaturess();
		return driverFeaturess;
	}
	
	@Transactional(readOnly = true)
	public List<DriverFeatures> obterDriverFeaturessSegunUsuario(Long idUsuario) {
		List<DriverFeatures> driverFeaturess = driverFeaturesDao.obterDriverFeaturessSegunUsuario(idUsuario);
		return driverFeaturess;
	}
}
