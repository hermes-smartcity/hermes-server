package es.udc.lbd.hermes.model.events.driverFeatures.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.ListaDriverFeatures;
import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.driverFeatures.DriverFeatures;
import es.udc.lbd.hermes.model.events.driverFeatures.DriverFeaturesDTO;
import es.udc.lbd.hermes.model.events.driverFeatures.dao.DriverFeaturesDao;
import es.udc.lbd.hermes.model.setting.Setting;
import es.udc.lbd.hermes.model.setting.dao.SettingDao;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.dao.UsuarioMovilDao;


@Service("driverFeaturesService")
@Transactional
public class DriverFeaturesServiceImpl implements DriverFeaturesService {
	
	@Autowired
	private DriverFeaturesDao driverFeaturesDao;
	
	@Autowired
	private UsuarioMovilDao usuarioMovilDao;
	
	@Autowired
	private SettingDao settingDao;
	
	@Override
	@Transactional(readOnly = true)
	public DriverFeatures get(Long id) {
		return driverFeaturesDao.get(id);
	}

	@Override
	public void create(DriverFeatures driverFeatures, String sourceId) {	
		UsuarioMovil usuarioMovil = usuarioMovilDao.findBySourceId(sourceId);
		if(usuarioMovil == null){
			usuarioMovil = new UsuarioMovil();
			usuarioMovil.setSourceId(sourceId);
			usuarioMovilDao.create(usuarioMovil);
		}		
		driverFeatures.setUsuarioMovil(usuarioMovil);
		driverFeaturesDao.create(driverFeatures);		
	}

	@Override
	public void update(DriverFeatures driverFeatures) {
		driverFeaturesDao.update(driverFeatures);
	}

	@Override
	public void delete(Long id) {
		DriverFeatures driverFeatures = driverFeaturesDao.get(id);
		if (driverFeatures != null) {
			driverFeaturesDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public List<DriverFeatures> obterDriverFeaturess() {
		List<DriverFeatures> driverFeaturess = driverFeaturesDao.obterDriverFeaturess();
		return driverFeaturess;
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public List<DriverFeatures> obterDriverFeaturesSegunUsuario(Long idUsuario) {
		List<DriverFeatures> driverFeaturess = driverFeaturesDao.obterDriverFeaturesSegunUsuario(idUsuario);
		return driverFeaturess;
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public long contar(){
		return driverFeaturesDao.contar();
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin) {		
		ListaEventosYdias listaEventosDias = new ListaEventosYdias();
		List<String> listaDias = new ArrayList<String>();
		List<BigInteger> listaN = new ArrayList<BigInteger>();
		List<EventosPorDia> ed = driverFeaturesDao.eventosPorDia(idUsuario, fechaIni, fechaFin);
		for(EventosPorDia e:ed){
			listaDias.add(e.getFecha());
			listaN.add(e.getNeventos());
		}
		
		listaEventosDias.setFechas(listaDias);
		listaEventosDias.setnEventos(listaN);
		
		return listaEventosDias;		
	}
	
	@Transactional(readOnly = true)
	public ListaDriverFeatures obterDriverFeatures(Long idUsuario, Calendar fechaIni, Calendar fechaFin){
		
		
		//Recuperamos cuantos resultados devolveria en total
		Long totalResults = driverFeaturesDao.contarDriverFeatures(idUsuario, fechaIni, fechaFin);
				
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
		
		List<DriverFeaturesDTO> driverFeatures = driverFeaturesDao.obterDriverFeaturesWithLimit(idUsuario, fechaIni, 
				fechaFin, -1, returnedResults);	
		
		ListaDriverFeatures listado = new ListaDriverFeatures(totalResults, returnedResults, driverFeatures);
		
		return listado;
	}
}
