package es.udc.lbd.hermes.model.events.heartRateData.service;

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
import es.udc.lbd.hermes.model.events.ListaHeartRateData;
import es.udc.lbd.hermes.model.events.heartRateData.HeartRateData;
import es.udc.lbd.hermes.model.events.heartRateData.HeartRateDataDTO;
import es.udc.lbd.hermes.model.events.heartRateData.dao.HeartRateDataDao;
import es.udc.lbd.hermes.model.setting.Setting;
import es.udc.lbd.hermes.model.setting.dao.SettingDao;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.dao.UsuarioMovilDao;
import es.udc.lbd.hermes.model.util.SettingsName;



@Service("heartRateDataService")
@Transactional
public class HeartRateDataServiceImpl implements HeartRateDataService {
	
	@Autowired
	private HeartRateDataDao heartRateDataDao;
	
	@Autowired
	private UsuarioMovilDao usuarioMovilDao;
	
	@Autowired
	private SettingDao settingDao;
	
	@Override
	@Transactional(readOnly = true)
	public HeartRateData get(Long id) {
		return heartRateDataDao.get(id);
	}

	@Override
	public void create(HeartRateData heartRateData, String sourceId) {	
		UsuarioMovil usuarioMovil = usuarioMovilDao.findBySourceId(sourceId);
		if(usuarioMovil == null){
			usuarioMovil = new UsuarioMovil();
			usuarioMovil.setSourceId(sourceId);
			usuarioMovilDao.create(usuarioMovil);
		}		
		heartRateData.setUsuarioMovil(usuarioMovil);
		heartRateDataDao.create(heartRateData);
		
	}

	@Override
	public void update(HeartRateData heartRateData) {
		heartRateDataDao.update(heartRateData);
	}

	@Override
	public void delete(Long id) {
		HeartRateData heartRateData = heartRateDataDao.get(id);
		if (heartRateData != null) {
			heartRateDataDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<HeartRateData> obterHeartRateData() {
		List<HeartRateData> heartRateData = heartRateDataDao.obterHeartRateData();
		return heartRateData;
	}
	
	@Transactional(readOnly = true)
	public List<HeartRateData> obterHeartRateDataSegunUsuario(Long idUsuario) {
		 List<HeartRateData> heartRateData = heartRateDataDao.obterHeartRateDataSegunUsuario(idUsuario);
		return heartRateData;
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public long contar(){
		return heartRateDataDao.contar();
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin) {		
		ListaEventosYdias listaEventosDias = new ListaEventosYdias();
		List<String> listaDias = new ArrayList<String>();
		List<BigInteger> listaN = new ArrayList<BigInteger>();
		List<EventosPorDia> ed = heartRateDataDao.eventosPorDia(idUsuario, fechaIni, fechaFin);
		for(EventosPorDia e:ed){
			listaDias.add(e.getFecha());
			listaN.add(e.getNeventos());
		}
		
		listaEventosDias.setFechas(listaDias);
		listaEventosDias.setnEventos(listaN);
		
		return listaEventosDias;		
	}
	
	@Transactional(readOnly = true)
	public ListaHeartRateData obterHeartRateData(Long idUsuario, Calendar fechaIni, Calendar fechaFin){
		
		
		//Recuperamos cuantos resultados devolveria en total
		Long totalResults = heartRateDataDao.contarHeartRateData(idUsuario, fechaIni, fechaFin);
				
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
		
		List<HeartRateDataDTO> heartRateData = heartRateDataDao.obterHeartRateDataWithLimit(idUsuario, fechaIni, 
				fechaFin, -1, returnedResults);	
		
		ListaHeartRateData listado = new ListaHeartRateData(totalResults, returnedResults, heartRateData);
		
		return listado;
	}

}
