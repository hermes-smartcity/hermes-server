package es.udc.lbd.hermes.model.events.dataSection.service;

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
import es.udc.lbd.hermes.model.events.ListaDataSection;
import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.dataSection.DataSection;
import es.udc.lbd.hermes.model.events.dataSection.dao.DataSectionDao;
import es.udc.lbd.hermes.model.setting.Setting;
import es.udc.lbd.hermes.model.setting.dao.SettingDao;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.dao.UsuarioMovilDao;
import es.udc.lbd.hermes.model.util.HelpersModel;


@Service("dataSectionService")
@Transactional
public class DataSectionServiceImpl implements DataSectionService {
	
	@Autowired
	private DataSectionDao dataSectionDao;
	
	@Autowired
	private UsuarioMovilDao usuarioMovilDao;
	
	@Autowired
	private SettingDao settingDao;
	
	@Override
	@Transactional(readOnly = true)
	public DataSection get(Long id) {
		return dataSectionDao.get(id);
	}

	@Override
	public void create(DataSection dataSection, String sourceId) {	
		UsuarioMovil usuarioMovil = usuarioMovilDao.findBySourceId(sourceId);
		if(usuarioMovil == null){
			usuarioMovil = new UsuarioMovil();
			usuarioMovil.setSourceId(sourceId);
			usuarioMovilDao.create(usuarioMovil);
		}		
		dataSection.setUsuarioMovil(usuarioMovil);
		dataSectionDao.create(dataSection);		
	}

	@Override
	public void update(DataSection dataSection) {
		dataSectionDao.update(dataSection);
	}

	@Override
	public void delete(Long id) {
		DataSection dataSection = dataSectionDao.get(id);
		if (dataSection != null) {
			dataSectionDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public ListaDataSection obterDataSections(Long idUsuarioMovil, Calendar fechaIni, Calendar fechaFin,
			Double wnLng, Double wnLat,	Double esLng, Double esLat) {
		Geometry polygon =  HelpersModel.prepararPoligono(wnLng, wnLat, esLng, esLat);

		//Recuperamos cuantos resultados devolveria en total
		Long totalResults = dataSectionDao.contarDataSections(idUsuarioMovil, fechaIni, fechaFin, polygon);

		//Tenemos que limitar la consulta a un tamano maximo		
		//Para ello, recuperamos el valor limitQuery
		Setting settingLimit = settingDao.get(new Long(1));
		Integer returnedResults = totalResults.intValue();
		if (settingLimit != null){
			returnedResults = settingLimit.getValueNumber().intValue();
		}

		List<DataSection> dataSections = dataSectionDao.obterDataSectionsWithLimit(idUsuarioMovil, fechaIni, fechaFin, polygon, -1, returnedResults);

		ListaDataSection listado = new ListaDataSection(totalResults, returnedResults, dataSections);

		return listado;
		
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public long contar(){
		return dataSectionDao.contar();
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public ListaEventosYdias obterEventosPorDia(Long idUsuarioMovil, Calendar fechaIni, Calendar fechaFin) {		
		ListaEventosYdias listaEventosDias = new ListaEventosYdias();
		List<String> listaDias = new ArrayList<String>();
		List<BigInteger> listaN = new ArrayList<BigInteger>();
		List<EventosPorDia> ed = dataSectionDao.eventosPorDia(idUsuarioMovil, fechaIni, fechaFin);
		for(EventosPorDia e:ed){
			listaDias.add(e.getFecha());
			listaN.add(e.getNeventos());
		}
		
		listaEventosDias.setFechas(listaDias);
		listaEventosDias.setnEventos(listaN);
		
		return listaEventosDias;		
	}
}
