package es.udc.lbd.hermes.model.events.userlocations.service;

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
import es.udc.lbd.hermes.model.events.GroupedDTO;
import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.ListaUserLocations;
import es.udc.lbd.hermes.model.events.userlocations.UserLocationDTO;
import es.udc.lbd.hermes.model.events.userlocations.UserLocations;
import es.udc.lbd.hermes.model.events.userlocations.dao.UserLocationsDao;
import es.udc.lbd.hermes.model.setting.Setting;
import es.udc.lbd.hermes.model.setting.dao.SettingDao;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.dao.UsuarioMovilDao;
import es.udc.lbd.hermes.model.util.HelpersModel;
import es.udc.lbd.hermes.model.util.SettingsName;

@Service("userLocationsService")
@Transactional
public class UserLocationsServiceImpl implements UserLocationsService{

	@Autowired
	private UserLocationsDao userLocationsDao;
	
	@Autowired
	private UsuarioMovilDao usuarioMovilDao;
	
	@Autowired
	private SettingDao settingDao;
	
	public UserLocations get(Long id){
		return userLocationsDao.get(id);
	}
	
	public void create(UserLocations userLocations, String sourceId){
		UsuarioMovil usuarioMovil = usuarioMovilDao.findBySourceId(sourceId);
		if(usuarioMovil == null){
			usuarioMovil = new UsuarioMovil();
			usuarioMovil.setSourceId(sourceId);
			usuarioMovilDao.create(usuarioMovil);
		}	
		userLocations.setUsuarioMovil(usuarioMovil);
		userLocationsDao.create(userLocations);
	}
	
	public void update(UserLocations userLocations){
		userLocationsDao.update(userLocations);
	}
	
	public void delete(Long id){
		UserLocations userLocations = userLocationsDao.get(id);
		if (userLocations != null) {
			userLocationsDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<UserLocations> obterUserLocations(){
		List<UserLocations> userLocationd = userLocationsDao.obterUserLocations();
		return userLocationd;
	}

	@Transactional(readOnly = true)
	public List<UserLocations> obterUserLocationsSegunUsuario(Long idUsuario){
		List<UserLocations> userLocations = userLocationsDao.obterUserLocationsSegunUsuario(idUsuario);
		return userLocations;
	}

	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public long contar(){
		return userLocationsDao.contar();
	}
	
	@Transactional(readOnly = true)
	public ListaUserLocations obterUserLocations(Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double wnLng, Double wnLat,	Double esLng, Double esLat){
		
		Geometry polygon =  HelpersModel.prepararPoligono(wnLng, wnLat, esLng, esLat);
		
		//Recuperamos cuantos resultados devolveria en total
		Long totalResults = userLocationsDao.contarUserLocations(idUsuario, fechaIni, fechaFin, polygon);
				
		//Tenemos que limitar la consulta a un tamano maximo		
		//Para ello, recuperamos el valor limitQuery
		Setting settingLimit = settingDao.getByName(SettingsName.LIMITQUERY);
		Integer returnedResults = null;
		if (settingLimit != null){
			returnedResults = settingLimit.getValueNumber().intValue();
		}

		//Si el total de resultados es menor que el limite, establecemos el limite a ese valor
		if (returnedResults != null){
			if (totalResults.intValue() < returnedResults){
				returnedResults = totalResults.intValue();
			}
		}else{
			returnedResults = totalResults.intValue();
		}
		
		List<UserLocationDTO> userLocations = userLocationsDao.obterUserLocationsWithLimit(idUsuario, fechaIni, 
				fechaFin, polygon, -1, returnedResults);	
		
		ListaUserLocations listado = new ListaUserLocations(totalResults, returnedResults, userLocations);
		
		return listado;
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin) {		
		ListaEventosYdias listaEventosDias = new ListaEventosYdias();
		List<String> listaDias = new ArrayList<String>();
		List<BigInteger> listaN = new ArrayList<BigInteger>();
		List<EventosPorDia> ed = userLocationsDao.eventosPorDia(idUsuario, fechaIni, fechaFin);
		for(EventosPorDia e:ed){
			listaDias.add(e.getFecha());
			listaN.add(e.getNeventos());
		}
		
		listaEventosDias.setFechas(listaDias);
		listaEventosDias.setnEventos(listaN);
		
		return listaEventosDias;		
	}
	
	@Transactional(readOnly = true)
	public List<GroupedDTO> obterUserLocationsGrouped(Long idUsuario, Calendar fechaIni,Calendar fechaFin, Double wnLng, Double wnLat,	Double esLng, Double esLat,int startIndex){
		
		Geometry bounds =  HelpersModel.prepararPoligono(wnLng, wnLat, esLng, esLat);
		
		//Recuperamos el numero de celdas
		Setting settingNumberOfCells  = settingDao.getByName(SettingsName.NUMBEROFCELLS);
		Integer numberOfCells = 5;
		if (settingNumberOfCells != null){
			numberOfCells = settingNumberOfCells.getValueNumber().intValue();
		}

		return userLocationsDao.obterUserLocationsGrouped(idUsuario, fechaIni, fechaFin, bounds, startIndex, numberOfCells);
	}
}
