package es.udc.lbd.hermes.model.events.usercaloriesexpended.service;

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
import es.udc.lbd.hermes.model.events.ListaUserCaloriesExpended;
import es.udc.lbd.hermes.model.events.usercaloriesexpended.UserCaloriesExpended;
import es.udc.lbd.hermes.model.events.usercaloriesexpended.UserCaloriesExpendedDTO;
import es.udc.lbd.hermes.model.events.usercaloriesexpended.dao.UserCaloriesExpendedDao;
import es.udc.lbd.hermes.model.setting.Setting;
import es.udc.lbd.hermes.model.setting.dao.SettingDao;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.dao.UsuarioMovilDao;
import es.udc.lbd.hermes.model.util.SettingsName;

@Service("userCaloriesExpendedService")
@Transactional
public class UserCaloriesExpendedServiceImpl implements UserCaloriesExpendedService{

	@Autowired
	private UserCaloriesExpendedDao userCaloriesExpendedDao;
	
	@Autowired
	private UsuarioMovilDao usuarioMovilDao;
	
	@Autowired
	private SettingDao settingDao;
		
	public UserCaloriesExpended get(Long id){
		return userCaloriesExpendedDao.get(id);
	}
	
	public void create(UserCaloriesExpended userCaloriesExpended, String sourceId){
		UsuarioMovil usuarioMovil = usuarioMovilDao.findBySourceId(sourceId);
		if(usuarioMovil == null){
			usuarioMovil = new UsuarioMovil();
			usuarioMovil.setSourceId(sourceId);
			usuarioMovilDao.create(usuarioMovil);
		}	
		userCaloriesExpended.setUsuarioMovil(usuarioMovil);
		userCaloriesExpendedDao.create(userCaloriesExpended);
	}
	
	public void update(UserCaloriesExpended userCaloriesExpended){
		userCaloriesExpendedDao.update(userCaloriesExpended);
	}
	
	public void delete(Long id){
		UserCaloriesExpended userCaloriesExpended = userCaloriesExpendedDao.get(id);
		if (userCaloriesExpended != null) {
			userCaloriesExpendedDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<UserCaloriesExpended> obterUserCaloriesExpended(){
		List<UserCaloriesExpended> userCaloriesExpended = userCaloriesExpendedDao.obterUserCaloriesExpended();
		return userCaloriesExpended;
	}

	@Transactional(readOnly = true)
	public List<UserCaloriesExpended> obterUserCaloriesExpendedSegunUsuario(Long idUsuario){
		List<UserCaloriesExpended> userCaloriesExpended = userCaloriesExpendedDao.obterUserCaloriesExpendedSegunUsuario(idUsuario);
		return userCaloriesExpended;
	}

	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public long contar(){
		return userCaloriesExpendedDao.contar();
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin) {		
		ListaEventosYdias listaEventosDias = new ListaEventosYdias();
		List<String> listaDias = new ArrayList<String>();
		List<BigInteger> listaN = new ArrayList<BigInteger>();
		List<EventosPorDia> ed = userCaloriesExpendedDao.eventosPorDia(idUsuario, fechaIni, fechaFin);
		for(EventosPorDia e:ed){
			listaDias.add(e.getFecha());
			listaN.add(e.getNeventos());
		}
		
		listaEventosDias.setFechas(listaDias);
		listaEventosDias.setnEventos(listaN);
		
		return listaEventosDias;		
	}
	
	@Transactional(readOnly = true)
	public ListaUserCaloriesExpended obterUserCaloriesExpended(Long idUsuario, Calendar fechaIni, Calendar fechaFin){
		
		
		//Recuperamos cuantos resultados devolveria en total
		Long totalResults = userCaloriesExpendedDao.contarUserCaloriesExpended(idUsuario, fechaIni, fechaFin);
				
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
		
		List<UserCaloriesExpendedDTO> userCaloriesExpended = userCaloriesExpendedDao.obterUserCaloriesExpendedWithLimit(idUsuario, fechaIni, 
				fechaFin, -1, returnedResults);	
		
		ListaUserCaloriesExpended listado = new ListaUserCaloriesExpended(totalResults, returnedResults, userCaloriesExpended);
		
		return listado;
	}
	
	public void delete(String sourceId, Calendar starttime){
		
		UsuarioMovil usuarioMovil = usuarioMovilDao.findBySourceId(sourceId);
		if(usuarioMovil != null){
			userCaloriesExpendedDao.delete(usuarioMovil.getId(), starttime);
		}	
				
	}
	
}
