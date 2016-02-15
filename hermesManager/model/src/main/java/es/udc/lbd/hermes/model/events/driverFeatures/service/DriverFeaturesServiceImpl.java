package es.udc.lbd.hermes.model.events.driverFeatures.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.events.driverFeatures.DriverFeatures;
import es.udc.lbd.hermes.model.events.driverFeatures.dao.DriverFeaturesDao;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.dao.UsuarioMovilDao;


@Service("driverFeaturesService")
@Transactional
public class DriverFeaturesServiceImpl implements DriverFeaturesService {
	
	@Autowired
	private DriverFeaturesDao driverFeaturesDao;
	
	@Autowired
	private UsuarioMovilDao usuarioMovilDao;
	
	@Override
	@Transactional(readOnly = true)
	public DriverFeatures get(Long id) {
		return driverFeaturesDao.get(id);
	}

	@Override
	public void create(DriverFeatures driverFeatures, String sourceId) {	
		UsuarioMovil usuarioMovil = usuarioMovilDao.findBySourceId(sourceId);
		if(usuarioMovil == null){
			usuarioMovil = new UsuarioMovil();
			usuarioMovil.setSourceId(sourceId);
			usuarioMovilDao.create(usuarioMovil);
		}		
		driverFeatures.setUsuarioMovil(usuarioMovil);
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
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public List<DriverFeatures> obterDriverFeaturess() {
		List<DriverFeatures> driverFeaturess = driverFeaturesDao.obterDriverFeaturess();
		return driverFeaturess;
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public List<DriverFeatures> obterDriverFeaturessSegunUsuario(Long idUsuario) {
		List<DriverFeatures> driverFeaturess = driverFeaturesDao.obterDriverFeaturessSegunUsuario(idUsuario);
		return driverFeaturess;
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public long contar(){
		return driverFeaturesDao.contar();
	}
}
