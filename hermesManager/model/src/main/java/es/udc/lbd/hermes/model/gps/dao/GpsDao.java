package es.udc.lbd.hermes.model.gps.dao;

import java.util.Calendar;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.gps.Gps;
import es.udc.lbd.hermes.model.gps.GpsDTO;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface GpsDao extends GenericDao<Gps, Long>{

	public Long contarGpsLocations(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, Geometry bounds);
	
	public List<GpsDTO> obterGpsLocationWithLimit(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, Geometry bounds, int startIndex, Integer limit);
}
