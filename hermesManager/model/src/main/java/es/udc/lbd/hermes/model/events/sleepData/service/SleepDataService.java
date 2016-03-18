package es.udc.lbd.hermes.model.events.sleepData.service;

import java.util.List;

import es.udc.lbd.hermes.model.events.sleepData.SleepData;

public interface SleepDataService {

	public SleepData get(Long id);
	
	public void create(SleepData sleepData, String sourceId);
	
	public void update(SleepData sleepData);
	
	public void delete(Long id);
	
	public List<SleepData> obterSleepData();
	
	public List<SleepData> obterSleepDataSegunUsuario(Long idUsuario);
	
	public long contar();
}
