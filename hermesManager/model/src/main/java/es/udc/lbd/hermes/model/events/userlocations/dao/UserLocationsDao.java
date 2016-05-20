package es.udc.lbd.hermes.model.events.userlocations.dao;

import java.util.Calendar;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.GroupedDTO;
import es.udc.lbd.hermes.model.events.userlocations.UserLocationDTO;
import es.udc.lbd.hermes.model.events.userlocations.UserLocations;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface UserLocationsDao extends GenericDao<UserLocations, Long> {

	public List<UserLocations> obterUserLocations();
	public List<UserLocations> obterUserLocationsSegunUsuario(Long idUsuario);
	public long contar();
	
	public Long contarUserLocations(Long idUsuario, Calendar fechaIni,Calendar fechaFin, Geometry bounds);
	
	public List<UserLocationDTO> obterUserLocationsWithLimit(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, Geometry bounds, int startIndex, Integer limit);
	
	public List<EventosPorDia> eventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public List<UserLocations> obterUserLocations(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, Geometry bounds, int startIndex, int count);
	
	public List<GroupedDTO> obterUserLocationsGrouped(Long idUsuario, Calendar fechaIni,Calendar fechaFin, Geometry bounds,int startIndex, Integer numberOfCells);
}
