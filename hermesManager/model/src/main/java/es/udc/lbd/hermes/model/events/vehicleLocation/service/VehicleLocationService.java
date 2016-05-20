package es.udc.lbd.hermes.model.events.vehicleLocation.service;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.GroupedDTO;
import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.ListaVehicleLocations;
import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;

public interface VehicleLocationService {

	public VehicleLocation get(Long id);
	
	public void create(VehicleLocation vehicleLocation, String sourceId);
	
	public void update(VehicleLocation vehicleLocation);
	
	public void delete(Long id);
	
	public ListaVehicleLocations obterVehicleLocations(Long idUsuario, Calendar fechaIni, Calendar fechaFin,
	Double wnLng, Double wnLat,	Double esLng, Double esLat);
	
	public long contar();
	
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public List<GroupedDTO> obterVehicleLocationsGrouped(Long idUsuario, Calendar fechaIni, Calendar fechaFin, Double wnLng, Double wnLat,	Double esLng, Double esLat, int startIndex);
}
