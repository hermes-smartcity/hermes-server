package es.udc.lbd.hermes.model.events.contextData.service;

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
import es.udc.lbd.hermes.model.events.ListaContextData;
import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.contextData.ContextData;
import es.udc.lbd.hermes.model.events.contextData.ContextDataDTO;
import es.udc.lbd.hermes.model.events.contextData.dao.ContextDataDao;
import es.udc.lbd.hermes.model.setting.Setting;
import es.udc.lbd.hermes.model.setting.dao.SettingDao;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.dao.UsuarioMovilDao;
import es.udc.lbd.hermes.model.util.HelpersModel;

@Service("contextDataService")
@Transactional
public class ContextDataServiceImpl implements ContextDataService {
	
	@Autowired
	private ContextDataDao contextDataDao;
	
	@Autowired
	private UsuarioMovilDao usuarioMovilDao;
	
	@Autowired
	private SettingDao settingDao;
	
	@Override
	@Transactional(readOnly = true)
	public ContextData get(Long id) {
		return contextDataDao.get(id);
	}

	@Override
	public void create(ContextData contextData, String sourceId) {	

		UsuarioMovil usuarioMovil = usuarioMovilDao.findBySourceId(sourceId);
		if(usuarioMovil == null){
			usuarioMovil = new UsuarioMovil();
			usuarioMovil.setSourceId(sourceId);
			usuarioMovilDao.create(usuarioMovil);
		}	
		contextData.setUsuarioMovil(usuarioMovil);
		contextDataDao.create(contextData);
		
	}

	@Override
	public void update(ContextData contextData) {
		contextDataDao.update(contextData);
	}

	@Override
	public void delete(Long id) {
		ContextData contextData = contextDataDao.get(id);
		if (contextData != null) {
			contextDataDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<ContextData> obterContextData() {
		List<ContextData> contextData = contextDataDao.obterContextData();
		return contextData;
	}
	
	@Transactional(readOnly = true)
	public List<ContextData> obterContextDataSegunUsuario(Long idUsuario) {
		 List<ContextData> contextData = contextDataDao.obterContextDataSegunUsuario(idUsuario);
		return contextData;
	}

	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public ListaContextData obterContextData(Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double wnLng, Double wnLat,	Double esLng, Double esLat) {
		Geometry polygon =  HelpersModel.prepararPoligono(wnLng, wnLat, esLng, esLat);

		//Recuperamos cuantos resultados devolveria en total
		Long totalResults = contextDataDao.contarContextData(idUsuario, fechaIni, fechaFin, polygon);

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
		
		List<ContextDataDTO> contextData = contextDataDao.obterContextDataWithLimit(idUsuario, fechaIni, 
				fechaFin, polygon, -1, returnedResults);

		ListaContextData listado = new ListaContextData(totalResults, returnedResults, contextData);

		return listado;
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public long contar(){
		return contextDataDao.contar();
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin) {		
		ListaEventosYdias listaEventosDias = new ListaEventosYdias();
		List<String> listaDias = new ArrayList<String>();
		List<BigInteger> listaN = new ArrayList<BigInteger>();
		List<EventosPorDia> ed = contextDataDao.eventosPorDia(idUsuario, fechaIni, fechaFin);
		for(EventosPorDia e:ed){
			listaDias.add(e.getFecha());
			listaN.add(e.getNeventos());
		}
		
		listaEventosDias.setFechas(listaDias);
		listaEventosDias.setnEventos(listaN);
		
		return listaEventosDias;		
	}
	
	
}
