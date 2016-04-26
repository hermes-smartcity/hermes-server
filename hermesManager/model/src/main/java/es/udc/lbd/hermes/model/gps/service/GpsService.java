package es.udc.lbd.hermes.model.gps.service;

import java.util.Calendar;

import es.udc.lbd.hermes.model.events.ListaGpsLocation;
import es.udc.lbd.hermes.model.gps.Gps;
import es.udc.lbd.hermes.model.gps.GpssJson;

public interface GpsService {

	public void create(Gps gps, String userId);
	
	public void parserGpss(GpssJson gpssJson);
	
	public ListaGpsLocation obterGpsLocations(Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double wnLng, Double wnLat,	Double esLng, Double esLat);
}
