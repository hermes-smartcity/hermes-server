package es.udc.lbd.hermes.model.events.usersteps.service;

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
import es.udc.lbd.hermes.model.events.ListaUserSteps;
import es.udc.lbd.hermes.model.events.usersteps.UserSteps;
import es.udc.lbd.hermes.model.events.usersteps.UserStepsDTO;
import es.udc.lbd.hermes.model.events.usersteps.dao.UserStepsDao;
import es.udc.lbd.hermes.model.setting.Setting;
import es.udc.lbd.hermes.model.setting.dao.SettingDao;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.dao.UsuarioMovilDao;
import es.udc.lbd.hermes.model.util.SettingsName;

@Service("userStepsService")
@Transactional
public class UserStepsServiceImpl implements UserStepsService{

	@Autowired
	private UserStepsDao userStepsDao;
	
	@Autowired
	private UsuarioMovilDao usuarioMovilDao;
	
	@Autowired
	private SettingDao settingDao;
		
	public UserSteps get(Long id){
		return userStepsDao.get(id);
	}
	
	public void create(UserSteps userSteps, String sourceId){
		UsuarioMovil usuarioMovil = usuarioMovilDao.findBySourceId(sourceId);
		if(usuarioMovil == null){
			usuarioMovil = new UsuarioMovil();
			usuarioMovil.setSourceId(sourceId);
			usuarioMovilDao.create(usuarioMovil);
		}	
		userSteps.setUsuarioMovil(usuarioMovil);
		userStepsDao.create(userSteps);
	}
	
	public void update(UserSteps userSteps){
		userStepsDao.update(userSteps);
	}
	
	public void delete(Long id){
		UserSteps userSteps = userStepsDao.get(id);
		if (userSteps != null) {
			userStepsDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<UserSteps> obterUserSteps(){
		List<UserSteps> userSteps = userStepsDao.obterUserSteps();
		return userSteps;
	}

	@Transactional(readOnly = true)
	public List<UserSteps> obterUserStepsSegunUsuario(Long idUsuario){
		List<UserSteps> userSteps = userStepsDao.obterUserStepsSegunUsuario(idUsuario);
		return userSteps;
	}

	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public long contar(){
		return userStepsDao.contar();
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin) {		
		ListaEventosYdias listaEventosDias = new ListaEventosYdias();
		List<String> listaDias = new ArrayList<String>();
		List<BigInteger> listaN = new ArrayList<BigInteger>();
		List<EventosPorDia> ed = userStepsDao.eventosPorDia(idUsuario, fechaIni, fechaFin);
		for(EventosPorDia e:ed){
			listaDias.add(e.getFecha());
			listaN.add(e.getNeventos());
		}
		
		listaEventosDias.setFechas(listaDias);
		listaEventosDias.setnEventos(listaN);
		
		return listaEventosDias;		
	}
	
	@Transactional(readOnly = true)
	public ListaUserSteps obterUserSteps(Long idUsuario, Calendar fechaIni, Calendar fechaFin){
		
		
		//Recuperamos cuantos resultados devolveria en total
		Long totalResults = userStepsDao.contarUserSteps(idUsuario, fechaIni, fechaFin);
				
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
		
		List<UserStepsDTO> userSteps = userStepsDao.obterUserStepsWithLimit(idUsuario, fechaIni, 
				fechaFin, -1, returnedResults);	
		
		ListaUserSteps listado = new ListaUserSteps(totalResults, returnedResults, userSteps);
		
		return listado;
	}
	
	public void delete(String sourceId, Calendar starttime){
		
		UsuarioMovil usuarioMovil = usuarioMovilDao.findBySourceId(sourceId);
		if(usuarioMovil != null){
			userStepsDao.delete(usuarioMovil.getId(), starttime);
		}	
				
	}
	
}
