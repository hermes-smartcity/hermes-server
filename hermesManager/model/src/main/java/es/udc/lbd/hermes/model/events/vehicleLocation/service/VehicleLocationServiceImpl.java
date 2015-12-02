package es.udc.lbd.hermes.model.events.vehicleLocation.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;
import es.udc.lbd.hermes.model.events.vehicleLocation.dao.VehicleLocationDao;
import es.udc.lbd.hermes.model.usuario.Usuario;
import es.udc.lbd.hermes.model.usuario.dao.UsuarioDao;
import es.udc.lbd.hermes.model.usuario.service.UsuarioService;
import es.udc.lbd.hermes.model.util.HelpersModel;
import es.udc.lbd.hermes.model.util.dao.BloqueElementos;



@Service("vehicleLocationService")
@Transactional
public class VehicleLocationServiceImpl implements VehicleLocationService {
	
	private static final int ELEMENTOS_PAXINA = 100;
	
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
		List<VehicleLocation> vehicleLocations = vehicleLocationDao.obterVehicleLocations(idUsuario, fechaIni, 
				fechaFin, polygon, -1, -1);		
		return vehicleLocations;
	}

	@Transactional(readOnly = true)
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin) {		
		ListaEventosYdias listaEventosDias = new ListaEventosYdias();
		List<String> listaDias = new ArrayList<String>();
		List<Long> listaN = new ArrayList<Long>();
		List<EventosPorDia> ed = vehicleLocationDao.eventosPorDia(idUsuario, fechaIni, fechaFin);
		for(EventosPorDia e:ed){
			listaDias.add(e.getFecha());
			listaN.add(e.getNumeroEventos());
		}
		
		listaEventosDias.setFechas(listaDias);
		listaEventosDias.setnEventos(listaN);
		
		return listaEventosDias;		
	}
	
	@Transactional(readOnly = true)
	public BloqueElementos<VehicleLocation> obterVehicleLocationsPaginados(Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double wnLng, Double wnLat,	Double esLng, Double esLat, int paxina) {

		/*
		 * Obten count+1 vehicleLocations para determinar si existen mais
		 * vehicleLocations no rango especificado.
		 */
		Geometry polygon =  HelpersModel.prepararPoligono(wnLng, wnLat, esLng, esLat);
		List<VehicleLocation> vehicleLocations = vehicleLocationDao.obterVehicleLocations(idUsuario, fechaIni, 
				fechaFin, polygon, ELEMENTOS_PAXINA * (paxina - 1), ELEMENTOS_PAXINA + 1);

		boolean haiMais = vehicleLocations.size() == (ELEMENTOS_PAXINA + 1);

		/*
		 * Borra o ultimo vehicleLocation da lista devolta si existen mais
		 * vehicleLocations no rango especificado
		 */
		if (haiMais) {
			vehicleLocations.remove(vehicleLocations.size() - 1);
		}

		long numero = vehicleLocationDao.contar();
		/* Return BloqueElementos. */
		return new BloqueElementos<VehicleLocation>(vehicleLocations, numero,
				ELEMENTOS_PAXINA, paxina, haiMais);
	}
	
	@Transactional(readOnly = true)
	public long contar(){
		return vehicleLocationDao.contar();
	}
	
}
