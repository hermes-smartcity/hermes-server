package es.udc.lbd.hermes.model.events.measurement.service;

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
import es.udc.lbd.hermes.model.events.GroupedDTO;
import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.ListaMeasurement;
import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.measurement.MeasurementDTO;
import es.udc.lbd.hermes.model.events.measurement.MeasurementType;
import es.udc.lbd.hermes.model.events.measurement.dao.MeasurementDao;
import es.udc.lbd.hermes.model.setting.Setting;
import es.udc.lbd.hermes.model.setting.dao.SettingDao;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.dao.UsuarioMovilDao;
import es.udc.lbd.hermes.model.util.HelpersModel;

@Service("measurementService")
@Transactional
public class MeasurementServiceImpl implements MeasurementService {

	@Autowired
	private MeasurementDao measurementDao;

	@Autowired
	private UsuarioMovilDao usuarioMovilDao;

	@Autowired
	private SettingDao settingDao;

	@Override
	@Transactional(readOnly = true)
	public Measurement get(Long id) {
		return measurementDao.get(id);
	}

	@Override
	public void create(Measurement measurement, String sourceId) {	
		UsuarioMovil usuarioMovil= usuarioMovilDao.findBySourceId(sourceId);
		if(usuarioMovil == null){
			usuarioMovil = new UsuarioMovil();
			usuarioMovil.setSourceId(sourceId);
			usuarioMovilDao.create(usuarioMovil);
		}
		measurement.setUsuarioMovil(usuarioMovil);
		measurementDao.create(measurement);

	}

	@Override
	public void update(Measurement measurement) {
		measurementDao.update(measurement);
	}

	@Override
	public void delete(Long id) {
		Measurement measurement = measurementDao.get(id);
		if (measurement != null) {
			measurementDao.delete(id);
		}
	}

	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public ListaMeasurement obterMeasurementsSegunTipo(MeasurementType tipo,Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double wnLng, Double wnLat,	Double esLng, Double esLat) {
		Geometry polygon =  HelpersModel.prepararPoligono(wnLng, wnLat, esLng, esLat);

		//Recuperamos cuantos resultados devolveria en total
		Long totalResults = measurementDao.contarMeasurementsSegunTipo(tipo, idUsuario, fechaIni, fechaFin, polygon);

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

		List<MeasurementDTO> measurements = measurementDao.obterMeasurementsSegunTipoWithLimit(tipo, idUsuario, fechaIni, fechaFin, polygon, -1, -1);

		ListaMeasurement listado = new ListaMeasurement(totalResults, returnedResults, measurements);

		return listado;
	}

	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public long contar() {
		return measurementDao.contar(null);
	}

	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public ListaEventosYdias obterEventosPorDia(MeasurementType tipo, Long idUsuario, Calendar fechaIni, Calendar fechaFin) {		
		ListaEventosYdias listaEventosDias = new ListaEventosYdias();
		List<String> listaDias = new ArrayList<String>();
		List<BigInteger> listaN = new ArrayList<BigInteger>();
		List<EventosPorDia> ed = measurementDao.eventosPorDia(tipo, idUsuario, fechaIni, fechaFin);
		for(EventosPorDia e:ed){
			listaDias.add(e.getFecha());
			listaN.add(e.getNeventos());
		}

		listaEventosDias.setFechas(listaDias);
		listaEventosDias.setnEventos(listaN);

		return listaEventosDias;

	}
	
	@Transactional(readOnly = true)
	public List<GroupedDTO> obterMeasurementGrouped(Long idUsuario, MeasurementType type, Calendar fechaIni,Calendar fechaFin, Double wnLng, Double wnLat,	Double esLng, Double esLat,int startIndex){
		
		Geometry bounds =  HelpersModel.prepararPoligono(wnLng, wnLat, esLng, esLat);
		
		//Recuperamos el numero de celdas
		Setting settingNumberOfCells  = settingDao.get(new Long(2));
		Integer numberOfCells = 5;
		if (settingNumberOfCells != null){
			numberOfCells = settingNumberOfCells.getValueNumber().intValue();
		}

		return measurementDao.obterMeasurementGrouped(idUsuario, type, fechaIni, fechaFin, bounds, startIndex, numberOfCells);
	}
}
