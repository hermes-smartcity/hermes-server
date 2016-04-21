package es.udc.lbd.hermes.model.gps.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import es.udc.lbd.hermes.model.gps.Gps;
import es.udc.lbd.hermes.model.gps.GpsJson;
import es.udc.lbd.hermes.model.gps.GpssJson;
import es.udc.lbd.hermes.model.gps.dao.GpsDao;
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
}
