package es.udc.lbd.hermes.model.events.usersleep.service;

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
import es.udc.lbd.hermes.model.events.ListaUserSleep;
import es.udc.lbd.hermes.model.events.usersleep.UserSleep;
import es.udc.lbd.hermes.model.events.usersleep.UserSleepDTO;
import es.udc.lbd.hermes.model.events.usersleep.dao.UserSleepDao;
import es.udc.lbd.hermes.model.setting.Setting;
import es.udc.lbd.hermes.model.setting.dao.SettingDao;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.dao.UsuarioMovilDao;
import es.udc.lbd.hermes.model.util.SettingsName;

@Service("userSleepService")
@Transactional
public class UserSleepServiceImpl implements UserSleepService{

	@Autowired
	private UserSleepDao userSleepDao;
	
	@Autowired
	private UsuarioMovilDao usuarioMovilDao;
	
	@Autowired
	private SettingDao settingDao;
		
	public UserSleep get(Long id){
		return userSleepDao.get(id);
	}
	
	public void create(UserSleep userSleep, String sourceId){
		UsuarioMovil usuarioMovil = usuarioMovilDao.findBySourceId(sourceId);
		if(usuarioMovil == null){
			usuarioMovil = new UsuarioMovil();
			usuarioMovil.setSourceId(sourceId);
			usuarioMovilDao.create(usuarioMovil);
		}	
		userSleep.setUsuarioMovil(usuarioMovil);
		userSleepDao.create(userSleep);
	}
	
	public void update(UserSleep userSleep){
		userSleepDao.update(userSleep);
	}
	
	public void delete(Long id){
		UserSleep userSleep = userSleepDao.get(id);
		if (userSleep != null) {
			userSleepDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<UserSleep> obterUserSleep(){
		List<UserSleep> userSleep = userSleepDao.obterUserSleep();
		return userSleep;
	}

	@Transactional(readOnly = true)
	public List<UserSleep> obterUserSleepSegunUsuario(Long idUsuario){
		List<UserSleep> userSleep = userSleepDao.obterUserSleepSegunUsuario(idUsuario);
		return userSleep;
	}

	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public long contar(){
		return userSleepDao.contar();
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin) {		
		ListaEventosYdias listaEventosDias = new ListaEventosYdias();
		List<String> listaDias = new ArrayList<String>();
		List<BigInteger> listaN = new ArrayList<BigInteger>();
		List<EventosPorDia> ed = userSleepDao.eventosPorDia(idUsuario, fechaIni, fechaFin);
		for(EventosPorDia e:ed){
			listaDias.add(e.getFecha());
			listaN.add(e.getNeventos());
		}
		
		listaEventosDias.setFechas(listaDias);
		listaEventosDias.setnEventos(listaN);
		
		return listaEventosDias;		
	}
	
	@Transactional(readOnly = true)
	public ListaUserSleep obterUserSleep(Long idUsuario, Calendar fechaIni, Calendar fechaFin){
		
		
		//Recuperamos cuantos resultados devolveria en total
		Long totalResults = userSleepDao.contarUserSleep(idUsuario, fechaIni, fechaFin);
				
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
		
		List<UserSleepDTO> userSleep = userSleepDao.obterUserSleepWithLimit(idUsuario, fechaIni, 
				fechaFin, -1, returnedResults);	
		
		ListaUserSleep listado = new ListaUserSleep(totalResults, returnedResults, userSleep);
		
		return listado;
	}
	
	public void delete(String sourceId, Calendar starttime){
		
		UsuarioMovil usuarioMovil = usuarioMovilDao.findBySourceId(sourceId);
		if(usuarioMovil != null){
			userSleepDao.delete(usuarioMovil.getId(), starttime);
		}	
				
	}
	
}
