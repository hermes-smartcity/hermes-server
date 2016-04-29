package es.udc.lbd.hermes.model.events.heartRateData.service;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.ListaHeartRateData;
import es.udc.lbd.hermes.model.events.heartRateData.HeartRateData;

public interface HeartRateDataService {

	public HeartRateData get(Long id);
	
	public void create(HeartRateData sleepData, String sourceId);
	
	public void update(HeartRateData sleepData);
	
	public void delete(Long id);
	
	public List<HeartRateData> obterHeartRateData();
	
	public List<HeartRateData> obterHeartRateDataSegunUsuario(Long idUsuario);
	
	public long contar();
	
	public ListaHeartRateData obterHeartRateData(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
}

