package es.udc.lbd.hermes.model.events.vehicleLocation.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.ListaVehicleLocations;
import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;
import es.udc.lbd.hermes.model.events.vehicleLocation.dao.VehicleLocationDao;
import es.udc.lbd.hermes.model.setting.Setting;
import es.udc.lbd.hermes.model.setting.dao.SettingDao;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.dao.UsuarioMovilDao;
import es.udc.lbd.hermes.model.util.HelpersModel;


@Service("vehicleLocationService")
@Transactional
public class VehicleLocationServiceImpl implements VehicleLocationService {
	
	@Autowired
	private VehicleLocationDao vehicleLocationDao;
	
	@Autowired
	private UsuarioMovilDao usuarioMovilDao;
	
	@Autowired
	private SettingDao settingDao;
	
	@Override
	@Transactional(readOnly = true)
	public VehicleLocation get(Long id) {
		return vehicleLocationDao.get(id);
	}

	@Override
	public void create(VehicleLocation vehicleLocation, String sourceId) {	

		UsuarioMovil usuarioMovil = usuarioMovilDao.findBySourceId(sourceId);
		usuarioMovil = usuarioMovilDao.findBySourceId(sourceId);
		if(usuarioMovil == null){
			usuarioMovil = new UsuarioMovil();
			usuarioMovil.setSourceId(sourceId);
			usuarioMovilDao.create(usuarioMovil);
		}
		vehicleLocation.setUsuarioMovil(usuarioMovil);
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
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public ListaVehicleLocations obterVehicleLocations(Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double wnLng, Double wnLat,	Double esLng, Double esLat) {
		Geometry polygon =  HelpersModel.prepararPoligono(wnLng, wnLat, esLng, esLat);
		
		//Recuperamos cuantos resultados devolveria en total
		Long totalResults = vehicleLocationDao.contarVehicleLocations(idUsuario, fechaIni, fechaFin, polygon);
				
		//Tenemos que limiar la consulta a un tamano maximo		
		//Para ello, recuperamos el valor limitQuery
		Setting settingLimit = settingDao.get(new Long(1));
		Integer returnedResults = totalResults.intValue();
		if (settingLimit != null){
			returnedResults = settingLimit.getValueNumber().intValue();
		}
		
		List<VehicleLocation> vehicleLocations = vehicleLocationDao.obterVehicleLocationsWithLimit(idUsuario, fechaIni, 
				fechaFin, polygon, -1, -1, returnedResults);	
		
		ListaVehicleLocations listado = new ListaVehicleLocations(totalResults, returnedResults, vehicleLocations);
		
		return listado;
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin) {		
		ListaEventosYdias listaEventosDias = new ListaEventosYdias();
		List<String> listaDias = new ArrayList<String>();
		List<BigInteger> listaN = new ArrayList<BigInteger>();
		List<EventosPorDia> ed = vehicleLocationDao.eventosPorDia(idUsuario, fechaIni, fechaFin);
		for(EventosPorDia e:ed){
			listaDias.add(e.getFecha());
			listaN.add(e.getNeventos());
		}
		
		listaEventosDias.setFechas(listaDias);
		listaEventosDias.setnEventos(listaN);
		
		return listaEventosDias;		
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public long contar(){
		return vehicleLocationDao.contar();
	}
	
}
