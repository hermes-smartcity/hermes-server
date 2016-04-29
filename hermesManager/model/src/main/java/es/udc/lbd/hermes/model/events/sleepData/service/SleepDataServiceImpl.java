package es.udc.lbd.hermes.model.events.sleepData.service;

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
import es.udc.lbd.hermes.model.events.ListaSleepData;
import es.udc.lbd.hermes.model.events.sleepData.SleepData;
import es.udc.lbd.hermes.model.events.sleepData.SleepDataDTO;
import es.udc.lbd.hermes.model.events.sleepData.dao.SleepDataDao;
import es.udc.lbd.hermes.model.setting.Setting;
import es.udc.lbd.hermes.model.setting.dao.SettingDao;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.dao.UsuarioMovilDao;



@Service("sleepDataService")
@Transactional
public class SleepDataServiceImpl implements SleepDataService {
	
	@Autowired
	private SleepDataDao sleepDataDao;

	@Autowired
	private UsuarioMovilDao usuarioMovilDao;
	
	@Autowired
	private SettingDao settingDao;
	
	@Override
	@Transactional(readOnly = true)
	public SleepData get(Long id) {
		return sleepDataDao.get(id);
	}

	@Override
	public void create(SleepData sleepData, String sourceId) {	

		UsuarioMovil usuarioMovil = usuarioMovilDao.findBySourceId(sourceId);
		if(usuarioMovil == null){
			usuarioMovil = new UsuarioMovil();
			usuarioMovil.setSourceId(sourceId);
			usuarioMovilDao.create(usuarioMovil);
		}
		sleepData.setUsuarioMovil(usuarioMovil);
		sleepDataDao.create(sleepData);
		
	}

	@Override
	public void update(SleepData sleepData) {
		sleepDataDao.update(sleepData);
	}

	@Override
	public void delete(Long id) {
		SleepData sleepData = sleepDataDao.get(id);
		if (sleepData != null) {
			sleepDataDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<SleepData> obterSleepData() {
		List<SleepData> sleepData = sleepDataDao.obterSleepData();
		return sleepData;
	}
	
	@Transactional(readOnly = true)
	public List<SleepData> obterSleepDataSegunUsuario(Long idUsuario) {
		 List<SleepData> sleepData = sleepDataDao.obterSleepDataSegunUsuario(idUsuario);
		return sleepData;
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public long contar(){
		return sleepDataDao.contar();
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin) {		
		ListaEventosYdias listaEventosDias = new ListaEventosYdias();
		List<String> listaDias = new ArrayList<String>();
		List<BigInteger> listaN = new ArrayList<BigInteger>();
		List<EventosPorDia> ed = sleepDataDao.eventosPorDia(idUsuario, fechaIni, fechaFin);
		for(EventosPorDia e:ed){
			listaDias.add(e.getFecha());
			listaN.add(e.getNeventos());
		}
		
		listaEventosDias.setFechas(listaDias);
		listaEventosDias.setnEventos(listaN);
		
		return listaEventosDias;		
	}
	
	@Transactional(readOnly = true)
	public ListaSleepData obterSleepData(Long idUsuario, Calendar fechaIni, Calendar fechaFin){
		
		
		//Recuperamos cuantos resultados devolveria en total
		Long totalResults = sleepDataDao.contarSleepData(idUsuario, fechaIni, fechaFin);
				
		//Tenemos que limitar la consulta a un tamano maximo		
		//Para ello, recuperamos el valor limitQuery
		Setting settingLimit = settingDao.get(new Long(1));
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
		
		List<SleepDataDTO> sleepData = sleepDataDao.obterSleepDataWithLimit(idUsuario, fechaIni, 
				fechaFin, -1, returnedResults);	
		
		ListaSleepData listado = new ListaSleepData(totalResults, returnedResults, sleepData);
		
		return listado;
	}
}
