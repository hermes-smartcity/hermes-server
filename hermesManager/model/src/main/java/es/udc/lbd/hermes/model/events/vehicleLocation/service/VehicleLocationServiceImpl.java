package es.udc.lbd.hermes.model.events.vehicleLocation.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;
import es.udc.lbd.hermes.model.events.vehicleLocation.dao.VehicleLocationDao;
import es.udc.lbd.hermes.model.usuario.Usuario;
import es.udc.lbd.hermes.model.usuario.dao.UsuarioDao;
import es.udc.lbd.hermes.model.usuario.service.UsuarioService;
import es.udc.lbd.hermes.model.util.HelpersModel;



@Service("vehicleLocationService")
@Transactional
public class VehicleLocationServiceImpl implements VehicleLocationService {
	
	@Autowired
	private VehicleLocationDao vehicleLocationDao;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioDao usuarioDao;
	
	@Override
	@Transactional(readOnly = true)
	public VehicleLocation get(Long id) {
		return vehicleLocationDao.get(id);
	}

	@Override
	public void create(VehicleLocation vehicleLocation, String sourceId) {	

		Usuario usuario = usuarioDao.findBySourceId(sourceId);
		//TODO prueba para comprobar hash 256. Luego borrar
		usuario = usuarioService.getBySourceId(sourceId);
		if(usuario == null){
			usuario = new Usuario();
			usuario.setSourceId(sourceId);
			usuarioDao.create(usuario);
		}
		vehicleLocation.setUsuario(usuario);
		vehicleLocationDao.create(vehicleLocation);
		
	}

	@Override
	public void update(VehicleLocation vehicleLocation) {
		vehicleLocationDao.update(vehicleLocation);
	}

	@Override
	public void delete(Long id) {
		VehicleLocation vehicleLocation = vehicleLocationDao.get(id);
		if (vehicleLocation != null) {
			vehicleLocationDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<VehicleLocation> obterVehicleLocations(Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double wnLng, Double wnLat,	Double esLng, Double esLat) {
		Geometry polygon =  HelpersModel.prepararPoligono(wnLng, wnLat, esLng, esLat);
		List<VehicleLocation> vehicleLocations = vehicleLocationDao.obterVehicleLocations(idUsuario, fechaIni, fechaFin, polygon);
		return vehicleLocations;
	}
	
}
