package es.udc.lbd.hermes.model.events.contextData.dao;

import java.util.Calendar;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.contextData.ContextData;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface ContextDataDao extends GenericDao<ContextData, Long> {
	
	public List<ContextData> obterContextData();
	public List<ContextData> obterContextDataSegunUsuario(Long idUsuario);
	public List<ContextData> obterContextData(Long idUsuario, Calendar fechaIni,Calendar fechaFin, Geometry bounds, int startIndex, int count);
	public List<EventosPorDia> eventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	public long contar();
}
