package es.udc.lbd.hermes.model.events.contextData.service;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.GroupedDTO;
import es.udc.lbd.hermes.model.events.ListaContextData;
import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.contextData.ContextData;

public interface ContextDataService {

	public ContextData get(Long id);
	
	public void create(ContextData contextData, String sourceId);
	
	public void update(ContextData contextData);
	
	public void delete(Long id);
	
	public List<ContextData> obterContextData();
	
	public List<ContextData> obterContextDataSegunUsuario(Long idUsuario);
	
	public ListaContextData obterContextData(Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double wnLng, Double wnLat,	Double esLng, Double esLat);
	
	public long contar();
	
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public List<GroupedDTO> obterContextDataGrouped(Long idUsuario, Calendar fechaIni,Calendar fechaFin, Double wnLng, Double wnLat,	Double esLng, Double esLat,int startIndex);
}
