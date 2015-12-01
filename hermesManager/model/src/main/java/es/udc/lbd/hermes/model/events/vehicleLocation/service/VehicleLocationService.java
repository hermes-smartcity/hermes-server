package es.udc.lbd.hermes.model.events.vehicleLocation.service;

import java.util.Calendar;
import java.util.List;
import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;
import es.udc.lbd.hermes.model.util.dao.BloqueElementos;

public interface VehicleLocationService {

	public VehicleLocation get(Long id);
	
	public void create(VehicleLocation vehicleLocation, String sourceId);
	
	public void update(VehicleLocation vehicleLocation);
	
	public void delete(Long id);
	
	public List<VehicleLocation> obterVehicleLocations(Long idUsuario, Calendar fechaIni, Calendar fechaFin,
	Double wnLng, Double wnLat,	Double esLng, Double esLat);
	
	public BloqueElementos<VehicleLocation> obterVehicleLocationsPaginados(Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double wnLng, Double wnLat,	Double esLng, Double esLat, int paxina);
	
	public long contar();
}
