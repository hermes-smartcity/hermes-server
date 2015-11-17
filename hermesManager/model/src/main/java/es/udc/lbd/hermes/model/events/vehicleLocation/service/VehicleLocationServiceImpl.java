package es.udc.lbd.hermes.model.events.vehicleLocation.service;

import java.util.ArrayList;
import java.util.List;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.hibernate.ejb.criteria.ParameterContainer.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

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
	public List<VehicleLocation> obterVehicleLocations() {
		List<VehicleLocation> vehicleLocations = vehicleLocationDao.obterVehicleLocations();
		return vehicleLocations;
	}
	
	@Transactional(readOnly = true)
	public List<VehicleLocation> obterVehicleLocationsByBounds(Double wnLng, Double wnLat, Double esLng, Double esLat) {
		List<VehicleLocation> vehicleLocations = new ArrayList<>();
			Geometry polygon =  HelpersModel.prepararPoligono(wnLng, wnLat, esLng, esLat);
			vehicleLocations = vehicleLocationDao.obterVehicleLocationsByBounds(polygon);
		
		return vehicleLocations;
	}
	
	@Transactional(readOnly = true)
	public List<VehicleLocation> obterVehicleLocationsSegunUsuario(Long idUsuario) {
		 List<VehicleLocation> vehicleLocations = vehicleLocationDao.obterVehicleLocationsSegunUsuario(idUsuario);
		return vehicleLocations;
	}
}
