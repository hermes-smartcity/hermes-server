package es.udc.lbd.hermes.model.events.stepsData.service;

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
import es.udc.lbd.hermes.model.events.ListaStepsData;
import es.udc.lbd.hermes.model.events.stepsData.StepsData;
import es.udc.lbd.hermes.model.events.stepsData.StepsDataDTO;
import es.udc.lbd.hermes.model.events.stepsData.dao.StepsDataDao;
import es.udc.lbd.hermes.model.setting.Setting;
import es.udc.lbd.hermes.model.setting.dao.SettingDao;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.dao.UsuarioMovilDao;

@Service("stepsDataService")
@Transactional
public class StepsDataServiceImpl implements StepsDataService {
	
	@Autowired
	private StepsDataDao stepsDataDao;
	
	@Autowired
	private UsuarioMovilDao usuarioMovilDao;
	
	@Autowired
	private SettingDao settingDao;
	
	@Override
	@Transactional(readOnly = true)
	public StepsData get(Long id) {
		return stepsDataDao.get(id);
	}

	@Override
	public void create(StepsData stepsData, String sourceId) {	

		UsuarioMovil usuarioMovil = usuarioMovilDao.findBySourceId(sourceId);
		if(usuarioMovil == null){
			usuarioMovil = new UsuarioMovil();
			usuarioMovil.setSourceId(sourceId);
			usuarioMovilDao.create(usuarioMovil);
		}	
		stepsData.setUsuarioMovil(usuarioMovil);
		stepsDataDao.create(stepsData);
		
	}

	@Override
	public void update(StepsData stepsData) {
		stepsDataDao.update(stepsData);
	}

	@Override
	public void delete(Long id) {
		StepsData stepsData = stepsDataDao.get(id);
		if (stepsData != null) {
			stepsDataDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<StepsData> obterStepsData() {
		List<StepsData> stepsData = stepsDataDao.obterStepsData();
		return stepsData;
	}
	
	@Transactional(readOnly = true)
	public List<StepsData> obterStepsDataSegunUsuario(Long idUsuario) {
		 List<StepsData> stepsData = stepsDataDao.obterStepsDataSegunUsuario(idUsuario);
		return stepsData;
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public long contar(){
		return stepsDataDao.contar();
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin) {		
		ListaEventosYdias listaEventosDias = new ListaEventosYdias();
		List<String> listaDias = new ArrayList<String>();
		List<BigInteger> listaN = new ArrayList<BigInteger>();
		List<EventosPorDia> ed = stepsDataDao.eventosPorDia(idUsuario, fechaIni, fechaFin);
		for(EventosPorDia e:ed){
			listaDias.add(e.getFecha());
			listaN.add(e.getNeventos());
		}
		
		listaEventosDias.setFechas(listaDias);
		listaEventosDias.setnEventos(listaN);
		
		return listaEventosDias;		
	}
	
	@Transactional(readOnly = true)
	public ListaStepsData obterStepsData(Long idUsuario, Calendar fechaIni, Calendar fechaFin){
		
		
		//Recuperamos cuantos resultados devolveria en total
		Long totalResults = stepsDataDao.contarStepsData(idUsuario, fechaIni, fechaFin);
				
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
		
		List<StepsDataDTO> stepsData = stepsDataDao.obterStepsDataWithLimit(idUsuario, fechaIni, 
				fechaFin, -1, returnedResults);	
		
		ListaStepsData listado = new ListaStepsData(totalResults, returnedResults, stepsData);
		
		return listado;
	}
}
