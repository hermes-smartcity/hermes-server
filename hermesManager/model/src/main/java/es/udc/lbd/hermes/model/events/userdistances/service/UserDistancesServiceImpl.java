package es.udc.lbd.hermes.model.events.userdistances.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.ListaUserDistances;
import es.udc.lbd.hermes.model.events.userdistances.UserDistances;
import es.udc.lbd.hermes.model.events.userdistances.UserDistancesDTO;
import es.udc.lbd.hermes.model.events.userdistances.dao.UserDistancesDao;
import es.udc.lbd.hermes.model.setting.Setting;
import es.udc.lbd.hermes.model.setting.dao.SettingDao;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.dao.UsuarioMovilDao;
import es.udc.lbd.hermes.model.util.SettingsName;

@Service("userDistancesService")
@Transactional
public class UserDistancesServiceImpl implements UserDistancesService{

	@Autowired
	private UserDistancesDao userDistancesDao;
	
	@Autowired
	private UsuarioMovilDao usuarioMovilDao;
	
	@Autowired
	private SettingDao settingDao;
		
	public UserDistances get(Long id){
		return userDistancesDao.get(id);
	}
	
	public void create(UserDistances userDistances, String sourceId){
		UsuarioMovil usuarioMovil = usuarioMovilDao.findBySourceId(sourceId);
		if(usuarioMovil == null){
			usuarioMovil = new UsuarioMovil();
			usuarioMovil.setSourceId(sourceId);
			usuarioMovilDao.create(usuarioMovil);
		}	
		userDistances.setUsuarioMovil(usuarioMovil);
		userDistancesDao.create(userDistances);
	}
	
	public void update(UserDistances userDistances){
		userDistancesDao.update(userDistances);
	}
	
	public void delete(Long id){
		UserDistances userDistances = userDistancesDao.get(id);
		if (userDistances != null) {
			userDistancesDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<UserDistances> obterUserDistances(){
		List<UserDistances> userDistances = userDistancesDao.obterUserDistances();
		return userDistances;
	}

	@Transactional(readOnly = true)
	public List<UserDistances> obterUserDistancesSegunUsuario(Long idUsuario){
		List<UserDistances> userDistances = userDistancesDao.obterUserDistancesSegunUsuario(idUsuario);
		return userDistances;
	}

	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public long contar(){
		return userDistancesDao.contar();
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin) {		
		ListaEventosYdias listaEventosDias = new ListaEventosYdias();
		List<String> listaDias = new ArrayList<String>();
		List<BigInteger> listaN = new ArrayList<BigInteger>();
		List<EventosPorDia> ed = userDistancesDao.eventosPorDia(idUsuario, fechaIni, fechaFin);
		for(EventosPorDia e:ed){
			listaDias.add(e.getFecha());
			listaN.add(e.getNeventos());
		}
		
		listaEventosDias.setFechas(listaDias);
		listaEventosDias.setnEventos(listaN);
		
		return listaEventosDias;		
	}
	
	@Transactional(readOnly = true)
	public ListaUserDistances obterUserDistances(Long idUsuario, Calendar fechaIni, Calendar fechaFin){
				
		//Recuperamos cuantos resultados devolveria en total
		Long totalResults = userDistancesDao.contarUserDistances(idUsuario, fechaIni, fechaFin);
				
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
		
		List<UserDistancesDTO> userDistances = userDistancesDao.obterUserDistancesWithLimit(idUsuario, fechaIni, 
				fechaFin, -1, returnedResults);	
		
		ListaUserDistances listado = new ListaUserDistances(totalResults, returnedResults, userDistances);
		
		return listado;
	}
	
	public void delete(String sourceId, Calendar starttime){
		
		UsuarioMovil usuarioMovil = usuarioMovilDao.findBySourceId(sourceId);
		if(usuarioMovil != null){
			userDistancesDao.delete(usuarioMovil.getId(), starttime);
		}	
				
	}
	
}
