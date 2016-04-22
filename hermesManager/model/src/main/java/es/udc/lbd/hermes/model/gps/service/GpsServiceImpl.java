package es.udc.lbd.hermes.model.gps.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import es.udc.lbd.hermes.model.events.ListaGpsLocation;
import es.udc.lbd.hermes.model.gps.Gps;
import es.udc.lbd.hermes.model.gps.GpsDTO;
import es.udc.lbd.hermes.model.gps.GpsJson;
import es.udc.lbd.hermes.model.gps.GpssJson;
import es.udc.lbd.hermes.model.gps.dao.GpsDao;
import es.udc.lbd.hermes.model.setting.Setting;
import es.udc.lbd.hermes.model.setting.dao.SettingDao;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.dao.UsuarioMovilDao;
import es.udc.lbd.hermes.model.util.HelpersModel;

@Service("gpsService")
@Transactional
public class GpsServiceImpl implements GpsService{

	@Autowired
	private GpsDao gpsDao;
	
	@Autowired
	private UsuarioMovilDao usuarioMovilDao;
	
	@Autowired
	private SettingDao settingDao;
	
	@Override
	public void create(Gps gps, String userId) {
				
		UsuarioMovil usuarioMovil= usuarioMovilDao.findBySourceId(userId);
		if(usuarioMovil == null){
			usuarioMovil = new UsuarioMovil();
			usuarioMovil.setSourceId(userId);
			usuarioMovilDao.create(usuarioMovil);
		}
		gps.setUsuarioMovil(usuarioMovil);
		gpsDao.create(gps);
	}
	
	public void parserGpss(GpssJson gpssJson){
		String userId = gpssJson.getUserid();
		String provider = gpssJson.getProvider();
		List<GpsJson> navegadoresJson = gpssJson.getGps();
		
		for (GpsJson gpsJson : navegadoresJson) {
			//Creamos el gps
			Calendar time = Calendar.getInstance();
			time.setTimeInMillis(gpsJson.getTime());
			
			Geometry position =  HelpersModel.prepararPunto(gpsJson.getLatitude(), gpsJson.getLongitude());
			
			Gps gps = new Gps(provider, time, (Point)position, gpsJson.getAltitude(), 
					gpsJson.getSpeed(), gpsJson.getBearing(), gpsJson.getAccuracy());
			
			create(gps, userId);
		}
		
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public ListaGpsLocation obterGpsLocations(Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double wnLng, Double wnLat,	Double esLng, Double esLat){
		
		Geometry polygon =  HelpersModel.prepararPoligono(wnLng, wnLat, esLng, esLat);

		//Recuperamos cuantos resultados devolveria en total
		Long totalResults = gpsDao.contarGpsLocations(idUsuario, fechaIni, fechaFin, polygon);

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

		List<GpsDTO> gpss = gpsDao.obterGpsLocationWithLimit(idUsuario, fechaIni, fechaFin, polygon, -1, -1);

		ListaGpsLocation listado = new ListaGpsLocation(totalResults, returnedResults, gpss);

		return listado;
	}
}
