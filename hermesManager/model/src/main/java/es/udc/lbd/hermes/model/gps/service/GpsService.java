package es.udc.lbd.hermes.model.gps.service;

import es.udc.lbd.hermes.model.gps.Gps;
import es.udc.lbd.hermes.model.gps.GpssJson;

public interface GpsService {

	public void create(Gps gps, String userId);
	
	public void parserGpss(GpssJson gpssJson);
	
}
