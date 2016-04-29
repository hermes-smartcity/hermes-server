package es.udc.lbd.hermes.model.events.userlocations.service;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.ListaUserLocations;
import es.udc.lbd.hermes.model.events.userlocations.UserLocations;

public interface UserLocationsService {

	public UserLocations get(Long id);
	
	public void create(UserLocations userLocations, String sourceId);
	
	public void update(UserLocations userLocations);
	
	public void delete(Long id);
	
	public List<UserLocations> obterUserLocations();
	
	public List<UserLocations> obterUserLocationsSegunUsuario(Long idUsuario);
	
	public long contar();
	
	public ListaUserLocations obterUserLocations(Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double wnLng, Double wnLat,	Double esLng, Double esLat);
	
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
}
