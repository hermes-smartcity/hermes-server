package es.udc.lbd.hermes.model.events.userheartrates.service;

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
import es.udc.lbd.hermes.model.events.ListaUserHeartRates;
import es.udc.lbd.hermes.model.events.userheartrates.UserHeartRates;
import es.udc.lbd.hermes.model.events.userheartrates.UserHeartRatesDTO;
import es.udc.lbd.hermes.model.events.userheartrates.dao.UserHeartRatesDao;
import es.udc.lbd.hermes.model.setting.Setting;
import es.udc.lbd.hermes.model.setting.dao.SettingDao;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.dao.UsuarioMovilDao;
import es.udc.lbd.hermes.model.util.SettingsName;

@Service("userHeartRatesService")
@Transactional
public class UserHeartRatesServiceImpl implements UserHeartRatesService{

	@Autowired
	private UserHeartRatesDao userHeartRatesDao;
	
	@Autowired
	private UsuarioMovilDao usuarioMovilDao;
	
	@Autowired
	private SettingDao settingDao;
		
	public UserHeartRates get(Long id){
		return userHeartRatesDao.get(id);
	}
	
	public void create(UserHeartRates userHeartRates, String sourceId){
		UsuarioMovil usuarioMovil = usuarioMovilDao.findBySourceId(sourceId);
		if(usuarioMovil == null){
			usuarioMovil = new UsuarioMovil();
			usuarioMovil.setSourceId(sourceId);
			usuarioMovilDao.create(usuarioMovil);
		}	
		userHeartRates.setUsuarioMovil(usuarioMovil);
		userHeartRatesDao.create(userHeartRates);
	}
	
	public void update(UserHeartRates userHeartRates){
		userHeartRatesDao.update(userHeartRates);
	}
	
	public void delete(Long id){
		UserHeartRates userHeartRates = userHeartRatesDao.get(id);
		if (userHeartRates != null) {
			userHeartRatesDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<UserHeartRates> obterUserHeartRates(){
		List<UserHeartRates> userHeartRates = userHeartRatesDao.obterUserHeartRates();
		return userHeartRates;
	}

	@Transactional(readOnly = true)
	public List<UserHeartRates> obterUserHeartRatesSegunUsuario(Long idUsuario){
		List<UserHeartRates> userHeartRates = userHeartRatesDao.obterUserHeartRatesSegunUsuario(idUsuario);
		return userHeartRates;
	}

	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public long contar(){
		return userHeartRatesDao.contar();
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin) {		
		ListaEventosYdias listaEventosDias = new ListaEventosYdias();
		List<String> listaDias = new ArrayList<String>();
		List<BigInteger> listaN = new ArrayList<BigInteger>();
		List<EventosPorDia> ed = userHeartRatesDao.eventosPorDia(idUsuario, fechaIni, fechaFin);
		for(EventosPorDia e:ed){
			listaDias.add(e.getFecha());
			listaN.add(e.getNeventos());
		}
		
		listaEventosDias.setFechas(listaDias);
		listaEventosDias.setnEventos(listaN);
		
		return listaEventosDias;		
	}
	
	@Transactional(readOnly = true)
	public ListaUserHeartRates obterUserHeartRates(Long idUsuario, Calendar fechaIni, Calendar fechaFin){
		
		
		//Recuperamos cuantos resultados devolveria en total
		Long totalResults = userHeartRatesDao.contarUserHeartRates(idUsuario, fechaIni, fechaFin);
				
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
		
		List<UserHeartRatesDTO> userHeartRates = userHeartRatesDao.obterUserHeartRatesWithLimit(idUsuario, fechaIni, 
				fechaFin, -1, returnedResults);	
		
		ListaUserHeartRates listado = new ListaUserHeartRates(totalResults, returnedResults, userHeartRates);
		
		return listado;
	}
	
	public void delete(String sourceId, Calendar starttime){
		
		UsuarioMovil usuarioMovil = usuarioMovilDao.findBySourceId(sourceId);
		if(usuarioMovil != null){
			userHeartRatesDao.delete(usuarioMovil.getId(), starttime);
		}	
				
	}
	
}
