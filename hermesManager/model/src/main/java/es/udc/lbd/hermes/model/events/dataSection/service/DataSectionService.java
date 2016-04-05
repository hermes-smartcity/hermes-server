package es.udc.lbd.hermes.model.events.dataSection.service;

import java.util.Calendar;

import es.udc.lbd.hermes.model.events.ListaDataSection;
import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.dataSection.DataSection;

public interface DataSectionService {

	public DataSection get(Long id);
	
	public void create(DataSection dataSection, String sourceId);
	
	public void update(DataSection dataSection);
	
	public void delete(Long id);

	public ListaDataSection obterDataSections(Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double wnLng, Double wnLat,	Double esLng, Double esLat);
		
	public long contar();
	
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
}
