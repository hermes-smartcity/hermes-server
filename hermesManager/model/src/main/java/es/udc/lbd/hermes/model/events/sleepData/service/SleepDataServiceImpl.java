package es.udc.lbd.hermes.model.events.sleepData.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.sleepData.SleepData;
import es.udc.lbd.hermes.model.events.sleepData.dao.SleepDataDao;
import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;
import es.udc.lbd.hermes.model.usuario.Usuario;
import es.udc.lbd.hermes.model.usuario.dao.UsuarioDao;
import es.udc.lbd.hermes.model.usuario.service.UsuarioService;
import es.udc.lbd.hermes.model.util.HelpersModel;



@Service("sleepDataService")
@Transactional
public class SleepDataServiceImpl implements SleepDataService {
	
	@Autowired
	private SleepDataDao sleepDataDao;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioDao usuarioDao;
	
	@Override
	@Transactional(readOnly = true)
	public SleepData get(Long id) {
		return sleepDataDao.get(id);
	}

	@Override
	public void create(SleepData sleepData, String sourceId) {	

		Usuario usuario = usuarioDao.findBySourceId(sourceId);
		//TODO prueba para comprobar hash 256. Luego borrar
		usuario = usuarioService.getBySourceId(sourceId);
		if(usuario == null){
			usuario = new Usuario();
			usuario.setSourceId(sourceId);
			usuarioDao.create(usuario);
		}
		sleepData.setUsuario(usuario);
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
