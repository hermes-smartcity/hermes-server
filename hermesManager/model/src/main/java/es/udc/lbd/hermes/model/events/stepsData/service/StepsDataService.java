package es.udc.lbd.hermes.model.events.stepsData.service;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.ListaStepsData;
import es.udc.lbd.hermes.model.events.stepsData.StepsData;

public interface StepsDataService {

	public StepsData get(Long id);
	
	public void create(StepsData stepsData, String sourceId);
	
	public void update(StepsData stepsData);
	
	public void delete(Long id);
	
	public List<StepsData> obterStepsData();
	
	public List<StepsData> obterStepsDataSegunUsuario(Long idUsuario);
	
	public long contar();
	
	public ListaStepsData obterStepsData(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
}
